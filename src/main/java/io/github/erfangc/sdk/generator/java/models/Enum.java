package io.github.erfangc.sdk.generator.java.models;

public class Enum {
    private String value;
    private boolean last;

    public String getValue() {
        return value;
    }

    public Enum setValue(String value) {
        this.value = value;
        return this;
    }

    public boolean isLast() {
        return last;
    }

    public Enum setLast(boolean last) {
        this.last = last;
        return this;
    }
}
