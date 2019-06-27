package io.github.erfangc.sdk.generator.java.models;

import io.github.erfangc.sdk.generator.java.Import;
import io.github.erfangc.sdk.generator.java.JavaType;
import io.swagger.v3.oas.models.media.Schema;

import java.util.List;

public class Context {
    private List<Enum> enums;
    private JavaType javaType;
    private Schema schema;
    private String packageName;
    private List<Import> imports;
    private List<Property> properties;

    public JavaType getJavaType() {
        return javaType;
    }

    public Context setJavaType(JavaType javaType) {
        this.javaType = javaType;
        return this;
    }

    public Schema getSchema() {
        return schema;
    }

    public Context setSchema(Schema schema) {
        this.schema = schema;
        return this;
    }

    public String getPackageName() {
        return packageName;
    }

    public Context setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public List<Import> getImports() {
        return imports;
    }

    public Context setImports(List<Import> imports) {
        this.imports = imports;
        return this;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public Context setProperties(List<Property> properties) {
        this.properties = properties;
        return this;
    }

    public List<Enum> getEnums() {
        return enums;
    }

    public Context setEnums(List<Enum> enums) {
        this.enums = enums;
        return this;
    }
}
