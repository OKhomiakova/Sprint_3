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
import static io.restassured.RestAssured.requestSpecification;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class LoginCourierAPITest {

    @Before
    public void createUser() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("http://qa-scooter.praktikum-services.ru/api/v1/courier");

        // Request parameters and other properties.
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("login", "okhomiakova_"));
        params.add(new BasicNameValuePair("password", "12345"));
        params.add(new BasicNameValuePair("firstName", "olya"));
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
    @DisplayName("Log in successfully with existing courier")
    public void logInWithExistingCourier() {
        Response response = given()
                .spec(SetUp.requestSpec())
                .header("Content-type", "application/json")
                .body("{\"login\": \"okhomiakova_\",\"password\": \"12345\"}")
                .post("/api/v1/courier/login");
        response.then().assertThat().body("id", not(0)).and().statusCode(200);
    }

    @Test
    @DisplayName("Log in without login")
    public void logInWithoutLogin() {
        Response response = given()
                .spec(SetUp.requestSpec())
                .header("Content-type", "application/json")
                .body("{\"login\": \"\", \"password\": \"12345\"}")
                .post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа")).and().statusCode(400);
    }

    @Test
    @DisplayName("Log in without password")
    public void logInWithoutPassword() {
        Response response = given()
                .spec(SetUp.requestSpec())
                .header("Content-type", "application/json")
                .body("{\"login\": \"okhomiakova_\", \"password\": \"\"}")
                .post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа")).and().statusCode(400);
    }

    @Test
    @DisplayName("Log in with incorrect login")
    public void logInWithIncorrectLogin() {
        Response response = given()
                .spec(SetUp.requestSpec())
                .header("Content-type", "application/json")
                .body("{\"login\": \"okokhomiakova_\",\"password\": \"12345\"}")
                .post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена")).and().statusCode(404);
    }

    @Test
    @DisplayName("Log in with incorrect password")
    public void logInWithIncorrectPassword() {
        Response response = given()
                .spec(SetUp.requestSpec())
                .header("Content-type", "application/json")
                .body("{\"login\": \"okhomiakova_\",\"password\": \"54321\"}")
                .post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена")).and().statusCode(404);
    }

    @Test
    @DisplayName("Log in with non-existent user")
    public void logInWithNonExistentUser() {
        Response response = given()
                .spec(SetUp.requestSpec())
                .header("Content-type", "application/json")
                .body("{\"login\": \"notokhomiakova_\",\"password\": \"54321\"}")
                .post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена")).and().statusCode(404);
    }
}
