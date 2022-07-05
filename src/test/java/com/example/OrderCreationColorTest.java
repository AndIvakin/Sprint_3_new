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

    //CourierClient courierClient;
    OrderClient orderClient;
    Orders orders;

    @Before
    public void setUp() {
        //courierClient = new CourierClient();
        orderClient = new OrderClient();
    }

    private final String[] color;


    public OrderCreationColorTest(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getColor() {
        return new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK, GREY"}},
                {new String[]{""}},
        };
    }


    @Test
    public void ChooseColorInCreateOrderTest() {
        //ValidatableResponse loginResponse = OrderClient.createOrders(color);
        ValidatableResponse loginResponse = OrderClient.createOrders (new Orders ("Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", color));
        int statusCode = loginResponse.extract().statusCode();

        assertThat("статус ответа", statusCode, equalTo(SC_CREATED));
        int trackIsCreated = loginResponse.extract().path("track");
        assertThat("Сообщение в теле", trackIsCreated, is(not(0)));
    }
}