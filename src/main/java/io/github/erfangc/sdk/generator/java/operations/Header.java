package io.github.erfangc.sdk.generator.java.operations;

public class Header {
    private String name;
    private String rawName;

    public String getName() {
        return name;
    }

    public Header setName(String name) {
        this.name = name;
        return this;
    }

    public String getRawName() {
        return rawName;
    }

    public Header setRawName(String rawName) {
        this.rawName = rawName;
        return this;
    }
}
