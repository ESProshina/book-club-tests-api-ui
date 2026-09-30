package tests.ui;

import io.qameta.allure.*;
import models.registration.RegistrationBodyModel;
import org.junit.jupiter.api.*;
import pages.LoginPage;

import static tests.TestData.*;

@Owner("Elena Black")
@Epic("UI: Авторизация")
@Feature("Логин через UI")
public class LoginUiTests extends UiTestBase {

    LoginPage loginPage = new LoginPage();

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
    @DisplayName("После успешной авторизации отображаются вкладки для авторизованного пользователя")
    @Tags({@Tag("ui"), @Tag("smoke"), @Tag("positive")})
    @Severity(SeverityLevel.CRITICAL)
    public void successfulLoginTest() {
        // Регистрируем пользователя через API (быстро!)
        RegistrationBodyModel regBody = new RegistrationBodyModel(username, password);
        api.users.register(regBody);

        // Логинимся через UI и проверяем главную
        loginPage
                .openLoginPage()
                .setUsername(username)
                .setPassword(password)
                .checkMainPageCondition()
                .checkMainPageHaveProfileButton()
                .checkMainPageHaveClubButton()
                .checkMainPageHaveCreateClubButton();
    }

    @Test
    @DisplayName("После ввода некорректного пароля отображается ошибка авторизации")
    @Tags({@Tag("ui"), @Tag("smoke"), @Tag("negative")})
    @Severity(SeverityLevel.NORMAL)
    public void wrongCredentialsLoginTest() {
        // Регистрируем пользователя через API
        RegistrationBodyModel regBody = new RegistrationBodyModel(username, password);
        api.users.register(regBody);

        // Логинимся через UI с неправильным паролем
        loginPage
                .openLoginPage()
                .setUsername(username)
                .setPassword(wrongPassword)
                .checkWrongCredentialsError();
    }
}