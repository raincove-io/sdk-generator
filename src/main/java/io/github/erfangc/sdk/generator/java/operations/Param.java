package io.github.erfangc.sdk.generator.java.operations;

public class Param {
    private String name;
    private String origName;
    private String type;
    private boolean isPathVariable;
    private boolean isHeaderVariable;
    private boolean last;

    public String getName() {
        return name;
    }

    public Param setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public Param setType(String type) {
        this.type = type;
        return this;
    }

    public boolean isLast() {
        return last;
    }

    public Param setLast(boolean last) {
        this.last = last;
        return this;
    }

    public boolean isPathVariable() {
        return isPathVariable;
    }

    public Param setPathVariable(boolean pathVariable) {
        isPathVariable = pathVariable;
        return this;
    }

    public boolean isHeaderVariable() {
        return isHeaderVariable;
    }

    public Param setHeaderVariable(boolean headerVariable) {
        isHeaderVariable = headerVariable;
        return this;
    }

    public String getOrigName() {
        return origName;
    }

    public Param setOrigName(String origName) {
        this.origName = origName;
        return this;
    }
}
