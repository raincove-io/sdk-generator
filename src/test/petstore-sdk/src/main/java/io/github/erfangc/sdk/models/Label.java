package io.github.erfangc.sdk.models;


public class Label {

    private String name;
    private String value;
    private LabelType labelType;

    public String getName() {
        return this.name;
    }

    public Label setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return this.value;
    }

    public Label setValue(String value) {
        this.value = value;
        return this;
    }

    public LabelType getLabelType() {
        return this.labelType;
    }

    public Label setLabelType(LabelType labelType) {
        this.labelType = labelType;
        return this;
    }

}