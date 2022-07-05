package com.example;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static io.restassured.http.ContentType.JSON;

public class ScooterRestClient {

    private static final String BASE_URL = "http://qa-scooter.praktikum-services.ru/";

    protected static RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder()
                .setContentType(JSON)
                .setBaseUri(BASE_URL)
                .build();
    }

    static String list = "?limit=10&page=0";

}