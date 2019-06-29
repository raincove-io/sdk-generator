package io.github.erfangc.sdk.generator.java.models;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import io.github.erfangc.sdk.generator.IOUtils;
import io.github.erfangc.sdk.generator.java.Import;
import io.github.erfangc.sdk.generator.java.JavaCaseUtil;
import io.github.erfangc.sdk.generator.java.JavaOptions;
import io.github.erfangc.sdk.generator.java.JavaType;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static io.github.erfangc.sdk.generator.java.JavaCaseUtil.toPascalCase;
import static io.github.erfangc.sdk.generator.java.JavaTypeResolver.toJavaType;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class ModelsProcessor {

    private static Logger logger = LoggerFactory.getLogger(ModelsProcessor.class);
    private JavaOptions options;

    public ModelsProcessor(JavaOptions options) {
        this.options = options;
    }

    private static String toNestedTypeName(List<String> parentNames, String propertyName) {
        final String singular = fromPlural(propertyName);
        return Stream.concat(parentNames.stream(), Stream.of(singular)).map(JavaCaseUtil::toPascalCase).collect(joining());
    }

    /**
     * Returns the non-plural form of a plural noun like: cars -> car, children -> child, people -> person, etc.
     *
     * @param str
     * @return the non-plural form of a plural noun like: cars -> car, children -> child, people -> person, etc.
     */
    private static String fromPlural(String str) {
        if (str.toLowerCase().endsWith("es") && !shouldEndWithE(str))
            return str.substring(0, str.toLowerCase().lastIndexOf("es"));
        else if (str.toLowerCase().endsWith("s")) return str.substring(0, str.toLowerCase().lastIndexOf('s'));
        else if (str.toLowerCase().endsWith("children")) return str.substring(0, str.toLowerCase().lastIndexOf("ren"));
        else if (str.toLowerCase().endsWith("people"))
            return str.substring(0, str.toLowerCase().lastIndexOf("ople")) + "rson";
        else return str;
    }

    /**
     * @param str
     * @return true is the singular form of a word should end with the letter "e"
     */
    private static boolean shouldEndWithE(String str) {
        return str.toLowerCase().endsWith("iece")
                || str.toLowerCase().endsWith("ice")
                || str.toLowerCase().endsWith("ace")
                || str.toLowerCase().endsWith("ise")
                ;
    }

    public List<Model> processSchema(Schema schema, String schemaName) {
        return processSchema(schema, schemaName, new ArrayList<>());
    }

    private List<Model> processSchema(Schema schema, String typeName, List<String> parentTypeNames) {
        final JavaOptions options = getOptions();
        List<Model> ret = new ArrayList<>();
        //
        // start processing the model
        //
        final Context context = new Context();
        List<Property> properties = new ArrayList<>();
        //
        // start processing properties
        //
        final Map<String, Schema> schemaProperties = schema.getProperties();

        //
        // consider the special case where the schema has no properties (we still create an empty object)
        // and / or the schema is of a literal type (string enum)
        //
        if (schemaProperties != null) {
            for (Map.Entry<String, Schema> schemaEntry : schemaProperties.entrySet()) {
                final String propertyName = schemaEntry.getKey();
                final Schema propertySchema = schemaEntry.getValue();
                //
                // determine the property type, and whether it requires recursive processing
                //
                final String packageName = options.getModelsPackageName();
                if (isNestedSchema(propertySchema)) {
                    //
                    // process nested schema that is not explicitly named or referenced
                    //
                    final List<String> psn = new ArrayList<>(parentTypeNames);
                    psn.add(typeName);
                    String nestedTypeName = toNestedTypeName(psn, propertyName);
                    String propertyType = nestedTypeName;
                    boolean isArray = propertySchema.getType().equals("array");
                    List<Model> nestedModels;

                    if (isArray) {
                        final ArraySchema arraySchema = (ArraySchema) propertySchema;
                        nestedModels = processSchema(arraySchema.getItems(), nestedTypeName, psn);
                    } else {
                        nestedModels = processSchema(propertySchema, nestedTypeName, psn);
                    }

                    ret.addAll(nestedModels);
                    //
                    // add the processed nested type as a property of the parent
                    //
                    String propertyPascalName = toPascalCase(propertyName);
                    List<Import> imports = new ArrayList<>();
                    imports.add(new Import().setPackageName(packageName).setTypeName(nestedTypeName));
                    if (isArray) {
                        propertyType = "List<" + nestedTypeName + ">";
                        imports.add(new Import().setPackageName("java.util").setTypeName("List"));
                    }
                    final Property property = new Property()
                            .setType(propertyType)
                            .setGetterName(propertyPascalName)
                            .setSetterName(propertyPascalName)
                            .setImports(imports)
                            .setName(propertyName);
                    properties.add(property);
                } else {
                    //
                    // process an ordinary reference or a primitive
                    //
                    final JavaType javaType = toJavaType(propertySchema, packageName);
                    if (javaType == null) {
                        logger.error("Cannot determine the type of propertySchema {}", propertySchema.toString());
                        throw new IllegalArgumentException("Cannot determine the type of propertySchema " + propertySchema.toString());
                    }
                    final String pascalCasePropertyName = toPascalCase(propertyName);
                    final Property property = new Property()
                            .setType(javaType.getTypeName())
                            .setGetterName(pascalCasePropertyName)
                            .setSetterName(pascalCasePropertyName)
                            .setImports(javaType.getRequiredImports())
                            .setName(propertyName);
                    properties.add(property);
                }
            }
        }

        context.setProperties(properties);
        context.setSchema(schema);
        context.setJavaType(new JavaType().setTypeName(typeName));
        context.setPackageName(options.getModelsPackageName());
        context.setImports(distinctImports(properties));

        if (properties.isEmpty()) {
            Model model = processPropertyLessSchema(context);
            ret.add(model);
        } else {
            Model model = new Model();
            model.setContext(context);
            model.setId(typeName);
            final Template template = Mustache.compiler().compile(IOUtils.readResourceFully("java/models/template.mustache"));
            String javaCode = template.execute(context);
            model.setJavaCode(javaCode);
            ret.add(model);
        }

        return ret;
    }

    private Model processPropertyLessSchema(Context context) {
        final Schema schema = context.getSchema();
        //
        // since the schema has no properties, we infer its Java code from its type
        // if the schema is of an primitive type we will simply return a type extending the boxed version (TODO)
        // if the schema is of a object type we will create an empty object
        // if the schema is of a string type with a enum property defined we will create a Java enum
        //
        final List schemaEnum = schema.getEnum();
        if ("string".equals(schema.getType()) && schemaEnum != null) {
            List<Enum> enums = new ArrayList<>();
            for (int i = 0; i < schemaEnum.size(); i++) {
                final Object o = schemaEnum.get(i);
                enums.add(
                        new Enum()
                                .setLast(i == schemaEnum.size() - 1)
                                .setValue(o.toString())
                );
            }
            context.setEnums(enums);
            final Template template = Mustache.compiler().compile(IOUtils.readResourceFully("java/models/enum.mustache"));
            final String javaCode = template.execute(context);
            return new Model().setContext(context).setJavaCode(javaCode).setId(context.getJavaType().getTypeName());
        } else {
            final Template template = Mustache.compiler().compile(IOUtils.readResourceFully("java/models/template.mustache"));
            String javaCode = template.execute(context);
            return new Model().setContext(context).setJavaCode(javaCode).setId(context.getJavaType().getTypeName());
        }
    }

    private List<Import> distinctImports(List<Property> properties) {
        return properties
                .stream()
                .flatMap(p -> p.getImports().stream())
                // filter out all imports in the same package as the models package
                .filter(p -> !p.getPackageName().equals(options.getModelsPackageName()))
                .distinct()
                .collect(toList());
    }

    private boolean isNestedSchema(Schema schemaValue) {
        //
        // a schema is considered nested if it does not define a type of a primitive type nor $ref but contains a "properties" property
        //
        final boolean isNestedArrayType = schemaValue instanceof ArraySchema
                && ((ArraySchema) schemaValue).getItems().getProperties() != null
                && !((ArraySchema) schemaValue).getItems().getProperties().isEmpty();
        final boolean isNestedLiteralType = schemaValue.getProperties() != null && !schemaValue.getProperties().isEmpty();
        return isNestedLiteralType || isNestedArrayType;
    }

    private JavaOptions getOptions() {
        return options;
    }

    public ModelsProcessor setOptions(JavaOptions options) {
        this.options = options;
        return this;
    }

}
