package io.github.erfangc.sdk.models;


public class OwnerAddress {

    private String line1;
    private Integer zipcode;
    private String city;

    public String getLine1() {
        return this.line1;
    }

    public OwnerAddress setLine1(String line1) {
        this.line1 = line1;
        return this;
    }

    public Integer getZipcode() {
        return this.zipcode;
    }

    public OwnerAddress setZipcode(Integer zipcode) {
        this.zipcode = zipcode;
        return this;
    }

    public String getCity() {
        return this.city;
    }

    public OwnerAddress setCity(String city) {
        this.city = city;
        return this;
    }

}