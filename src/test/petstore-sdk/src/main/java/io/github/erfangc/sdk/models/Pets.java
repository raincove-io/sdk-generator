package io.github.erfangc.sdk.models;

import java.util.List;

public class Pets {

    private List<Pet> pets;

    public List<Pet> getPets() {
        return this.pets;
    }

    public Pets setPets(List<Pet> pets) {
        this.pets = pets;
        return this;
    }

}