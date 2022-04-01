package com.example;

import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)

public class OrderCreationColorTest {

    CourierClient courierClient;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    private final String color;


    public OrderCreationColorTest(String color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static List<String> getColor() {
        return List.of("\"BLACK\"",
                "\"GREY\"",
                "\"BLACK, GREY\"",
                "");
    }

    @Test
    public void ChooseColorInCreateOrderTest() {
        ValidatableResponse loginResponse = courierClient.CreateOrders(color);
        int statusCode = loginResponse.extract().statusCode();
        int trackIsCreated = loginResponse.extract().path("track");

        assertThat("статус ответа", statusCode, equalTo(SC_CREATED));
        assertThat("Сообщение в теле", trackIsCreated, is(not(0)));
    }

}