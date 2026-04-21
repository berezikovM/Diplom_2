package tests;

import client.AuthClient;
import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.request.CreateUserRequest;
import model.request.LoginRequest;
import model.response.AuthResponse;
import model.response.BaseResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

@DisplayName("Тесты пользователя (регистрация и логин)")
public class UserTest {

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
            userClient.deleteUser(accessToken); // очистка
        }
    }

    // ====================== РЕГИСТРАЦИЯ ======================

    @Test
    @DisplayName("Создать уникального пользователя → success = true")
    public void createUniqueUserSuccessTest() {
        CreateUserRequest request = CreateUserRequest.builder()
                .email(email).password(PASSWORD).name(NAME).build();

        ValidatableResponse response = authClient.register(request);

        AuthResponse auth = response.statusCode(200).extract().as(AuthResponse.class);
        assertTrue(auth.getSuccess());
    }

    @Test
    @DisplayName("Создать уникального пользователя → возвращаются accessToken и refreshToken")
    public void createUniqueUserReturnsTokensTest() {
        CreateUserRequest request = CreateUserRequest.builder()
                .email(email).password(PASSWORD).name(NAME).build();

        ValidatableResponse response = authClient.register(request);

        AuthResponse auth = response.statusCode(200).extract().as(AuthResponse.class);
        assertNotNull(auth.getAccessToken());
        assertNotNull(auth.getRefreshToken());
    }

    @Test
    @DisplayName("Создать уникального пользователя → возвращаются корректные email и name")
    public void createUniqueUserReturnsCorrectDataTest() {
        CreateUserRequest request = CreateUserRequest.builder()
                .email(email).password(PASSWORD).name(NAME).build();

        ValidatableResponse response = authClient.register(request);

        AuthResponse auth = response.statusCode(200).extract().as(AuthResponse.class);
        assertEquals(email, auth.getUser().getEmail());
        assertEquals(NAME, auth.getUser().getName());
    }

    @Test
    @DisplayName("Создать уже существующего пользователя → 403 + User already exists")
    public void createExistingUserReturns403Test() {
        CreateUserRequest request = CreateUserRequest.builder()
                .email(email).password(PASSWORD).name(NAME).build();

        authClient.register(request); // первый раз

        ValidatableResponse response = authClient.register(request);

        BaseResponse base = response.statusCode(403).extract().as(BaseResponse.class);
        assertFalse(base.getSuccess());
        assertEquals("User already exists", base.getMessage());
    }

    @Test
    @DisplayName("Создать пользователя без email → 403 + required fields")
    public void createUserWithoutEmailReturns403Test() {
        CreateUserRequest request = CreateUserRequest.builder()
                .password(PASSWORD).name(NAME).build();

        ValidatableResponse response = authClient.register(request);

        BaseResponse base = response.statusCode(403).extract().as(BaseResponse.class);
        assertFalse(base.getSuccess());
        assertThat(base.getMessage(), containsString("required fields"));
    }

    @Test
    @DisplayName("Создать пользователя без password → 403 + required fields")
    public void createUserWithoutPasswordReturns403Test() {
        CreateUserRequest request = CreateUserRequest.builder()
                .email(email).name(NAME).build();

        ValidatableResponse response = authClient.register(request);

        BaseResponse base = response.statusCode(403).extract().as(BaseResponse.class);
        assertFalse(base.getSuccess());
        assertThat(base.getMessage(), containsString("required fields"));
    }

    @Test
    @DisplayName("Создать пользователя без name → 403 + required fields")
    public void createUserWithoutNameReturns403Test() {
        CreateUserRequest request = CreateUserRequest.builder()
                .email(email).password(PASSWORD).build();

        ValidatableResponse response = authClient.register(request);

        BaseResponse base = response.statusCode(403).extract().as(BaseResponse.class);
        assertFalse(base.getSuccess());
        assertThat(base.getMessage(), containsString("required fields"));
    }

    // ====================== ЛОГИН ======================

    @Test
    @DisplayName("Войти под существующим пользователем → success = true")
    public void loginExistingUserSuccessTest() {
        // создаём пользователя
        CreateUserRequest reg = CreateUserRequest.builder()
                .email(email).password(PASSWORD).name(NAME).build();
        authClient.register(reg);

        LoginRequest login = LoginRequest.builder()
                .email(email).password(PASSWORD).build();

        ValidatableResponse response = authClient.login(login);

        AuthResponse auth = response.statusCode(200).extract().as(AuthResponse.class);
        assertTrue(auth.getSuccess());
    }

    @Test
    @DisplayName("Войти с неверным email/password → 401 + email or password are incorrect")
    public void loginWithWrongCredentialsReturns401Test() {
        LoginRequest login = LoginRequest.builder()
                .email("wrong@email.ru").password("wrongpass").build();

        ValidatableResponse response = authClient.login(login);

        BaseResponse base = response.statusCode(401).extract().as(BaseResponse.class);
        assertFalse(base.getSuccess());
        assertEquals("email or password are incorrect", base.getMessage());
    }
}