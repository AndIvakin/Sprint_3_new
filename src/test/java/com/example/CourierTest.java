package com.example;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CourierTest {

    CourierClient courierClient;
    Courier courier;
    int courierId;

    @Before
    @DisplayName("создание курьера")
    public void setUp() {
        courierClient = new CourierClient();
        courier = CourierGenerator.getRandom();
        courierClient.createCourier(courier);
    }

    @After
    @DisplayName("удаление курьера")
    public void tearDown() {
        courierClient.deleteCourier(courierId);
    }

    @Test
    @DisplayName("логирование курьера")
    public void courierCanLoginWithValidCredentials() {
        ValidatableResponse loginResponse = courierClient.login(new CourierCredentials(courier.getLogin(), courier.getPassword()));
        int statusCode = loginResponse.extract().statusCode();
        courierId = loginResponse.extract().path("id");

        assertThat("Courier cannot login", statusCode, equalTo(SC_OK));
        assertThat("Courier ID is incorrect", courierId, is(not(0)));
    }

    @Test
    @DisplayName("логирование курьера с невалидным паролем")
    public void courierMayPasswordWithInvalidCredentials() {
        ValidatableResponse loginResponse = courierClient.login(new CourierCredentials(courier.getLogin(), "test"));
        int statusCode = loginResponse.extract().statusCode();
        String errorMessage = loginResponse.extract().body().path("message");

        assertThat("Courier cannot login", statusCode, equalTo(SC_NOT_FOUND));
        assertThat("Unknown error message", errorMessage, equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("логирование курьера с невалидным логином")
    public void courierMayLoginWithInvalidCredentials() {
        ValidatableResponse loginResponse = courierClient.login(new CourierCredentials("test", courier.getPassword()));
        int statusCode = loginResponse.extract().statusCode();
        String errorMessage = loginResponse.extract().body().path("message");

        assertThat("Courier cannot login", statusCode, equalTo(SC_NOT_FOUND));
        assertThat("Unknown error message ", errorMessage, equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("логирование курьера с невалидным логином и паролем")
    public void courierWithInvalidPasswordLogin() {
        ValidatableResponse loginResponse = courierClient.login(new CourierCredentials("test", "test"));
        int statusCode = loginResponse.extract().statusCode();
        String errorMessage = loginResponse.extract().body().path("message");

        assertThat("Courier cannot login", statusCode, equalTo(SC_NOT_FOUND));
        assertThat("Unknown error message ", errorMessage, equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("логирование курьера без логина")
    public void courierWithInvalidLogin() {
        ValidatableResponse loginResponse = courierClient.login(new CourierCredentials(null, courier.getPassword()));
        int statusCode = loginResponse.extract().statusCode();
        String errorMessage = loginResponse.extract().body().path("message");

        assertThat("Courier cannot login", statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("Unknown error message ", errorMessage, equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("логирование курьера без пароля")
    public void courierWithInvalidPassword() {
        ValidatableResponse loginResponse = courierClient.login(new CourierCredentials(courier.getLogin(), null));
        int statusCode = loginResponse.extract().statusCode();

        assertThat("Courier cannot login", statusCode, equalTo(SC_BAD_REQUEST));

        String errorMessage = loginResponse.extract().body().path("message");

        assertThat("Unknown error message ", errorMessage, equalTo("Недостаточно данных для входа"));
    }
}
