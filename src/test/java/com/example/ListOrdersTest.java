package com.example;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ListOrdersTest {

    CourierClient courierClient;

    @Test
    @DisplayName("тело ответа возвращается список заказов")
    public void theResponseReturnsListOrders() {
        courierClient = new CourierClient();
        ValidatableResponse listOrders = OrderClient.listOrders();
        int statusCode = listOrders.extract().statusCode();
        assertThat("статус ответа", statusCode, equalTo(SC_OK));
        String courierId = listOrders.extract().path("id");
        assertThat("Сообщение в теле", courierId, is(not(0)));
    }
}
