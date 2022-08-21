package ru.practicum.api;

import POJO.CourierForSignIn;
import POJO.CourierForSignUp;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import ru.practicum.api.steps.CourierTestSteps;

public class LoginCourierAPITest {

    private CourierForSignUp courier;

    @Before
    public void createCourier() {
        String login = RandomStringUtils.randomAlphanumeric(5);
        String password = RandomStringUtils.randomAlphanumeric(5);
        String firstName = RandomStringUtils.randomAlphanumeric(5);

        this.courier = new CourierForSignUp(login, password, firstName);
    }

    @Test
    @DisplayName("Логин с корректным логином и паролем")
    public void logInWithExistingCourier() {
        Response createResponse = CourierTestSteps.createNewCourier(courier);
        // логинимся за этого пользователя
        CourierForSignIn courierForSignIn = new CourierForSignIn(this.courier.getLogin(), this.courier.getPassword());
        Response logInResponse = CourierTestSteps.loginCourier(courierForSignIn);
        // проверяем, что залогинился успешно
        CourierTestSteps.compareSuccessLoginResponseStringAndCode(logInResponse, 200);
        // получаем id курьера
        String courierId = CourierTestSteps.getCourierId(logInResponse);
        // удаляем курьера по id
        Response deleteResponse = CourierTestSteps.deleteCourier(courierId);
        CourierTestSteps.compareSuccessResponseStringAndCode(deleteResponse, "ok", 200);
    }

    @Test
    @DisplayName("Логин с пустым логином")
    public void logInWithEmptyLogin() {
        // создаем курьера
        CourierTestSteps.createNewCourier(courier);
        // без пароля логинимся за этого пользователя
        CourierForSignIn courierForSignIn = new CourierForSignIn("", this.courier.getLogin());
        Response logInResponse = CourierTestSteps.loginCourier(courierForSignIn);
        // проверяем, что залогинился неуспешно
        CourierTestSteps.compareErrorResponseStringAndCode(logInResponse, 400, "Недостаточно данных для входа");
        CourierForSignIn courierForSignIn2 = new CourierForSignIn(this.courier.getLogin(), this.courier.getPassword());
        Response logInResponse2 = CourierTestSteps.loginCourier(courierForSignIn2);
        // получаем id курьера
        String courierId = CourierTestSteps.getCourierId(logInResponse2);
        // удаляем курьера по id
        Response deleteResponse = CourierTestSteps.deleteCourier(courierId);
        CourierTestSteps.compareSuccessResponseStringAndCode(deleteResponse, "ok", 200);
    }

    @Test
    @DisplayName("Логин без ввода логина")
    public void logInWithoutLogin() {
        // создаем курьера
        CourierTestSteps.createNewCourier(courier);
        // без пароля логинимся за этого пользователя
        CourierForSignIn courierForSignIn = new CourierForSignIn(null, this.courier.getLogin());
        Response logInResponse = CourierTestSteps.loginCourier(courierForSignIn);
        // проверяем, что залогинился неуспешно
        CourierTestSteps.compareErrorResponseStringAndCode(logInResponse, 400, "Недостаточно данных для входа");
        CourierForSignIn courierForSignIn2 = new CourierForSignIn(this.courier.getLogin(), this.courier.getPassword());
        Response logInResponse2 = CourierTestSteps.loginCourier(courierForSignIn2);
        // получаем id курьера
        String courierId = CourierTestSteps.getCourierId(logInResponse2);
        // удаляем курьера по id
        Response deleteResponse = CourierTestSteps.deleteCourier(courierId);
        CourierTestSteps.compareSuccessResponseStringAndCode(deleteResponse, "ok", 200);
    }

    @Test
    @DisplayName("Логин c пустым паролем")
    public void logInWithEmptyPassword() {
        // создаем курьера
        CourierTestSteps.createNewCourier(courier);
        // без пароля логинимся за этого пользователя
        CourierForSignIn courierForSignIn = new CourierForSignIn(this.courier.getLogin(), "");
        Response logInResponse = CourierTestSteps.loginCourier(courierForSignIn);
        // проверяем, что залогинился неуспешно
        CourierTestSteps.compareErrorResponseStringAndCode(logInResponse, 400, "Недостаточно данных для входа");
        CourierForSignIn courierForSignIn2 = new CourierForSignIn(this.courier.getLogin(), this.courier.getPassword());
        Response logInResponse2 = CourierTestSteps.loginCourier(courierForSignIn2);
        // получаем id курьера
        String courierId = CourierTestSteps.getCourierId(logInResponse2);
        // удаляем курьера по id
        Response deleteResponse = CourierTestSteps.deleteCourier(courierId);
        CourierTestSteps.compareSuccessResponseStringAndCode(deleteResponse, "ok", 200);
    }

