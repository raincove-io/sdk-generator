package io.github.erfangc.sdk.models;

import java.util.List;

public class Owner {

    private Long id;
    private List<OwnerAddress> addresses;
    private List<Pet> pets;

    public Long getId() {
        return this.id;
    }

    public Owner setId(Long id) {
        this.id = id;
        return this;
    }

    public List<OwnerAddress> getAddresses() {
        return this.addresses;
    }

    public Owner setAddresses(List<OwnerAddress> addresses) {
        this.addresses = addresses;
        return this;
    }

    public List<Pet> getPets() {
        return this.pets;
    }

    public Owner setPets(List<Pet> pets) {
        this.pets = pets;
        return this;
    }

}