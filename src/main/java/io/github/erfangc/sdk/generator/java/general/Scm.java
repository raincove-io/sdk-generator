package io.github.erfangc.sdk.generator.java.general;

public class Scm {
    private String connection;
    private String developerConnection;
    private String url;

    public String getConnection() {
        return connection;
    }

    public Scm setConnection(String connection) {
        this.connection = connection;
        return this;
    }

    public String getDeveloperConnection() {
        return developerConnection;
    }

    public Scm setDeveloperConnection(String developerConnection) {
        this.developerConnection = developerConnection;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Scm setUrl(String url) {
        this.url = url;
        return this;
    }
}
