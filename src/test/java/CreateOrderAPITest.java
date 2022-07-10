import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.not;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;

@RunWith(Parameterized.class)
public class CreateOrderAPITest {
    private final String firstName;
    private final String lastName;
    private final String address;
    private final int metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] color;
    private int trackNumber;

    public CreateOrderAPITest(String firstName, String lastName, String address, int metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getOrderData() {
        return new Object[][] {
            { "Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", new String [] { "BLACK" } },
            { "Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", new String [] { "GRAY" } },
            { "Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", new String [] { "BLACK", "GRAY" } },
            { "Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", new String [] { } },
            { "Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", new String [] { "RED" } },
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI= "http://qa-scooter.praktikum-services.ru/";
    }

    @After
    public void cancelOrder() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        System.out.println("[deleteTrack]: trackNumber " + trackNumber);

        HttpPut httpput = new HttpPut("http://qa-scooter.praktikum-services.ru/api/v1/orders/cancel?track=" + trackNumber);

        //Execute and get the response.
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpput);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpEntity entity = response.getEntity();

        String responseString = null;
        try {
            responseString = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            System.out.println("[deleteTrack]: " + "exception");
            e.printStackTrace();
        }
        System.out.println("[deleteTrack]: response " + responseString);

    }

    @Test
    @DisplayName("Create orders with different scooter color options")
    public void createOrderParameterizedTest() {
        Response response = given()
                .header("Content-type", "application/json")
                .body("")
                .post("/api/v1/orders");
        response.then().assertThat().body("track", not(0)).and().statusCode(201);

        JsonPath jsonPath = new JsonPath(response.getBody().print());
        trackNumber = jsonPath.getInt("track");
    }
}
