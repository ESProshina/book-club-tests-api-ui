package tests.ui;

import io.qameta.allure.*;
import models.registration.RegistrationBodyModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import pages.RegistrationPage;

import static tests.TestData.*;

@Owner("Elena Black")
@Epic("UI: Авторизация")
@Feature("Регистрация через UI")
public class RegistrationUiTests extends UiTestBase {

    RegistrationPage registrationPage = new RegistrationPage();

    String username;
    String password;
    String wrongPassword;

    @BeforeEach
    public void prepareTestData() {
        username = generateUsername();
        password = generatePassword();
        wrongPassword = generateWrongPassword();
    }

    @Test
    @DisplayName("После успешной регистрации происходит автоматическая авторизация")
    @Tags({@Tag("ui"), @Tag("smoke"), @Tag("positive")})
    @Severity(SeverityLevel.CRITICAL)
    public void successfulRegistrationTest() {
        registrationPage
                .openRegistrationPage()
                .setUsername(username)
                .setPassword(password)
                .setTheSamePasswordAndConfirm(password)
                .checkMainPageCondition()
                .checkMainPageHaveProfileButton()
                .checkMainPageHaveClubButton()
                .checkMainPageHaveCreateClubButton();
    }

    @Test
    @DisplayName("Ввод некорректного пароля в подтверждении отображает ошибку")
    @Tags({@Tag("ui"), @Tag("negative")})
    @Severity(SeverityLevel.NORMAL)
    public void mismatchPasswordTest() {
        registrationPage
                .openRegistrationPage()
                .setUsername(username)
                .setPassword(password)
                .setNotTheSamePasswordAndConfirm(wrongPassword)
                .checkMismatchPasswordError();
    }

    @Test
    @DisplayName("Повторная регистрация существующего пользователя отображает ошибку")
    @Tags({@Tag("ui"), @Tag("negative")})
    @Severity(SeverityLevel.NORMAL)
    public void existingUserRegistrationTest() {
        RegistrationBodyModel regBody = new RegistrationBodyModel(username, password);
        api.users.register(regBody);

        registrationPage
                .openRegistrationPage()
                .setUsername(regBody.username())
                .setPassword(regBody.password())
                .setTheSamePasswordAndConfirm(regBody.password())
                .checkSameCredentialsError();
    }
}