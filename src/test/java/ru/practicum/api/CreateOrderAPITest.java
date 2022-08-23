package ru.practicum.api;

import POJO.Order;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.practicum.api.steps.OrderTestSteps;

@RunWith(Parameterized.class)
public class CreateOrderAPITest {
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] color;
    private int trackNumber;

    public CreateOrderAPITest(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color) {
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
            { "Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", new String [] { "BLACK" } },
            { "Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", new String [] { "GRAY" } },
            { "Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", new String [] { "BLACK", "GRAY" } },
            { "Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", new String [] { } },
            { "Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", new String [] { "RED" } },
        };
    }

    @Test
    @DisplayName("Создание заказов с разными цветами скутеров")
    public void createOrderParameterizedTest() {
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        Response createResponse = OrderTestSteps.createNewOrder(order);
        OrderTestSteps.compareSuccessOrderSet(createResponse, 201);
        String orderTrack = OrderTestSteps.getOrderTrack(createResponse);
        // удаляем заказ
        Response deleteResponse = OrderTestSteps.deleteOrder(orderTrack);
        OrderTestSteps.compareSuccessOrderCancel(deleteResponse, 200);
    }
}
