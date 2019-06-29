package io.github.erfangc.sdk.generator.java.general;

public class License {
    private String id;
    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public License setName(String name) {
        this.name = name;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public License setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getId() {
        return id;
    }

    public License setId(String id) {
        this.id = id;
        return this;
    }
}
