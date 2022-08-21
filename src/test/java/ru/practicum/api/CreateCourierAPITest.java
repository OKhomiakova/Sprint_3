package ru.practicum.api;

import POJO.CourierForSignIn;
import POJO.CourierForSignUp;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import ru.practicum.api.steps.CourierTestSteps;
import io.restassured.response.Response;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;

public class CreateCourierAPITest {

    private CourierForSignUp courier;

    @Before
    public void generateDataForNewCourier() {
        String login = RandomStringUtils.randomAlphanumeric(5);
        String password = RandomStringUtils.randomAlphanumeric(5);
        String firstName = RandomStringUtils.randomAlphanumeric(5);

        this.courier = new CourierForSignUp(login, password, firstName);
    }

    @Test
    @DisplayName("Создание нового курьера с полными, корректыми данными")
    public void createNewCourierWithCorrectAndSufficientData() {
        Response createResponse = CourierTestSteps.createNewCourier(this.courier);
        CourierTestSteps.compareSuccessResponseStringAndCode(createResponse, "ok", 201);
        // логинимся за этого пользователя
        CourierForSignIn courierForSignIn = new CourierForSignIn(this.courier.getLogin(), this.courier.getPassword());
        Response logInResponse = CourierTestSteps.loginCourier(courierForSignIn);
        // получаем id курьера
        String courierId = CourierTestSteps.getCourierId(logInResponse);
        // удаляем курьера по id
        Response deleteResponse = CourierTestSteps.deleteCourier(courierId);
        CourierTestSteps.compareSuccessResponseStringAndCode(deleteResponse, "ok", 200);
    }

    @Test
    @DisplayName("Создание двух одинаковых курьеров")
    public void createNewCourierWithDuplicateData() {
        Response response = CourierTestSteps.createNewCourier(this.courier);
        CourierTestSteps.compareSuccessResponseStringAndCode(response, "ok", 201);
        Response secondResponse = CourierTestSteps.createNewCourier(this.courier);
        CourierTestSteps.compareErrorResponseStringAndCode(secondResponse, 409, "Этот логин уже используется. Попробуйте другой.");
        CourierForSignIn courierForSignIn = new CourierForSignIn(this.courier.getLogin(), this.courier.getPassword());
        Response logInResponse = CourierTestSteps.loginCourier(courierForSignIn);
        // получаем id курьера
        String courierId = CourierTestSteps.getCourierId(logInResponse);
        // удаляем курьера по id
        Response deleteResponse = CourierTestSteps.deleteCourier(courierId);
        CourierTestSteps.compareSuccessResponseStringAndCode(deleteResponse, "ok", 200);
    }

    @Test
    @DisplayName("Создание курьера с пустыми логином, паролем и именем")
    public void createNewCourierWithAllEmptyFields() {
        CourierForSignUp courier = new CourierForSignUp("", "", "");
        Response response = CourierTestSteps.createNewCourier(courier);
        CourierTestSteps.compareErrorResponseStringAndCode(response, 400, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Создание курьера с пустым логином")
    public void createNewCourierWithEmptyLogin() {
        CourierForSignUp courier = new CourierForSignUp("", RandomStringUtils.randomAlphanumeric(5), RandomStringUtils.randomAlphanumeric(5));
        Response response = CourierTestSteps.createNewCourier(courier);
        CourierTestSteps.compareErrorResponseStringAndCode(response, 400, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Создание курьера без ввода логином")
    public void createNewCourierWithoutLogin() {
        CourierForSignUp courier = new CourierForSignUp(null, RandomStringUtils.randomAlphanumeric(5), RandomStringUtils.randomAlphanumeric(5));
        Response response = CourierTestSteps.createNewCourier(courier);
        CourierTestSteps.compareErrorResponseStringAndCode(response, 400, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Создание курьера с пустым паролем")
    public void createNewCourierWithEmptyPassword() {
        CourierForSignUp courier = new CourierForSignUp(RandomStringUtils.randomAlphanumeric(5), "", RandomStringUtils.randomAlphanumeric(5));
        Response response = CourierTestSteps.createNewCourier(courier);
        CourierTestSteps.compareErrorResponseStringAndCode(response, 400, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Создание курьера без ввода пароля")
    public void createNewCourierWithoutPassword() {
        CourierForSignUp courier = new CourierForSignUp(RandomStringUtils.randomAlphanumeric(5), null, RandomStringUtils.randomAlphanumeric(5));
        Response response = CourierTestSteps.createNewCourier(courier);
        CourierTestSteps.compareErrorResponseStringAndCode(response, 400, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Создание курьера с пустым именем")
    public void createNewCourierWithEmptyFirstName() {
        String login = RandomStringUtils.randomAlphanumeric(5);
        String password = RandomStringUtils.randomAlphanumeric(5);

        CourierForSignUp courier = new CourierForSignUp(login, password, null);
        Response response = CourierTestSteps.createNewCourier(courier);
        CourierTestSteps.compareSuccessResponseStringAndCode(response, "ok", 201);
        CourierForSignIn courierForSignIn = new CourierForSignIn(login, password);
        Response logInResponse = CourierTestSteps.loginCourier(courierForSignIn);
        // получаем id курьера
        String courierId = CourierTestSteps.getCourierId(logInResponse);
        // удаляем курьера по id
        Response deleteResponse = CourierTestSteps.deleteCourier(courierId);
        CourierTestSteps.compareSuccessResponseStringAndCode(deleteResponse, "ok", 200);
    }

    @Test
    @DisplayName("Создание курьера без ввода имени")
    public void createNewCourierWithoutFirstName() {
        String login = RandomStringUtils.randomAlphanumeric(5);
        String password = RandomStringUtils.randomAlphanumeric(5);

        CourierForSignUp courier = new CourierForSignUp(login, password, "");
        Response response = CourierTestSteps.createNewCourier(courier);
        CourierTestSteps.compareSuccessResponseStringAndCode(response, "ok", 201);
        CourierForSignIn courierForSignIn = new CourierForSignIn(login, password);
        Response logInResponse = CourierTestSteps.loginCourier(courierForSignIn);
        // получаем id курьера
        String courierId = CourierTestSteps.getCourierId(logInResponse);
        // удаляем курьера по id
        Response deleteResponse = CourierTestSteps.deleteCourier(courierId);
        CourierTestSteps.compareSuccessResponseStringAndCode(deleteResponse, "ok", 200);
        }

    // Тест падает т.к. в документации ожидаемый мессадж "Этот логин уже используется",
    // но возвращается "Этот логин уже используется. Попробуйте другой."
    @Test
    @DisplayName("Создание курьера с повторяющимся логином")
    public void createNewCourierWithTakenLogin() {
        CourierForSignUp courier = new CourierForSignUp("ninja", RandomStringUtils.randomAlphanumeric(5), RandomStringUtils.randomAlphanumeric(5));
        Response response = CourierTestSteps.createNewCourier(courier);
        CourierTestSteps.compareErrorResponseStringAndCode(response, 409, "Этот логин уже используется");
    }
}
