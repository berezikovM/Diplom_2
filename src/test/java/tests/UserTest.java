package tests;

import client.AuthClient;
import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.request.CreateUserRequest;
import model.response.AuthResponse;
import model.response.BaseResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

@DisplayName("Тесты пользователя. Регистрация, возвраты")
public class UserTest {

    private AuthClient authClient;
    private UserClient userClient;
    private String accessToken;
    private String email;
    private CreateUserRequest createUserRequest;
    private ValidatableResponse setupResponse;

    private static final String PASSWORD = "password123";
    private static final String NAME = "Test User";

    @Before
    public void setUp() {
        authClient = new AuthClient();
        userClient = new UserClient();
        email = "testuser_" + System.currentTimeMillis() + "@yandex.ru";

        // создаём пользователя
        createUserRequest = CreateUserRequest.builder()
                .email(email).password(PASSWORD).name(NAME).build();

        setupResponse = authClient.register(createUserRequest);

        accessToken = setupResponse.extract().path("accessToken");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Успешное создание уникального пользователя")
    @Description("Проверка, создание уникального пользователя вернуло success = true")
    public void createUniqueUserSuccessTest() {
        AuthResponse auth = setupResponse.statusCode(200).extract().as(AuthResponse.class);
        assertTrue("Поле success должно быть true", auth.getSuccess());
    }

    @Test
    @DisplayName("Успешное возвращение токенов при создании уникального пользователя")
    @Description("Проверка, что пользователю вернулись accessToken и refreshToken")
    public void createUniqueUserReturnsTokensTest() {
        AuthResponse auth = setupResponse.statusCode(200).extract().as(AuthResponse.class);
        assertNotNull("accessToken не должен быть null", auth.getAccessToken());
        assertNotNull("refreshToken не должен быть null", auth.getRefreshToken());
    }

    @Test
    @DisplayName("Успешное возвращение email при создании уникального пользователя")
    @Description("Успешное возвращение email")
    public void createUniqueUserReturnsCorrectEmail() {
        AuthResponse auth = setupResponse.statusCode(200).extract().as(AuthResponse.class);
        assertEquals("Email в ответе не совпадает с отправленным", email, auth.getUser().getEmail());
    }

    @Test
    @DisplayName("Успешное возвращение name при создании уникального пользователя")
    @Description("Успешное возвращение name")
    public void createUniqueUserReturnsCorrectName() {
        AuthResponse auth = setupResponse.statusCode(200).extract().as(AuthResponse.class);
        assertEquals("Имя в ответе не совпадает с отправленным", NAME, auth.getUser().getName());
    }

    @Test
    @DisplayName("Создать уже существующего пользователя")
    @Description("Пытаемся создать пользователя с данными того, кто уже был создан в @Before")
    public void createExistingUserReturns403Test() {
        // Попытка создать того же самого пользователя второй раз
        ValidatableResponse response = authClient.register(createUserRequest);

        BaseResponse base = response.statusCode(403).extract().as(BaseResponse.class);
        assertFalse("Поле success должно быть false при дубликате", base.getSuccess());
        assertEquals("Текст ошибки не совпадает", "User already exists", base.getMessage());
    }

}