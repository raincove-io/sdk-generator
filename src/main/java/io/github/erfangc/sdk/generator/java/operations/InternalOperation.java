package io.github.erfangc.sdk.generator.java.operations;

import io.github.erfangc.sdk.generator.java.Import;
import io.github.erfangc.sdk.generator.java.JavaType;

import java.util.List;

public class InternalOperation {
    private String name;
    private List<Param> params;
    private List<Header> headers;
    private JavaType requestBody;
    private String requestLine;
    private JavaType responseType;
    private List<Import> requiredImports;

    public String getName() {
        return name;
    }

    public InternalOperation setName(String name) {
        this.name = name;
        return this;
    }

    public List<Param> getParams() {
        return params;
    }

    public InternalOperation setParams(List<Param> params) {
        this.params = params;
        return this;
    }

    public JavaType getRequestBody() {
        return requestBody;
    }

    public InternalOperation setRequestBody(JavaType requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    public String getRequestLine() {
        return requestLine;
    }

    public InternalOperation setRequestLine(String requestLine) {
        this.requestLine = requestLine;
        return this;
    }

    public JavaType getResponseType() {
        return responseType;
    }

    public InternalOperation setResponseType(JavaType responseType) {
        this.responseType = responseType;
        return this;
    }

    public List<Import> getRequiredImports() {
        return requiredImports;
    }

    public InternalOperation setRequiredImports(List<Import> requiredImports) {
        this.requiredImports = requiredImports;
        return this;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public InternalOperation setHeaders(List<Header> headers) {
        this.headers = headers;
        return this;
    }
}
