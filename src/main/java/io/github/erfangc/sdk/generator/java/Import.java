package io.github.erfangc.sdk.generator.java;

import java.util.Objects;

public class Import implements Comparable<Import> {
    private String packageName = "";
    private String typeName;

    public String getPackageName() {
        return packageName;
    }

    public Import setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public String getTypeName() {
        return typeName;
    }

    public Import setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Import anImport = (Import) o;
        return getPackageName().equals(anImport.getPackageName()) &&
                getTypeName().equals(anImport.getTypeName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPackageName(), getTypeName());
    }

    @Override
    public int compareTo(Import o) {
        if (packageName.equals(o.packageName)) {
            return packageName.compareTo(o.packageName);
        } else {
            return typeName.compareTo(o.typeName);
        }
    }
}
