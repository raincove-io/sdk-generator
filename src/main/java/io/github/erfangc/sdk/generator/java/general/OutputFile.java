package io.github.erfangc.sdk.generator.java.general;

public class OutputFile {
    private String path;
    private String content;

    public String getContent() {
        return content;
    }

    public OutputFile setContent(String content) {
        this.content = content;
        return this;
    }

    public String getPath() {
        return path;
    }

    public OutputFile setPath(String path) {
        this.path = path;
        return this;
    }
}
