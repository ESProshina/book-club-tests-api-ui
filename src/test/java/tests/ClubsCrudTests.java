package tests;
import io.qameta.allure.*;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import models.clubs.ClubModel;
import models.clubs.CreateClubBodyModel;
import models.clubs.PatchClubBodyModel;
import models.login.LoginBodyModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.clubs.ClubsSpec.*;
import static tests.TestData.*;

@Owner("Elena Black")
@Epic("Клубы")
@Feature("Управление клубами")
@Story("CRUD операции с клубами")

public class ClubsCrudTests extends TestBase {

    private String accessToken;
    private Integer createdClubId;

    @BeforeEach
    public void auth() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);
        accessToken = api.auth.loginAndGetAccessToken(loginData);
    }

    @AfterEach
    public void cleanup() {
        if (createdClubId != null) {
            try {
                api.clubs.deleteClub(accessToken, createdClubId);
            } catch (Exception ignored) {
            }
            createdClubId = null;
        }
    }

    @Test
    @DisplayName("Позитивный: Создание клуба (201 Created)")
    public void createClubTest() {
        CreateClubBodyModel body = new CreateClubBodyModel(
                CLUB_BOOK_TITLE, CLUB_BOOK_AUTHORS, CLUB_PUBLICATION_YEAR,
                CLUB_DESCRIPTION, CLUB_TELEGRAM_LINK);

        ClubModel response = api.clubs.createClub(accessToken, body);
        createdClubId = response.id();

        step("Проверка: id > 0", () -> assertThat(response.id()).isPositive());
        step("Проверка: bookTitle", () -> assertThat(response.bookTitle()).isEqualTo(CLUB_BOOK_TITLE));
        step("Проверка: bookAuthors", () -> assertThat(response.bookAuthors()).isEqualTo(CLUB_BOOK_AUTHORS));
        step("Проверка: publicationYear", () -> assertThat(response.publicationYear()).isEqualTo(CLUB_PUBLICATION_YEAR));
        step("Проверка: description", () -> assertThat(response.description()).isEqualTo(CLUB_DESCRIPTION));
        step("Проверка: telegramChatLink", () -> assertThat(response.telegramChatLink()).isEqualTo(CLUB_TELEGRAM_LINK));
        step("Проверка: owner != null", () -> assertThat(response.owner()).isNotNull());
        step("Проверка: created != null", () -> assertThat(response.created()).isNotNull());
    }

    @Test
    @DisplayName("Позитивный: Получение клуба по ID (200 OK)")
    public void getClubByIdTest() {
        ClubModel created = api.clubs.createClub(accessToken,
                new CreateClubBodyModel(CLUB_BOOK_TITLE, CLUB_BOOK_AUTHORS,
                        CLUB_PUBLICATION_YEAR, CLUB_DESCRIPTION, CLUB_TELEGRAM_LINK));
        createdClubId = created.id();

        ClubModel response = api.clubs.getClubById(createdClubId);

        step("Проверка: id совпадает", () -> assertThat(response.id()).isEqualTo(createdClubId));
        step("Проверка: bookTitle", () -> assertThat(response.bookTitle()).isEqualTo(CLUB_BOOK_TITLE));
        step("Проверка: description", () -> assertThat(response.description()).isEqualTo(CLUB_DESCRIPTION));
    }

    @Test
    @DisplayName("Позитивный: Полное обновление клуба через PUT (200 OK)")
    public void updateClubPutTest() {
        ClubModel created = api.clubs.createClub(accessToken,
                new CreateClubBodyModel(CLUB_BOOK_TITLE, CLUB_BOOK_AUTHORS,
                        CLUB_PUBLICATION_YEAR, CLUB_DESCRIPTION, CLUB_TELEGRAM_LINK));
        createdClubId = created.id();

        CreateClubBodyModel updated = new CreateClubBodyModel(
                UPDATED_CLUB_BOOK_TITLE, UPDATED_CLUB_BOOK_AUTHORS,
                UPDATED_CLUB_PUBLICATION_YEAR, UPDATED_CLUB_DESCRIPTION,
                UPDATED_CLUB_TELEGRAM_LINK);

        ClubModel response = api.clubs.updateClub(accessToken, createdClubId, updated);

        step("Проверка: bookTitle", () -> assertThat(response.bookTitle()).isEqualTo(UPDATED_CLUB_BOOK_TITLE));
        step("Проверка: bookAuthors", () -> assertThat(response.bookAuthors()).isEqualTo(UPDATED_CLUB_BOOK_AUTHORS));
        step("Проверка: description", () -> assertThat(response.description()).isEqualTo(UPDATED_CLUB_DESCRIPTION));
    }


    @Test
    @DisplayName("Позитивный: Частичное обновление клуба через PATCH (200 OK)")
    public void updateClubPatchTest() {
        ClubModel created = api.clubs.createClub(accessToken,
                new CreateClubBodyModel(CLUB_BOOK_TITLE, CLUB_BOOK_AUTHORS,
                        CLUB_PUBLICATION_YEAR, CLUB_DESCRIPTION, CLUB_TELEGRAM_LINK));
        createdClubId = created.id();

        PatchClubBodyModel patch = new PatchClubBodyModel(
                UPDATED_CLUB_BOOK_TITLE, null, null, null, null);

        ClubModel response = api.clubs.patchClub(accessToken, createdClubId, patch);

        step("Проверка: bookTitle изменился", () ->
                assertThat(response.bookTitle()).isEqualTo(UPDATED_CLUB_BOOK_TITLE));
        step("Проверка: bookAuthors НЕ изменился", () ->
                assertThat(response.bookAuthors()).isEqualTo(CLUB_BOOK_AUTHORS));
        step("Проверка: description НЕ изменился", () ->
                assertThat(response.description()).isEqualTo(CLUB_DESCRIPTION));
    }

    @Test
    @DisplayName("Позитивный: Удаление клуба (204 No Content)")
    public void deleteClubTest() {
        ClubModel created = api.clubs.createClub(accessToken,
                new CreateClubBodyModel(CLUB_BOOK_TITLE, CLUB_BOOK_AUTHORS,
                        CLUB_PUBLICATION_YEAR, CLUB_DESCRIPTION, CLUB_TELEGRAM_LINK));
        int id = created.id();

        api.clubs.deleteClub(accessToken, id);

        step("Проверка: GET по удалённому ID возвращает 404", () -> {
            var response = api.clubs.getClubByIdWithSpec(id, clubNotFoundResponseSpec);
            assertThat(response.statusCode()).isEqualTo(404);
        });

        createdClubId = null;
    }


    @Test
    @DisplayName("Позитивный: Создатель автоматически добавлен в members клуба")
    public void creatorIsAutoAddedToMembersTest() {
        ClubModel created = api.clubs.createClub(accessToken,
                new CreateClubBodyModel(CLUB_BOOK_TITLE, CLUB_BOOK_AUTHORS,
                        CLUB_PUBLICATION_YEAR, CLUB_DESCRIPTION, CLUB_TELEGRAM_LINK));
        createdClubId = created.id();

        step("Проверка: members содержит owner", () -> {
            assertThat(created.members()).isNotNull().isNotEmpty();
            assertThat(created.members()).contains(created.owner());
        });
    }

    @Test
    @DisplayName("Негативный: Повторное вступление в клуб (400 Bad Request)")
    public void joinClubTwiceTest() {
        ClubModel created = api.clubs.createClub(accessToken,
                new CreateClubBodyModel(CLUB_BOOK_TITLE, CLUB_BOOK_AUTHORS,
                        CLUB_PUBLICATION_YEAR, CLUB_DESCRIPTION, CLUB_TELEGRAM_LINK));
        createdClubId = created.id();

        var response = api.clubs.joinClubWithSpec(accessToken, createdClubId, clubBadRequestResponseSpec);

        step("Проверка: статус 400", () -> assertThat(response.statusCode()).isEqualTo(400));
    }

    @Test
    @DisplayName("Негативный: Создание клуба без токена (401 Unauthorized)")
    public void createClubWithoutTokenTest() {
        CreateClubBodyModel body = new CreateClubBodyModel(
                CLUB_BOOK_TITLE, CLUB_BOOK_AUTHORS, CLUB_PUBLICATION_YEAR,
                CLUB_DESCRIPTION, CLUB_TELEGRAM_LINK);

        var response = api.clubs.createClubWithSpec(null, body, clubUnauthorizedResponseSpec);

        step("Проверка: статус 401", () -> assertThat(response.statusCode()).isEqualTo(401));
    }

    @Test
    @DisplayName("Негативный: Создание клуба с пустым bookTitle (400 Bad Request)")
    public void createClubWithEmptyBookTitleTest() {
        CreateClubBodyModel body = new CreateClubBodyModel(
                EMPTY_STRING, CLUB_BOOK_AUTHORS, CLUB_PUBLICATION_YEAR,
                CLUB_DESCRIPTION, CLUB_TELEGRAM_LINK);

        var response = api.clubs.createClubWithSpec(accessToken, body, clubBadRequestResponseSpec);

        step("Проверка: статус 400", () -> assertThat(response.statusCode()).isEqualTo(400));
    }

    @Test
    @DisplayName("Негативный: Получение несуществующего клуба (404 Not Found)")
    public void getNonExistentClubTest() {
        var response = api.clubs.getClubByIdWithSpec(NON_EXISTENT_CLUB_ID, clubNotFoundResponseSpec);

        step("Проверка: статус 404", () -> assertThat(response.statusCode()).isEqualTo(404));
    }

    @Test
    @DisplayName("Негативный: Удаление несуществующего клуба (404 Not Found)")
    public void deleteNonExistentClubTest() {
        var response = api.clubs.deleteClubWithSpec(accessToken, NON_EXISTENT_CLUB_ID, clubNotFoundResponseSpec);

        step("Проверка: статус 404", () -> assertThat(response.statusCode()).isEqualTo(404));
    }
}