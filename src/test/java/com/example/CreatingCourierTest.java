package com.example;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CreatingCourierTest {

    CourierClient courierClient;
    Courier courier;
    int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = CourierGenerator.getRandom();
    }

    @After
    @DisplayName("удаление курьера")
    public void tearDown() {
        courierClient.delete(courierId);
    }

    @Test
    @DisplayName("создание курьера")
    public void createCourierWithoutFirstsName() {
        ValidatableResponse loginResponse = courierClient.create(new Courier(courier.getLogin(), courier.getPassword(), courier.getFirstName()));
        int statusCode = loginResponse.extract().statusCode();
        boolean createdStatus = loginResponse.extract().body().path("ok");

        ValidatableResponse loginResponseForDelete = courierClient.login(new CourierCredentials(courier.getLogin(), courier.getPassword()));
        courierId = loginResponseForDelete.extract().path("id");

        assertThat("статус ответа", statusCode, equalTo(SC_CREATED));
        assertThat("Сообщение в теле", createdStatus, equalTo(true));
    }

    @Test
    @DisplayName("создание курьера без логина")
    public void creatingCourierWithoutLogin() {
        ValidatableResponse loginResponse = courierClient.create(new Courier(null, courier.getPassword(), courier.getFirstName()));
        int statusCode = loginResponse.extract().statusCode();

        assertThat("статус ответа", statusCode, equalTo(SC_BAD_REQUEST));

        String errorMessage = loginResponse.extract().body().path("message");

        assertThat("Сообщение в теле", errorMessage, equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("создание курьера без пароля")
    public void creatingCourierWithoutPassword() {
        ValidatableResponse loginResponse = courierClient.create(new Courier(courier.getLogin(), null, courier.getFirstName()));
        int statusCode = loginResponse.extract().statusCode();

        assertThat("статус ответа", statusCode, equalTo(SC_BAD_REQUEST));

        String errorMessage = loginResponse.extract().body().path("message");

        assertThat("Сообщение в теле", errorMessage, equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("создание курьера без имени") //баг в документации уточнить и переделать
    public void creatingCourierWithoutFirstName() {
        ValidatableResponse loginResponse = courierClient.create(new Courier(courier.getLogin(), courier.getPassword(), null));

        int statusCode = loginResponse.extract().statusCode();
        String errorMessage = loginResponse.extract().body().path("message");

        assertThat("статус ответа", statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("Сообщение в теле", errorMessage, equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("создать двух одинаковых курьеров")
    public void createTwoIdenticalCouriers() {
        ValidatableResponse oneLoginResponse = courierClient.create(new Courier(courier.getLogin(), courier.getPassword(), courier.getFirstName()));
        ValidatableResponse loginResponse = courierClient.create(new Courier(courier.getLogin(), courier.getPassword(), courier.getFirstName()));

        int statusCode = loginResponse.extract().statusCode();
        assertThat("статус ответа", statusCode, equalTo(SC_CONFLICT));
        String errorMessage = loginResponse.extract().body().path("message");

        assertThat("Сообщение в теле", errorMessage, equalTo("Этот логин уже используется"));
    }

    @Test
    @DisplayName("создать пользователя с логином, который уже есть")
    public void loginThatAlreadyExists() {
        ValidatableResponse oneLoginResponse = courierClient.create(new Courier(courier.getLogin(), courier.getPassword(), courier.getFirstName()));
        String login = courier.getLogin();
        courier = CourierGenerator.getRandom();
        courier.setLogin(login);
        ValidatableResponse loginResponse = courierClient.create(new Courier(courier.getLogin(), courier.getPassword(), courier.getFirstName()));

        int statusCode = loginResponse.extract().statusCode();
        String errorMessage = loginResponse.extract().body().path("message");

        assertThat("статус ответа", statusCode, equalTo(SC_CONFLICT));
        assertThat("Сообщение в теле", errorMessage, equalTo("Этот логин уже используется"));
    }

}
