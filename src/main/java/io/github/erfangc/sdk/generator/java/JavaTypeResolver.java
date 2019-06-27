package io.github.erfangc.sdk.generator.java;

import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;

import java.util.ArrayList;
import java.util.List;

import static java.text.MessageFormat.format;
import static java.util.Collections.singletonList;

public class JavaTypeResolver {

    public static JavaType toJavaType(Schema schema, String packageName) {
        JavaType type = null;
        final String schemaType = schema.getType();
        if ("number".equals(schemaType)) {
            final String format = schema.getFormat();
            if ("int32".equals(format)) {
                type = new JavaType().setTypeName("Integer");
            } else if ("int64".equals(schema.getFormat())) {
                type = new JavaType().setTypeName("Long");
            } else {
                type = new JavaType().setTypeName("Double");
            }
        } else if ("boolean".equals(schemaType)) {
            type = new JavaType().setTypeName("Boolean");
        } else if ("integer".equals(schemaType)) {
            type = new JavaType().setTypeName("Integer");
        } else if ("string".equals(schemaType)) {
            type = new JavaType().setTypeName("String");
        } else if ("array".equals(schemaType)) {
            //
            // process List types
            //
            final Schema<?> items = ((ArraySchema) schema).getItems();
            final JavaType javaType = toJavaType(items, packageName);
            List<Import> requiredImports = new ArrayList<>();
            requiredImports.add(new Import().setPackageName("java.util").setTypeName("List"));
            requiredImports.addAll(javaType.getRequiredImports());
            type = new JavaType()
                    .setTypeName(format("List<{0}>", javaType.getTypeName()))
                    .setRequiredImports(requiredImports);
        } else if (schema.get$ref() != null) {
            final String[] split = schema.get$ref().split("/");
            final String referencedType = split[split.length - 1];
            type = new JavaType()
                    .setTypeName(referencedType)
                    .setRequiredImports(singletonList(new Import().setPackageName(packageName).setTypeName(referencedType)));
        } else if (schema.getAdditionalProperties() != null) {
            //
            // process Map types
            //
            final Schema additionalProperties = (Schema) schema.getAdditionalProperties();
            JavaType generic = toJavaType(additionalProperties, packageName);
            ArrayList<Import> requiredImports = new ArrayList<>();
            requiredImports.add(new Import().setPackageName("java.util").setTypeName("Map"));
            requiredImports.addAll(generic.getRequiredImports());
            type = new JavaType()
                    .setTypeName("Map<String, " + generic.getTypeName() + ">")
                    .setRequiredImports(requiredImports);

        } else if ("object".equals(schemaType)) {
            //
            // handle the case the schema is just "object" with no additional information
            //
            type = new JavaType().setTypeName("Object");
        }
        return type;
    }

}
