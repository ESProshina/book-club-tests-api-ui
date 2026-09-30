package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static tests.TestData.*;

public class LoginPage {

    private final SelenideElement usernameInput = $("[data-testid=username-input]");
    private final SelenideElement passwordInput = $("[data-testid=password-input]");
    private final SelenideElement mainPage = $(".clubs-page");
    private final SelenideElement profileBtn = $("[data-testid=profile-link]");
    private final SelenideElement clubBtn = $("[data-testid=clubs-link]");
    private final SelenideElement createClubBtn = $("[data-testid=create-club-link]");
    private final SelenideElement wrongCredentialsError = $("[data-testid=login-container]");

    @Step("Открыть страницу авторизации")
    public LoginPage openLoginPage() {
        open("/signin");
        return this;
    }

    @Step("Заполнить поле username")
    public LoginPage setUsername(String value) {
        usernameInput.shouldBe(visible).setValue(value);
        return this;
    }

    @Step("Заполнить поле password")
    public LoginPage setPassword(String value) {
        passwordInput.shouldBe(visible).setValue(value).pressEnter();
        return this;
    }

    @Step("Отображается главная страница")
    public LoginPage checkMainPageCondition() {
        mainPage.shouldBe(visible);
        return this;
    }

    @Step("На главной странице отображается кнопка Профиль")
    public LoginPage checkMainPageHaveProfileButton() {
        profileBtn.shouldHave(text(EXPECTED_PROFILE_BTN_NAME));
        return this;
    }

    @Step("На главной странице отображается кнопка Клубы")
    public LoginPage checkMainPageHaveClubButton() {
        clubBtn.shouldHave(text(EXPECTED_CLUB_BTN_NAME));
        return this;
    }

    @Step("На главной странице отображается кнопка Создать клуб")
    public LoginPage checkMainPageHaveCreateClubButton() {
        createClubBtn.shouldHave(text(EXPECTED_CREATE_CLUB_BTN_NAME));
        return this;
    }

    @Step("Отображается ошибка авторизации")
    public LoginPage checkWrongCredentialsError() {
        wrongCredentialsError.shouldBe(visible)
                .shouldHave(text(EXPECTED_WRONG_CREDENTIALS_ERROR_MESSAGE));
        return this;
    }
}