package io.github.erfangc.sdk.generator.java.models;

public class Model {
    private String id;
    private Context context;
    private String javaCode;

    public String getId() {
        return id;
    }

    public Model setId(String id) {
        this.id = id;
        return this;
    }

    public Context getContext() {
        return context;
    }

    public Model setContext(Context context) {
        this.context = context;
        return this;
    }

    public String getJavaCode() {
        return javaCode;
    }

    public Model setJavaCode(String javaCode) {
        this.javaCode = javaCode;
        return this;
    }
}
