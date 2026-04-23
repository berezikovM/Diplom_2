package tests;

import client.AuthClient;
import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.request.CreateUserRequest;
import model.request.LoginRequest;
import model.response.AuthResponse;
import model.response.BaseResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

@DisplayName("Тесты пользователя. Логин")
public class UserLoginTest {

    private AuthClient authClient;
    private UserClient userClient;
    private String accessToken;
    private String email;
    private CreateUserRequest createUserRequest;
    private ValidatableResponse setupResponse;

    private static final String PASSWORD = "password123";
    private static final String NAME = "Test User";

    private static final String WRONG_PASSWORD = "wrongpass";
    private static final String WRONG_EMAIL = "wrong@email.ru";

    @Before
    public void setUp() {
        authClient = new AuthClient();
        userClient = new UserClient();
        email = "testuser_" + System.currentTimeMillis() + "@yandex.ru";

        // создаём
        createUserRequest = CreateUserRequest.builder()
                .email(email).password(PASSWORD).name(NAME).build();

        setupResponse = authClient.register(createUserRequest);

        accessToken = setupResponse.extract().path("accessToken");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken); // очистка
        }
    }

    @Test
    @DisplayName("Войти под существующим пользователем")
    @Description("Проверка, войти под существующим пользователем и ожидаем → success = true")
    public void loginExistingUserSuccessTest() {
        LoginRequest login = LoginRequest.builder()
                .email(email).password(PASSWORD).build();

        ValidatableResponse response = authClient.login(login);

        AuthResponse auth = response.statusCode(200).extract().as(AuthResponse.class);
        assertTrue(auth.getSuccess());
    }

    @Test
    @DisplayName("Войти с неверным email")
    @Description("Проверка, войти с неверным email и ожидаем → 401 + email or password are incorrect")
    public void loginWithWrongEmailReturns401Test() {
        LoginRequest login = LoginRequest.builder()
                .email(WRONG_EMAIL).password(PASSWORD).build();

        ValidatableResponse response = authClient.login(login);

        BaseResponse base = response.statusCode(401).extract().as(BaseResponse.class);
        assertFalse(base.getSuccess());
        assertEquals("email or password are incorrect", base.getMessage());
    }

    @Test
    @DisplayName("Войти с неверным password")
    @Description("Проверка, войти с неверным password и ожидаем → 401 + email or password are incorrect")
    public void loginWithWrongPasswordReturns401Test() {
        LoginRequest login = LoginRequest.builder()
                .email(email).password(WRONG_PASSWORD).build();

        ValidatableResponse response = authClient.login(login);

        BaseResponse base = response.statusCode(401).extract().as(BaseResponse.class);
        assertFalse(base.getSuccess());
        assertEquals("email or password are incorrect", base.getMessage());
    }

    @Test
    @DisplayName("Войти с неверным email и password")
    @Description("Проверка, войти с неверным email и password и ожидаем → 401 + email or password are incorrect")
    public void loginWithWrongEmailAndPasswordReturns401Test() {
        LoginRequest login = LoginRequest.builder()
                .email(WRONG_EMAIL).password(WRONG_PASSWORD).build();

        ValidatableResponse response = authClient.login(login);

        BaseResponse base = response.statusCode(401).extract().as(BaseResponse.class);
        assertFalse(base.getSuccess());
        assertEquals("email or password are incorrect", base.getMessage());
    }
}
