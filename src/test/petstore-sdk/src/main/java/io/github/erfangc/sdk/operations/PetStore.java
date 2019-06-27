package io.github.erfangc.sdk.operations;

import feign.Param;
import feign.RequestLine;
import io.github.erfangc.sdk.models.Pet;
import io.github.erfangc.sdk.models.Pets;

public interface PetStore {

    @RequestLine("GET /pets?limit={limit}")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Pets listPets(@Param("limit") Integer limit);

    @RequestLine("POST /pets?nosave={nosave}&test={test}")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    void createPets(Pet body, @Param("nosave") String nosave, @Param("test") Boolean test);

    @RequestLine("GET /pets/{petId}")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Pets showPetById(@Param("petId") String petId);

}