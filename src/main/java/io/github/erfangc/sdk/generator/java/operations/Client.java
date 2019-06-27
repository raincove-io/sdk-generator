package io.github.erfangc.sdk.generator.java.operations;

public class Client {
    private String packageName;
    private String javaCode;
    private Context context;
    private String clientType;
    private String clientName;
    private String baseUrl;

    public String getClientType() {
        return clientType;
    }

    public Client setClientType(String clientType) {
        this.clientType = clientType;
        return this;
    }

    public String getClientName() {
        return clientName;
    }

    public Client setClientName(String clientName) {
        this.clientName = clientName;
        return this;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public Client setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public String getJavaCode() {
        return javaCode;
    }

    public Client setJavaCode(String javaCode) {
        this.javaCode = javaCode;
        return this;
    }

    public Context getContext() {
        return context;
    }

    public Client setContext(Context context) {
        this.context = context;
        return this;
    }

    public String getPackageName() {
        return packageName;
    }

    public Client setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }
}
