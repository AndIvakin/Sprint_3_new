package com.example;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class CourierClient extends ScooterRestClient {

    private static final String COURIER_PATH = "/api/v1/courier/";

    @Step("логирование курьера {credentials}")
    public ValidatableResponse login(CourierCredentials credentials) {
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(COURIER_PATH + "login")
                .then();
    }

    @Step("создание курьера {courier}")
    public ValidatableResponse createCourier(Courier courier) {
        return given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then();
    }

    @Step("удалять курьера, если он был создан в тесте {courierId}")
    public ValidatableResponse deleteCourier(int courierId) {
        return given()
                .spec(getBaseSpec())
                .body("{\"id\":\"" + courierId + "\"}")
                .when()
                .delete(COURIER_PATH + courierId)
                .then();
    }
}