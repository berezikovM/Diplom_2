package client;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import model.request.CreateOrderRequest;

public class OrderClient extends BaseClient {

    private static final String ORDERS_PATH = "/api/orders";
    private static final String ORDERS_ALL_PATH = "/api/orders/all";

    @Step("Создание заказа")
    public ValidatableResponse createOrder(String accessToken, CreateOrderRequest orderRequest) {
        if (accessToken == null || accessToken.isEmpty()) {
            // Без авторизации
            return RestAssured.given()
                    .spec(getBaseSpec())
                    .body(orderRequest)
                    .post(ORDERS_PATH)
                    .then();
        } else {
            // С авторизацией
            return RestAssured.given()
                    .spec(getAuthSpec(accessToken))
                    .body(orderRequest)
                    .post(ORDERS_PATH)
                    .then();
        }
    }

    @Step("Получить заказы конкретного пользователя (требует авторизации)")
    public ValidatableResponse getUserOrders(String accessToken) {
        return RestAssured.given()
                .spec(getAuthSpec(accessToken))
                .get(ORDERS_PATH)
                .then();
    }

    @Step("Получить все заказы (публичный эндпоинт)")
    public ValidatableResponse getAllOrders() {
        return RestAssured.given()
                .spec(getBaseSpec())
                .get(ORDERS_ALL_PATH)
                .then();
    }
}