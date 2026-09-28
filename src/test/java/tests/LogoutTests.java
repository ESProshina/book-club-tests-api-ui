package tests;

import models.login.LoginBodyModel;
import models.logout.LogoutBodyModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.logout.LogoutSpec.logoutResponse400Spec;
import static specs.logout.LogoutSpec.logoutResponse401Spec;
import static tests.TestData.*;

public class LogoutTests extends TestBase {

    @Test
    @DisplayName("Позитивный: Успешный разлогин с валидным refresh-токеном")
    public void successfulLogoutTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);

        String refreshToken = step(
                "Получение refresh-токена через логин",
                () -> api.auth.loginAndGetRefreshToken(loginData)
        );

        LogoutBodyModel logoutBody = new LogoutBodyModel(refreshToken);

        step("Отправка POST /auth/logout/ с валидным refresh-токеном", () ->
                api.auth.logout(logoutBody));
    }

    @Test
    @DisplayName("Негативный: Разлогин с недействительным refresh-токеном (401 Unauthorized)")
    public void logoutWithInvalidTokenTest() {
        LogoutBodyModel logoutBody = new LogoutBodyModel(INVALID_REFRESH_TOKEN);

        var response = step(
                "Отправка POST /auth/logout/ с невалидным токеном",
                () -> api.auth.logoutWithSpec(logoutBody, logoutResponse401Spec)
        );

        step("Проверка сообщения об ошибке", () ->
                assertThat(response.path("detail").toString())
                        .isEqualTo(INVALID_TOKEN_ERROR));

        step("Проверка кода ошибки", () ->
                assertThat(response.path("code").toString())
                        .isEqualTo(INVALID_TOKEN_CODE));
    }

    @Test
    @DisplayName("Негативный: Разлогин с пустым refresh-токеном (400 Bad Request)")
    public void logoutWithEmptyTokenTest() {
        LogoutBodyModel logoutBody = new LogoutBodyModel(EMPTY_STRING);

        var response = step(
                "Отправка POST /auth/logout/ с пустым токеном",
                () -> api.auth.logoutWithSpec(logoutBody, logoutResponse400Spec)
        );

        step("Проверка ошибки для поля refresh", () ->
                assertThat(response.path("refresh[0]").toString())
                        .isEqualTo(FIELD_REQUIRED_ERROR));
    }
}