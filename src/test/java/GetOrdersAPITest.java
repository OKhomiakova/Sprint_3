import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrdersAPITest {
    @Before
    public void setUp() {
        RestAssured.baseURI= "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Get all orders")
    public void getAllOrders() {
        Response response = given()
                .header("Content-type", "application/json")
                .body("")
                .get("/api/v1/orders");
        response.then().assertThat().body("orders", notNullValue()).and().statusCode(200);
    }
}
