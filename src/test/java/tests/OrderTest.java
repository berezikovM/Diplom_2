package tests;

import client.AuthClient;
import client.OrderClient;
import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.request.CreateOrderRequest;
import model.request.CreateUserRequest;
import model.response.BaseResponse;
import model.response.OrderResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@DisplayName("Тесты создания заказа")
public class OrderTest {

    private AuthClient authClient;
    private OrderClient orderClient;
    private UserClient userClient;        // добавили для очистки
    private String accessToken;

    // Валидные ingredient id (рекомендуется позже заменить на динамические)
    private final List<String> VALID_INGREDIENTS = Arrays.asList(
            "61c0c5a71d1f82001bdaaa6d",
            "61c0c5a71d1f82001bdaaa6f"
    );

    @Before
    public void setUp() {
        authClient = new AuthClient();
        orderClient = new OrderClient();
        userClient = new UserClient();

        String email = "orderuser_" + System.currentTimeMillis() + "@yandex.ru";

        CreateUserRequest user = CreateUserRequest.builder()
                .email(email)
                .password("123456")
                .name("Order Tester")
                .build();

        ValidatableResponse reg = authClient.register(user);

        accessToken = reg.extract().path("accessToken");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);   // очистка
        }
    }

    // ====================== ПОЗИТИВНЫЕ СЦЕНАРИИ ======================

    @Test
    @DisplayName("Успешное создать заказа с авторизацией")
    @Description("Проверка, создания заказа с авторизацией и ожидаем → success = true")
    public void createOrderWithAuthSuccessTest() {
        CreateOrderRequest request = CreateOrderRequest.builder()
                .ingredients(VALID_INGREDIENTS).build();

        ValidatableResponse response = orderClient.createOrder(accessToken, request);

        OrderResponse orderResp = response.statusCode(200).extract().as(OrderResponse.class);
        assertTrue(orderResp.getSuccess());
    }

    @Test
    @DisplayName("Успешное возвращение номера заказа при создании заказа с авторизацией")
    @Description("Проверка, создания заказа с авторизацией и ожидаем → возвращается номер заказа")
    public void createOrderReturnsOrderNumberTest() {
        CreateOrderRequest request = CreateOrderRequest.builder()
                .ingredients(VALID_INGREDIENTS).build();

        ValidatableResponse response = orderClient.createOrder(accessToken, request);

        OrderResponse orderResp = response.statusCode(200).extract().as(OrderResponse.class);
        assertNotNull(orderResp.getOrder());
        assertTrue(orderResp.getOrder().getNumber() > 0);
    }

    // ====================== СЦЕНАРИЙ БЕЗ АВТОРИЗАЦИИ ======================

    @Test
    @DisplayName("Успешное создание заказа без авторизации")
    @Description("Проверка, создания заказа без авторизациии и ожидаем → success = true")
    public void createOrderWithoutAuthorizationSuccessTest() {
        CreateOrderRequest request = CreateOrderRequest.builder()
                .ingredients(VALID_INGREDIENTS).build();

        ValidatableResponse response = orderClient.createOrder(null, request);

        OrderResponse orderResp = response.statusCode(200).extract().as(OrderResponse.class);
        assertTrue(orderResp.getSuccess());
    }

    @Test
    @DisplayName("Успешное возвращение номера заказа при создании заказа без авторизации")
    @Description("Проверка, создания заказа без авторизации и и ожидаем → возвращается номер заказа")
    public void createOrderWithoutAuthorizationReturnsNumberTest() {
        CreateOrderRequest request = CreateOrderRequest.builder()
                .ingredients(VALID_INGREDIENTS).build();

        ValidatableResponse response = orderClient.createOrder(null, request);

        OrderResponse orderResp = response.statusCode(200).extract().as(OrderResponse.class);
        assertNotNull(orderResp.getOrder());
        assertTrue(orderResp.getOrder().getNumber() > 0);
    }

    // ====================== НЕГАТИВНЫЕ СЦЕНАРИИ ======================

    @Test
    @DisplayName("Создать заказ без ингредиентов")
    @Description("Проверка, создания заказ без ингредиентов и ожидаем → 400 + Ingredient ids must be provided")
    public void createOrderWithoutIngredientsReturns400Test() {
        CreateOrderRequest request = CreateOrderRequest.builder()
                .ingredients(Collections.emptyList()).build();

        ValidatableResponse response = orderClient.createOrder(accessToken, request);

        BaseResponse base = response.statusCode(400).extract().as(BaseResponse.class);
        assertFalse(base.getSuccess());
        assertEquals("Ingredient ids must be provided", base.getMessage());
    }

    @Test
    @DisplayName("Создать заказ с неверным хешем ингредиента")
    @Description("Проверка, создания заказ без ингредиентов и ожидаем → 500")
    public void createOrderWithInvalidHashReturns500Test() {
        CreateOrderRequest request = CreateOrderRequest.builder()
                .ingredients(Collections.singletonList("invalid_hash_123456")).build();

        ValidatableResponse response = orderClient.createOrder(accessToken, request);

        response.statusCode(500);
    }
}