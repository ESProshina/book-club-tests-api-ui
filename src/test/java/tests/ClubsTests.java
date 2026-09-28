package tests;

import models.clubs.ClubModel;
import models.clubs.ClubsListResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

public class ClubsTests extends TestBase {

    @Test
    @DisplayName("GET /clubs возвращает 200 и валидную структуру")
    public void getClubsReturns200AndValidStructure() {
        ClubsListResponseModel response = step(
                "Отправка GET /clubs/",
                () -> api.clubs.getClubs()
        );

        step("Проверка, что ответ не null", () ->
                assertThat(response).isNotNull());

        step("Проверка поля count", () ->
                assertThat(response.count()).isGreaterThanOrEqualTo(0));

        step("Проверка поля results", () ->
                assertThat(response.results()).isNotNull());

        step("Проверка пагинации (results.size() <= count)", () ->
                assertThat(response.results().size()).isLessThanOrEqualTo(response.count()));
    }

    @Test
    @DisplayName("Количество клубов корректно (count и results не противоречат друг другу)")
    public void getClubsCountMatchesResultsSize() {
        ClubsListResponseModel response = step(
                "Отправка GET /clubs/",
                () -> api.clubs.getClubs()
        );

        step("Проверка, что размер results не превышает count", () ->
                assertThat(response.results()).hasSizeLessThanOrEqualTo(response.count()));
    }

    @Test
    @DisplayName("Каждый клуб содержит обязательные поля")
    public void getClubsEachClubHasRequiredFields() {
        ClubsListResponseModel response = step(
                "Отправка GET /clubs/",
                () -> api.clubs.getClubs()
        );

        if (response.results().isEmpty()) {
            step("⚠️ Нет клубов для проверки полей");
            return;
        }

        step("Проверка обязательных полей у " + response.results().size() + " клубов", () -> {
            for (ClubModel club : response.results()) {
                assertThat(club.id()).isNotNull().isPositive();
                assertThat(club.bookTitle()).isNotNull();
                assertThat(club.bookAuthors()).isNotNull();
                assertThat(club.publicationYear()).isNotNull();
                assertThat(club.description()).isNotNull();
                assertThat(club.telegramChatLink()).isNotNull();
                assertThat(club.owner()).isNotNull().isPositive();
                assertThat(club.members()).isNotNull();
                assertThat(club.reviews()).isNotNull();
                assertThat(club.created()).isNotNull();
            }
        });
    }

    @Test
    @DisplayName("Проверка пагинации: count, next, previous, results")
    public void getClubsPaginationFieldsPresent() {
        ClubsListResponseModel response = step(
                "Отправка GET /clubs/",
                () -> api.clubs.getClubs()
        );

        step("Проверка поля count", () ->
                assertThat(response.count()).isNotNull().isGreaterThanOrEqualTo(0));

        step("Проверка поля results", () ->
                assertThat(response.results()).isNotNull());

        step("Проверка поля next (если есть следующая страница)", () -> {
            if (response.results().size() < response.count()) {
                assertThat(response.next()).as("Если есть ещё страницы, next не должен быть null").isNotNull();
            }
        });
    }
}