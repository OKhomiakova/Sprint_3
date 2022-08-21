import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

public class SetUp {
        public static RequestSpecification requestSpec() {
            return given()
                    .baseUri("http://qa-scooter.praktikum-services.ru/");
        }
}
