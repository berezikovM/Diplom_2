package tests;

import client.AuthClient;
import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.request.CreateUserRequest;
import model.response.BaseResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

@DisplayName("Тесты пользователя. Регистрация, без одного из полей")
public class UserIncorrectTest {

    private AuthClient authClient;
    private UserClient userClient;
    private String accessToken;
    private String email;

    private static final String PASSWORD = "password123";
    private static final String NAME = "Test User";

    @Before
    public void setUp() {
        authClient = new AuthClient();
        userClient = new UserClient();
        email = "testuser_" + System.currentTimeMillis() + "@yandex.ru";
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken); // очистка, на всякий случай
        }
    }

    @Test
    @DisplayName("Создать пользователя без email")
    @Description("Проверка, создание пользователя без email и ожидаем → 403 + required fields")
    public void createUserWithoutEmailReturns403Test() {
        CreateUserRequest request = CreateUserRequest.builder()
                .password(PASSWORD).name(NAME).build();

        ValidatableResponse response = authClient.register(request);

        accessToken = response.extract().path("accessToken"); // на всякий случай

        BaseResponse base = response.statusCode(403).extract().as(BaseResponse.class);
        assertFalse(base.getSuccess());
        assertThat(base.getMessage(), containsString("required fields"));
    }

    @Test
    @DisplayName("Создать пользователя без password")
    @Description("Проверка, создание пользователя без password и ожидаем → 403 + required fields")
    public void createUserWithoutPasswordReturns403Test() {
        CreateUserRequest request = CreateUserRequest.builder()
                .email(email).name(NAME).build();

        ValidatableResponse response = authClient.register(request);

        accessToken = response.extract().path("accessToken"); // на всякий случай

        BaseResponse base = response.statusCode(403).extract().as(BaseResponse.class);
        assertFalse(base.getSuccess());
        assertThat(base.getMessage(), containsString("required fields"));
    }

    @Test
    @DisplayName("Создать пользователя без name")
    @Description("Проверка, создание пользователя без name и ожидаем → 403 + required fields")
    public void createUserWithoutNameReturns403Test() {
        CreateUserRequest request = CreateUserRequest.builder()
                .email(email).password(PASSWORD).build();

        ValidatableResponse response = authClient.register(request);

        accessToken = response.extract().path("accessToken"); // на всякий случай

        BaseResponse base = response.statusCode(403).extract().as(BaseResponse.class);
        assertFalse(base.getSuccess());
        assertThat(base.getMessage(), containsString("required fields"));
    }

}