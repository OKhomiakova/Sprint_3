package steps;

import ru.practicum.api.SetUp;
import POJO.Courier;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierTestSteps {

    @Step("Создание нового курьера")
    public Response createNewCourier(Courier courier) {
        Response response = given()
                .spec(SetUp.requestSpec())
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier");
        return response;
    }
}
