package io.github.erfangc.sdk.generator.java.operations;

import io.github.erfangc.sdk.generator.java.Import;

import java.util.List;
import java.util.Set;

public class Context {
    private String packageName;
    private Set<Import> imports;
    private String clientName;
    private List<InternalOperation> operations;

    public Set<Import> getImports() {
        return imports;
    }

    public Context setImports(Set<Import> imports) {
        this.imports = imports;
        return this;
    }

    public String getClientName() {
        return clientName;
    }

    public Context setClientName(String clientName) {
        this.clientName = clientName;
        return this;
    }

    public List<InternalOperation> getOperations() {
        return operations;
    }

    public Context setOperations(List<InternalOperation> internalOperations) {
        this.operations = internalOperations;
        return this;
    }

    public String getPackageName() {
        return packageName;
    }

    public Context setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }
}
