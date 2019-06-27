package io.github.erfangc.sdk.generator.java;

import java.util.Collections;
import java.util.List;

public class JavaType {
    public static JavaType Void = new JavaType().setTypeName("void");

    private List<Import> requiredImports = Collections.emptyList();
    private String typeName;

    public String getTypeName() {
        return typeName;
    }

    public JavaType setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public List<Import> getRequiredImports() {
        return requiredImports;
    }

    public JavaType setRequiredImports(List<Import> requiredImports) {
        this.requiredImports = requiredImports;
        return this;
    }
}
