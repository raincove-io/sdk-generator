package io.github.erfangc.sdk.generator.java.models;

import io.github.erfangc.sdk.generator.java.Import;

import java.util.List;

public class Property {
    private List<Import> imports;
    private String type;
    private String name;
    private String getterName;
    private String setterName;

    public String getType() {
        return type;
    }

    public Property setType(String type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return name;
    }

    public Property setName(String name) {
        this.name = name;
        return this;
    }

    public String getGetterName() {
        return getterName;
    }

    public Property setGetterName(String getterName) {
        this.getterName = getterName;
        return this;
    }

    public String getSetterName() {
        return setterName;
    }

    public Property setSetterName(String setterName) {
        this.setterName = setterName;
        return this;
    }

    public List<Import> getImports() {
        return imports;
    }

    public Property setImports(List<Import> imports) {
        this.imports = imports;
        return this;
    }
}
