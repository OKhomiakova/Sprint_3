package ru.practicum.api;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;

import org.junit.Test;
import ru.practicum.api.steps.OrderTestSteps;

import static org.hamcrest.Matchers.*;

public class GetOrdersAPITest {

    @Test
    @DisplayName("Get all orders")
    public void getAllOrders() {
        Response response = OrderTestSteps.getAllOrders();
        response.then().assertThat().body("orders", hasSize(greaterThan(0))).and().statusCode(200);
    }
}
