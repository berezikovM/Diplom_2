package client;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import model.request.CreateUserRequest;
import model.request.LoginRequest;
import model.request.LogoutRequest;

public class AuthClient extends BaseClient {

    private static final String REGISTER_PATH = "/api/auth/register";
    private static final String LOGIN_PATH = "/api/auth/login";
    private static final String LOGOUT_PATH = "/api/auth/logout";
    private static final String TOKEN_PATH = "/api/auth/token";

    @Step("Регистрация пользователя")
    public ValidatableResponse register(CreateUserRequest user) {
        return RestAssured.given()
                .spec(getBaseSpec())
                .body(user)
                .post(REGISTER_PATH)
                .then();
    }

    @Step("Логин пользователя")
    public ValidatableResponse login(LoginRequest credentials) {
        return RestAssured.given()
                .spec(getBaseSpec())
                .body(credentials)
                .post(LOGIN_PATH)
                .then();
    }

    @Step("Выход из системы")
    public ValidatableResponse logout(String refreshToken) {
        LogoutRequest request = LogoutRequest.builder().token(refreshToken).build();

        return RestAssured.given()
                .spec(getBaseSpec())
                .body(request)
                .post(LOGOUT_PATH)
                .then();
    }

    @Step("Обновление токена")
    public ValidatableResponse refreshToken(String refreshToken) {
        return RestAssured.given()
                .spec(getBaseSpec())
                .body("{\"token\":\"" + refreshToken + "\"}")
                .post(TOKEN_PATH)
                .then();
    }

}