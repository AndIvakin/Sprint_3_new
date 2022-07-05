package com.example;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends ScooterRestClient {

    private static final String ORDER_PATH = "api/v1/orders/";

    @Step("Создание заказа цвет")
    public static ValidatableResponse createOrders(Orders orders) {
        return given()
                .spec(getBaseSpec())
                .body(orders)
                //.body("{\"firstName\":\"Naruto\",\"lastName\":\"Uchiha\",\"address\":\"Konoha, 142 apt.\",\"metroStation\":4,\"phone\":\"+7 800 355 35 35\",\"rentTime\":5,\"deliveryDate\":\"2020-06-06\",\"comment\":\"Saske, come back to Konoha\",\"color\":[" + color + "]}")
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("список заказов")
    public static ValidatableResponse listOrders() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH + list)
                .then();
    }
}
