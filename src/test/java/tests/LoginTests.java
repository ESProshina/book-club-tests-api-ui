package tests;

import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.login.LoginSpec.loginResponse400Spec;
import static specs.login.LoginSpec.wrongCredentialsLoginResponseSpec;
import static tests.TestData.*;

public class LoginTests extends TestBase {

    @Test
    @DisplayName("Позитивный: Успешная авторизация (200 OK)")
    public void successfulLoginTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);

        SuccessfulLoginResponseModel loginResponse = step(
                "Отправка POST /auth/token/ с логином " + LOGIN_USERNAME,
                () -> api.auth.login(loginData)
        );

        step("Проверка access-токена", () ->
                assertThat(loginResponse.access()).startsWith(LOGIN_TOKEN_PREFIX));

        step("Проверка refresh-токена", () ->
                assertThat(loginResponse.refresh()).startsWith(LOGIN_TOKEN_PREFIX));

        step("Проверка, что access и refresh различаются", () ->
                assertThat(loginResponse.access()).isNotEqualTo(loginResponse.refresh()));
    }

    @Test
    @DisplayName("Негативный: Авторизация с неверными учетными данными (401 Unauthorized)")
    public void wrongCredentialsLoginTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_WRONG_PASSWORD);

        var response = step(
                "Отправка POST /auth/token/ с неверным паролем",
                () -> api.auth.loginWithSpec(loginData, wrongCredentialsLoginResponseSpec)
        );

        step("Проверка сообщения об ошибке", () ->
                assertThat(response.path("detail").toString())
                        .isEqualTo(LOGIN_WRONG_CREDENTIALS_ERROR));
    }

    @Test
    @DisplayName("Негативный: Авторизация с неверным паролем (401 Unauthorized)")
    public void loginWithInvalidPasswordTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_WRONG_PASSWORD);

        var response = step(
                "Отправка POST /auth/token/ с неверным паролем",
                () -> api.auth.loginWithSpec(loginData, wrongCredentialsLoginResponseSpec)
        );

        step("Проверка сообщения об ошибке", () ->
                assertThat(response.path("detail").toString())
                        .isEqualTo(LOGIN_WRONG_CREDENTIALS_ERROR));
    }

    @Test
    @DisplayName("Негативный: Авторизация с несуществующим пользователем (401 Unauthorized)")
    public void loginWithNonExistentUserTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_WRONG_USERNAME, LOGIN_PASSWORD);

        var response = step(
                "Отправка POST /auth/token/ с несуществующим пользователем",
                () -> api.auth.loginWithSpec(loginData, wrongCredentialsLoginResponseSpec)
        );

        step("Проверка сообщения об ошибке", () ->
                assertThat(response.path("detail").toString())
                        .isEqualTo(LOGIN_WRONG_CREDENTIALS_ERROR));
    }

    @Test
    @DisplayName("Негативный: Авторизация с пустыми полями (400 Bad Request)")
    public void loginWithEmptyCredentialsTest() {
        LoginBodyModel loginData = new LoginBodyModel(EMPTY_STRING, EMPTY_STRING);

        var response = step(
                "Отправка POST /auth/token/ с пустыми полями",
                () -> api.auth.loginWithSpec(loginData, loginResponse400Spec)
        );

        step("Проверка ошибки для поля username", () ->
                assertThat(response.path("username[0]").toString())
                        .isEqualTo(FIELD_REQUIRED_ERROR));

        step("Проверка ошибки для поля password", () ->
                assertThat(response.path("password[0]").toString())
                        .isEqualTo(FIELD_REQUIRED_ERROR));
    }
}