    // Этот тест падает, поскольку сервер зависает
    @Test
    @DisplayName("Логин без ввода пароля")
    public void logInWithoutPassword() {
        // создаем курьера
        CourierTestSteps.createNewCourier(courier);
        // без пароля логинимся за этого пользователя
        CourierForSignIn courierForSignIn = new CourierForSignIn(this.courier.getLogin(), null);
        Response logInResponse = CourierTestSteps.loginCourier(courierForSignIn);
        // проверяем, что залогинился неуспешно
        CourierTestSteps.compareErrorResponseStringAndCode(logInResponse, 400, "Недостаточно данных для входа");
        CourierForSignIn courierForSignIn2 = new CourierForSignIn(this.courier.getLogin(), this.courier.getPassword());
        Response logInResponse2 = CourierTestSteps.loginCourier(courierForSignIn2);
        // получаем id курьера
        String courierId = CourierTestSteps.getCourierId(logInResponse2);
        // удаляем курьера по id
        Response deleteResponse = CourierTestSteps.deleteCourier(courierId);
        CourierTestSteps.compareSuccessResponseStringAndCode(deleteResponse, "ok", 200);
    }

    @Test
    @DisplayName("Логин с неправильным паролем")
    public void logInWithIncorrectPassword() {
        // создаем курьера
        CourierTestSteps.createNewCourier(courier);
        // без пароля логинимся за этого пользователя
        CourierForSignIn courierForSignIn = new CourierForSignIn(this.courier.getLogin(), "incorrect_password");
        Response logInResponse = CourierTestSteps.loginCourier(courierForSignIn);
        // проверяем, что залогинился неуспешно
        CourierTestSteps.compareErrorResponseStringAndCode(logInResponse, 404, "Учетная запись не найдена");
        CourierForSignIn courierForSignIn2 = new CourierForSignIn(this.courier.getLogin(), this.courier.getPassword());
        Response logInResponse2 = CourierTestSteps.loginCourier(courierForSignIn2);
        // получаем id курьера
        String courierId = CourierTestSteps.getCourierId(logInResponse2);
        // удаляем курьера по id
        Response deleteResponse = CourierTestSteps.deleteCourier(courierId);
        CourierTestSteps.compareSuccessResponseStringAndCode(deleteResponse, "ok", 200);
    }

    @Test
    @DisplayName("Log in with incorrect password")
    public void logInWithIncorrectLogin() {
        // создаем курьера
        CourierTestSteps.createNewCourier(courier);
        // без пароля логинимся за этого пользователя
        CourierForSignIn courierForSignIn = new CourierForSignIn("incorrect_login", this.courier.getPassword());
        Response logInResponse = CourierTestSteps.loginCourier(courierForSignIn);
        // проверяем, что залогинился неуспешно
        CourierTestSteps.compareErrorResponseStringAndCode(logInResponse, 404, "Учетная запись не найдена");
        CourierForSignIn courierForSignIn2 = new CourierForSignIn(this.courier.getLogin(), this.courier.getPassword());
        Response logInResponse2 = CourierTestSteps.loginCourier(courierForSignIn2);
        // получаем id курьера
        String courierId = CourierTestSteps.getCourierId(logInResponse2);
        // удаляем курьера по id
        Response deleteResponse = CourierTestSteps.deleteCourier(courierId);
        CourierTestSteps.compareSuccessResponseStringAndCode(deleteResponse, "ok", 200);
    }

    @Test
    @DisplayName("Логин за несуществующего курьера (неправильный логин и пароль)")
    public void logInWithNonExistentUser() {
        CourierForSignIn courierForSignIn = new CourierForSignIn("incorrect_login", "incorrect_password");
        Response logInResponse = CourierTestSteps.loginCourier(courierForSignIn);
        // проверяем, что залогинился неуспешно
        CourierTestSteps.compareErrorResponseStringAndCode(logInResponse, 404, "Учетная запись не найдена");
    }
//
}
