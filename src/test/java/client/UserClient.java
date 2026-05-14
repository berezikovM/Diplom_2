package client;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import model.request.UpdateUserRequest;

public class UserClient extends BaseClient {

    private static final String USER_PATH = "/api/auth/user";

    @Step("Получить данные пользователя")
    public ValidatableResponse getUser(String accessToken) {
        return RestAssured.given()
                .spec(getAuthSpec(accessToken))
                .get(USER_PATH)
                .then();
    }

    @Step("Обновить данные пользователя")
    public ValidatableResponse updateUser(String accessToken, UpdateUserRequest updateData) {
        return RestAssured.given()
                .spec(getAuthSpec(accessToken))
                .body(updateData)
                .patch(USER_PATH)
                .then();
    }

    @Step("Удалить пользователя")
    public ValidatableResponse deleteUser(String accessToken) {
        return RestAssured.given()
                .spec(getAuthSpec(accessToken))
                .delete(USER_PATH)
                .then();
    }
}
