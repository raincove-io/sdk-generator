package io.github.erfangc.sdk.apis;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import io.github.erfangc.sdk.operations.PetStore;

public class CompanySdk {

    private static String baseUrl;

    public static void setBaseUrl(String baseUrl) {
        CompanySdk.baseUrl = baseUrl;
    }

    public static PetStore petStore(String accessToken) {
        return Feign
                .builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .requestInterceptor(new AccessTokenInterceptor(accessToken))
                .target(PetStore.class, CompanySdk.baseUrl != null ? CompanySdk.baseUrl : "https://erfang.io/petstore/api/v1");
    }

    public static PetStore petStore() {
        return Feign
                .builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .requestInterceptor(new AccessTokenInterceptor())
                .target(PetStore.class, CompanySdk.baseUrl != null ? CompanySdk.baseUrl : "https://erfang.io/petstore/api/v1");

    }

}