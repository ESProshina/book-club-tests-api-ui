package tests;

import models.login.LoginBodyModel;
import models.user.UpdateUserBodyModel;
import models.user.UserResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.user.UserSpec.*;
import static tests.TestData.*;

public class UpdateUserTests extends TestBase {

    private String accessToken;

    @BeforeEach
    public void auth() {
        step("Авторизация и получение access-токена", () -> {
            LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);
            accessToken = api.auth.loginAndGetAccessToken(loginData);
        });
    }

    @Test
    @DisplayName("Позитивный: Успешное обновление имени, фамилии и email")
    public void successfulUpdateUserTest() {
        String uniqueEmail = "elena_" + System.currentTimeMillis() + "@example.com";

        UpdateUserBodyModel updateData = new UpdateUserBodyModel(
                UPDATED_FIRST_NAME,
                UPDATED_LAST_NAME,
                uniqueEmail
        );

        UserResponseModel response = step(
                "Отправка PATCH /users/me/ с новыми данными",
                () -> api.users.updateUser(accessToken, updateData)
        );

        step("Проверка поля firstName", () ->
                assertThat(response.firstName()).isEqualTo(UPDATED_FIRST_NAME));

        step("Проверка поля lastName", () ->
                assertThat(response.lastName()).isEqualTo(UPDATED_LAST_NAME));

        step("Проверка поля email", () ->
                assertThat(response.email()).isEqualTo(uniqueEmail));
    }

    @Test
    @DisplayName("Негативный: Обновление с некорректным форматом email (400 Bad Request)")
    public void updateUserWithInvalidEmailTest() {
        UpdateUserBodyModel updateData = new UpdateUserBodyModel(
                UPDATED_FIRST_NAME,
                UPDATED_LAST_NAME,
                INVALID_EMAIL
        );

        var response = step(
                "Отправка PATCH /users/me/ с невалидным email",
                () -> api.users.updateUserWithSpec(accessToken, updateData, userResponse400Spec)
        );

        step("Проверка сообщения об ошибке email", () ->
                assertThat(response.path("email[0]").toString())
                        .isEqualTo(INVALID_EMAIL_ERROR));
    }

    @Test
    @DisplayName("Негативный: Обновление пользователя без токена авторизации (401 Unauthorized)")
    public void updateUserWithoutTokenTest() {
        UpdateUserBodyModel updateData = new UpdateUserBodyModel(
                UPDATED_FIRST_NAME,
                UPDATED_LAST_NAME,
                UPDATED_EMAIL
        );

        var response = step(
                "Отправка PATCH /users/me/ без токена авторизации",
                () -> api.users.updateUserWithSpec(null, updateData, userResponse401Spec)
        );

        step("Проверка сообщения об ошибке", () ->
                assertThat(response.path("detail").toString())
                        .isEqualTo(UNAUTHORIZED_ERROR));
    }
}