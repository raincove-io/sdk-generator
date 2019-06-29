package io.github.erfangc.sdk.apis;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import io.github.erfangc.sdk.operations.PetStore;

public class CompanySdk {

    private static String endpoint = "http://localhost:8080";

    public static void setEndpoint(String endpoint) {
        CompanySdk.endpoint = endpoint;
    }

    public static PetStore petStore(String accessToken) {
        return Feign
                .builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .requestInterceptor(new AccessTokenInterceptor(accessToken))
                .target(PetStore.class, CompanySdk.endpoint + "/petstore/api/v1");
    }

    public static PetStore petStore() {
        return Feign
                .builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .requestInterceptor(new AccessTokenInterceptor())
                .target(PetStore.class, CompanySdk.endpoint + "/petstore/api/v1");
    }

}