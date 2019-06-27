package io.github.erfangc.sdk.generator.java.operations;

public class Param {
    private String name;
    private String type;
    private boolean isPathVariable;
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
}
