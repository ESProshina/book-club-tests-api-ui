package tests;

import models.registration.RegistrationBodyModel;
import models.registration.SuccessfulRegistrationResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.registration.RegistrationSpec.registrationResponse400Spec;
import static tests.TestData.*;

public class RegistrationTests extends TestBase {

    String username;
    String password;

    @BeforeEach
    public void prepareTestData() {
        username = "user_" + System.currentTimeMillis();
        password = "pass_" + System.currentTimeMillis();
    }

    @Test
    @DisplayName("Позитивный: Успешная регистрация")
    public void successfulRegistrationTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        SuccessfulRegistrationResponseModel registrationResponse = step(
                "Отправка POST /users/register/ с логином " + username,
                () -> api.users.register(registrationData)
        );

        step("Проверка id пользователя", () ->
                assertThat(registrationResponse.id()).isGreaterThan(0));

        step("Проверка username", () ->
                assertThat(registrationResponse.username()).isEqualTo(username));

        step("Проверка пустого firstName", () ->
                assertThat(registrationResponse.firstName()).isEqualTo(""));

        step("Проверка пустого lastName", () ->
                assertThat(registrationResponse.lastName()).isEqualTo(""));

        step("Проверка пустого email", () ->
                assertThat(registrationResponse.email()).isEqualTo(""));

        step("Проверка формата remoteAddr (IP-адрес)", () ->
                assertThat(registrationResponse.remoteAddr()).matches(REGISTRATION_IP_REGEXP));
    }

    @Test
    @DisplayName("Негативный: Регистрация без пароля (400 Bad Request)")
    public void registrationWithoutPasswordTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, EMPTY_STRING);

        var response = step(
                "Отправка POST /users/register/ без пароля",
                () -> api.users.registerWithSpec(registrationData, registrationResponse400Spec)
        );

        step("Проверка ошибки для поля password", () ->
                assertThat(response.path("password[0]").toString())
                        .isEqualTo(FIELD_REQUIRED_ERROR));
    }

    @Test
    @DisplayName("Негативный: Регистрация без логина (400 Bad Request)")
    public void registrationWithoutUsernameTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(EMPTY_STRING, password);

        var response = step(
                "Отправка POST /users/register/ без логина",
                () -> api.users.registerWithSpec(registrationData, registrationResponse400Spec)
        );

        step("Проверка ошибки для поля username", () ->
                assertThat(response.path("username[0]").toString())
                        .isEqualTo(FIELD_REQUIRED_ERROR));
    }
}