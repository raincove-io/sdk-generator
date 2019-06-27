package io.github.erfangc.sdk.generator.java.operations;

public class RequestBody {
    private String body;
    private String type;

    public String getBody() {
        return body;
    }

    public RequestBody setBody(String body) {
        this.body = body;
        return this;
    }

    public String getType() {
        return type;
    }

    public RequestBody setType(String type) {
        this.type = type;
        return this;
    }
}
