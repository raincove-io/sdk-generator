package io.github.erfangc.sdk.models;

import java.util.List;
import java.util.Map;

public class Pet {

    private Integer id;
    private List<String> nicknames;
    private List<Map<String, Label>> complex;
    private String name;
    private String tag;
    private Map<String, Label> labels;
    private Map<String, String> metadata;
    private Owner owner;

    public Integer getId() {
        return this.id;
    }

    public Pet setId(Integer id) {
        this.id = id;
        return this;
    }

    public List<String> getNicknames() {
        return this.nicknames;
    }

    public Pet setNicknames(List<String> nicknames) {
        this.nicknames = nicknames;
        return this;
    }

    public List<Map<String, Label>> getComplex() {
        return this.complex;
    }

    public Pet setComplex(List<Map<String, Label>> complex) {
        this.complex = complex;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Pet setName(String name) {
        this.name = name;
        return this;
    }

    public String getTag() {
        return this.tag;
    }

    public Pet setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public Map<String, Label> getLabels() {
        return this.labels;
    }

    public Pet setLabels(Map<String, Label> labels) {
        this.labels = labels;
        return this;
    }

    public Map<String, String> getMetadata() {
        return this.metadata;
    }

    public Pet setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
        return this;
    }

    public Owner getOwner() {
        return this.owner;
    }

    public Pet setOwner(Owner owner) {
        this.owner = owner;
        return this;
    }

}