package ru.practicum.api.steps;

import POJO.Order;
import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import ru.practicum.api.SetUp;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class OrderTestSteps {

    static String endPointOrders = "/api/v1/orders";
    static String getEndPointOrderCancel = "/api/v1/orders/cancel";

    @Step("Создание нового заказа")
    public static Response createNewOrder(Order order) {
        Response response = given()
                .spec(SetUp.requestSpec())
                .header("Content-type", "application/json")
                .body(order)
                .post(endPointOrders);
        return response;
    }

    @Step("Сравнение кода ответа")
    public static void compareSuccessOrderSet(Response response, int responseCode) {
        response.then().assertThat().body("track", not(0)).and().statusCode(responseCode);
    }

    @Step("Сравнение кода ответа об отмене")
    public static void compareSuccessOrderCancel(Response response, int responseCode) {
        response.then().assertThat().body("ok", equalTo(true)).and().statusCode(responseCode);
    }

    @Step("Получение трека заказа")
    public static String getOrderTrack(Response response) {
        String trackNumber = response.then().extract().body().asString();
        JsonPath jsonPath = new JsonPath(trackNumber);
        String track = jsonPath.getString("track");
        return track;
    }

    @Step("Удаление заказа по треку")
    public static Response deleteOrder(String track) {
        Response deleteResponse = given()
                .spec(SetUp.requestSpec())
                .header("Content-type", "application/json")
                .put(getEndPointOrderCancel + "?track=" + track);
        return deleteResponse;
    }

    @Step("Получение списка всех заказов")
    public static Response getAllOrders() {
        Response response = given()
                .spec(SetUp.requestSpec())
                .header("Content-type", "application/json")
                .body("")
                .get(endPointOrders);
        return response;
    }




}
