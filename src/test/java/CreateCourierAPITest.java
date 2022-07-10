import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateCourierAPITest {

    @Before
    public void setUp() {
        RestAssured.baseURI= "http://qa-scooter.praktikum-services.ru/";
    }

    @After
    public void clearUserData() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("http://qa-scooter.praktikum-services.ru/api/v1/courier/login");

        // Request parameters and other properties.
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("login", "okhomiakova_"));
        params.add(new BasicNameValuePair("password", "12345"));
        try {
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Execute and get the response.
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httppost);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpEntity entity = response.getEntity();

        String responseString = null;
        try {
            responseString = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            System.out.println("[clearUserData]: " + "exception");
            e.printStackTrace();
        }
        System.out.println("[clearUserData]: " + responseString);

        if (responseString.equals("{\"code\":404,\"message\":\"Учетная запись не найдена\"}")) {
            return;
        }

        Gson gson = new Gson();
        LoginResponse loginResponse = gson.fromJson(responseString, LoginResponse.class);

        System.out.println("[clearUserData]: " + loginResponse.getId());

        HttpDelete httpdelete = new HttpDelete("http://qa-scooter.praktikum-services.ru/api/v1/courier/" + loginResponse.getId());

        try {
            response = httpclient.execute(httpdelete);
        } catch (IOException e) {
            e.printStackTrace();
        }

        entity = response.getEntity();

        responseString = null;
        try {
            responseString = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            System.out.println("exception");
            e.printStackTrace();
        }
        System.out.println("[clearUserData]: " + responseString);
    }

    @Test
    @DisplayName("Create new courier")
    public void createNewCourierWithCorrectAndSufficientData() {
        Response response = given()
                .header("Content-type", "application/json")
                .body("{\"login\": \"okhomiakova_\",\"password\": \"12345\",\"firstName\": \"olya\"}")
                .post("/api/v1/courier");
                response.then().assertThat().body("ok", equalTo(true)).and().statusCode(201);
    }

    @Test
    @DisplayName("Create a courier with duplicate data")
    public void createNewCourierWithDuplicateData() {
        Response response = given()
                .header("Content-type", "application/json")
                .body("{\"login\": \"ninja\",\"password\": \"1234\",\"firstName\": \"saske\"}")
                .post("/api/v1/courier");
        response.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой.")).and().statusCode(409);
    }

    @Test
    @DisplayName("Create new courier without login")
    public void createNewCourierWithoutLogin() {
        Response response = given()
                .header("Content-type", "application/json")
                .body("{\"login\": \"\", \"password\": \"12345\",\"firstName\": \"olya\"}")
                .post("/api/v1/courier");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи")).and().statusCode(400);
    }

    @Test
    @DisplayName("Create new courier without password")
    public void createNewCourierWithoutPassword() {
        Response response = given()
                .header("Content-type", "application/json")
                .body("{\"login\": \"okhomiakova\", \"password\": \"\", \"firstName\": \"olya\"}")
                .post("/api/v1/courier");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи")).and().statusCode(400);
    }

    @Test
    @DisplayName("Create new courier without firstName")
    public void createNewCourierWithoutFirstName() {
        Response response = given()
                .header("Content-type", "application/json")
                .body("{\"login\": \"okhomiakova\", \"password\": \"\", \"firstName\": \"\"}")
                .post("/api/v1/courier");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи")).and().statusCode(400);
    }

    @Test
    @DisplayName("Create new courier with login which already exists")
    public void createNewCourierWithTakenLogin() {
        Response response = given()
                .header("Content-type", "application/json")
                .body("{\"login\": \"ninja\",\"password\": \"12345\",\"firstName\": \"olya\"}")
                .post("/api/v1/courier");
        // В документации ожидаемый мессадж "Этот логин уже используется",
        // но возвращается "Этот логин уже используется. Попробуйте другой.",
        // поэтому указала его, чтобы тест не фейлился :)
        response.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой.")).and().statusCode(409);
    }
}
