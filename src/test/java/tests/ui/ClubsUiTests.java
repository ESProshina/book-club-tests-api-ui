package tests.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.*;
import models.clubs.ClubModel;
import models.clubs.CreateClubBodyModel;
import models.local_storage.AuthModel;
import models.local_storage.UserLocalStorageModel;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.SuccessfulRegistrationResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import pages.ClubInfoPage;

import static io.qameta.allure.Allure.step;
import static tests.TestData.*;

@Owner("Elena Black")
@Epic("UI: Клубы")
@Feature("Просмотр и управление клубом")
public class ClubsUiTests extends UiTestBase {

    ClubInfoPage clubInfoPage = new ClubInfoPage();

    String username;
    String password;

    @BeforeEach
    public void prepareTestData() {
        username = generateUsername();
        password = generatePassword();
    }

    @Test
    @DisplayName("[Гибридный] Владелец не может покинуть свой клуб")
    @Tags({@Tag("ui"), @Tag("smoke"), @Tag("negative")})
    @Severity(SeverityLevel.CRITICAL)
    public void cantLeaveClubAsOwnerTest() throws JsonProcessingException {

        SuccessfulRegistrationResponseModel regResponse = step("Регистрация пользователя через API", () -> {
            RegistrationBodyModel regBody = new RegistrationBodyModel(username, password);
            return api.users.register(regBody);
        });

        SuccessfulLoginResponseModel loginResponse = step("Логин через API", () -> {
            LoginBodyModel loginBody = new LoginBodyModel(username, password);
            return api.auth.login(loginBody);
        });

        String accessToken = loginResponse.access();
        String refreshToken = loginResponse.refresh();

        // Создаём JSON для localStorage
        String localStorageAuthBody = step("Создание JSON авторизации для localStorage", () -> {
            UserLocalStorageModel user = new UserLocalStorageModel(
                    regResponse.id(),
                    regResponse.username(),
                    regResponse.firstName(),
                    regResponse.lastName(),
                    regResponse.email(),
                    regResponse.remoteAddr()
            );
            AuthModel auth = new AuthModel(user, accessToken, refreshToken, true);
            try {
                return new ObjectMapper().writeValueAsString(auth);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });

        ClubModel club = step("Создание клуба через API", () -> {
            CreateClubBodyModel body = new CreateClubBodyModel(
                    generateBookTitle(),
                    generateBookAuthors(),
                    generatePublicationYear(),
                    generateDescription(),
                    generateTelegramChatLink()
            );
            return api.clubs.createClub(accessToken, body);
        });

        int clubId = club.id();

        // === UI ПРОВЕРКА ===
        step("Открытие страницы клуба с авторизацией из localStorage", () ->
                clubInfoPage
                        .openPage()
                        .putAuthIntoLocalStorage(localStorageAuthBody)
                        .openClubInfoPage(clubId)
        );

        step("UI: Клик Покинуть клуб и проверка ошибки", () ->
                clubInfoPage
                        .checkClubInfo()
                        .clickLeaveBtn()
                        .confirmLeaveClub()
                        .checkLeaveClubError()
        );
    }
}
