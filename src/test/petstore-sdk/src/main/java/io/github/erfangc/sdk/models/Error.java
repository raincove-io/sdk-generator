package io.github.erfangc.sdk.models;


public class Error {

    private Integer code;
    private String message;

    public Integer getCode() {
        return this.code;
    }

    public Error setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return this.message;
    }

    public Error setMessage(String message) {
        this.message = message;
        return this;
    }

}