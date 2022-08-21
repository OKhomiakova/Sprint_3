package ru.practicum.api.steps;

import io.restassured.path.json.JsonPath;
import ru.practicum.api.SetUp;
import POJO.CourierForSignUp;
import POJO.CourierForSignIn;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.equalTo;

public class CourierTestSteps {

    static String endPointCreateCourier = "/api/v1/courier";
    static String endPointLogin = "/api/v1/courier/login";

    @Step("Создание нового курьера")
    public static Response createNewCourier(CourierForSignUp courier) {
        Response response = given()
                .spec(SetUp.requestSpec())
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(endPointCreateCourier);
        return response;
    }

    @Step("Сравнение кода ответа (успех)")
    public static void compareSuccessResponseStringAndCode(Response response, String responseString, int responseStatusCode) {
        response.then().assertThat().body(responseString, equalTo(true)).and().statusCode(responseStatusCode);
    }

    @Step("Сравнение кода ответа логина (успех)")
    public static void compareSuccessLoginResponseStringAndCode(Response response, int responseStatusCode) {
        response.then().assertThat().body("id", not(0)).and().statusCode(responseStatusCode);
    }

    @Step("Сравнение кода ответа (ошибка)")
    public static void compareErrorResponseStringAndCode(Response response, int responseStatusCode, String message) {
        response.then().assertThat().body("message", equalTo(message)).and().statusCode(responseStatusCode);
    }

    @Step("Логин курьера")
    public static Response loginCourier(CourierForSignIn courier) {
        Response logInResponse = given()
                .spec(SetUp.requestSpec())
                .header("Content-type", "application/json")
                .body(courier)
                .post(endPointLogin);
        return logInResponse;
    }

    @Step("Получение id курьера")
    public static String getCourierId(Response response) {
        String courierId = response.then().extract().body().asString();
        JsonPath jsonPath = new JsonPath(courierId);
        String id = jsonPath.getString("id");
        return id;
    }

    @Step("Удаление курьера")
    public static Response deleteCourier(String id) {
        Response deleteResponse = given()
                .spec(SetUp.requestSpec())
                .header("Content-type", "application/json")
                .delete("/api/v1/courier/{id}", id);
        return deleteResponse;
    }
}
