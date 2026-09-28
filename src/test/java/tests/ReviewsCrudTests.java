package tests;

import models.clubs.ClubModel;
import models.clubs.CreateClubBodyModel;
import models.login.LoginBodyModel;
import models.reviews.ReviewBodyModel;
import models.reviews.ReviewPatchBodyModel;
import models.reviews.ReviewResponseModel;
import models.reviews.ReviewsListResponseModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.reviews.ReviewsSpec.*;
import static tests.TestData.*;

public class ReviewsCrudTests extends TestBase {

    private String accessToken;
    private Integer clubId;
    private Integer reviewId;

    @BeforeEach
    public void setup() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);
        accessToken = api.auth.loginAndGetAccessToken(loginData);

        ClubModel club = api.clubs.createClub(accessToken,
                new CreateClubBodyModel(CLUB_BOOK_TITLE, CLUB_BOOK_AUTHORS,
                        CLUB_PUBLICATION_YEAR, CLUB_DESCRIPTION, CLUB_TELEGRAM_LINK));
        clubId = club.id();
    }

    @AfterEach
    public void cleanup() {
        if (reviewId != null) {
            try { api.reviews.deleteReview(accessToken, reviewId); } catch (Exception ignored) {}
            reviewId = null;
        }
        if (clubId != null) {
            try { api.clubs.deleteClub(accessToken, clubId); } catch (Exception ignored) {}
            clubId = null;
        }
    }

    @Test
    @DisplayName("Позитивный: Создание ревью (201 Created)")
    public void createReviewTest() {
        ReviewBodyModel body = new ReviewBodyModel(
                clubId, REVIEW_TEXT, REVIEW_ASSESSMENT, REVIEW_READ_PAGES);

        ReviewResponseModel response = api.reviews.createReview(accessToken, body);
        reviewId = response.id();

        step("Проверка: id > 0", () -> assertThat(response.id()).isPositive());
        step("Проверка: club", () -> assertThat(response.club()).isEqualTo(clubId));
        step("Проверка: user не null", () -> assertThat(response.user()).isNotNull());
        step("Проверка: review", () -> assertThat(response.review()).isEqualTo(REVIEW_TEXT));
        step("Проверка: assessment", () -> assertThat(response.assessment()).isEqualTo(REVIEW_ASSESSMENT));
        step("Проверка: readPages", () -> assertThat(response.readPages()).isEqualTo(REVIEW_READ_PAGES));
    }

    @Test
    @DisplayName("Позитивный: Получение ревью по ID (200 OK)")
    public void getReviewByIdTest() {
        ReviewResponseModel created = api.reviews.createReview(accessToken,
                new ReviewBodyModel(clubId, REVIEW_TEXT, REVIEW_ASSESSMENT, REVIEW_READ_PAGES));
        reviewId = created.id();

        ReviewResponseModel response = api.reviews.getReviewById(reviewId);

        step("Проверка: id", () -> assertThat(response.id()).isEqualTo(reviewId));
        step("Проверка: review", () -> assertThat(response.review()).isEqualTo(REVIEW_TEXT));
    }

    @Test
    @DisplayName("Позитивный: Получение списка ревью с пагинацией (200 OK)")
    public void getReviewsListTest() {
        ReviewResponseModel created = api.reviews.createReview(accessToken,
                new ReviewBodyModel(clubId, REVIEW_TEXT, REVIEW_ASSESSMENT, REVIEW_READ_PAGES));
        reviewId = created.id();

        ReviewsListResponseModel response = api.reviews.getReviews();

        step("Проверка: count > 0", () -> assertThat(response.count()).isPositive());
        step("Проверка: results не пуст", () -> assertThat(response.results()).isNotEmpty());
        step("Проверка: содержит созданное ревью", () ->
                assertThat(response.results().stream().map(ReviewResponseModel::id)).contains(reviewId));
    }

    @Test
    @DisplayName("Позитивный: Полное обновление ревью через PUT (200 OK)")
    public void updateReviewPutTest() {
        ReviewResponseModel created = api.reviews.createReview(accessToken,
                new ReviewBodyModel(clubId, REVIEW_TEXT, REVIEW_ASSESSMENT, REVIEW_READ_PAGES));
        reviewId = created.id();

        ReviewBodyModel updated = new ReviewBodyModel(
                clubId, UPDATED_REVIEW_TEXT, UPDATED_REVIEW_ASSESSMENT, UPDATED_REVIEW_READ_PAGES);

        ReviewResponseModel response = api.reviews.updateReview(accessToken, reviewId, updated);

        step("Проверка: review обновлён", () ->
                assertThat(response.review()).isEqualTo(UPDATED_REVIEW_TEXT));
        step("Проверка: assessment обновлён", () ->
                assertThat(response.assessment()).isEqualTo(UPDATED_REVIEW_ASSESSMENT));
    }

    @Test
    @DisplayName("Позитивный: Частичное обновление ревью через PATCH (200 OK)")
    public void updateReviewPatchTest() {
        ReviewResponseModel created = api.reviews.createReview(accessToken,
                new ReviewBodyModel(clubId, REVIEW_TEXT, REVIEW_ASSESSMENT, REVIEW_READ_PAGES));
        reviewId = created.id();

        // Передаём ВСЕ поля — API требует их наличия даже в PATCH
        ReviewPatchBodyModel patch = new ReviewPatchBodyModel(
                UPDATED_REVIEW_TEXT, UPDATED_REVIEW_ASSESSMENT, UPDATED_REVIEW_READ_PAGES);

        ReviewResponseModel response = api.reviews.patchReview(accessToken, reviewId, patch);

        step("Проверка: review обновлён", () ->
                assertThat(response.review()).isEqualTo(UPDATED_REVIEW_TEXT));
        step("Проверка: assessment обновлён", () ->
                assertThat(response.assessment()).isEqualTo(UPDATED_REVIEW_ASSESSMENT));
        step("Проверка: readPages обновлён", () ->
                assertThat(response.readPages()).isEqualTo(UPDATED_REVIEW_READ_PAGES));
    }

    @Test
    @DisplayName("Негативный: PATCH без assessment и readPages возвращает 400")
    public void patchReviewWithoutRequiredFieldsTest() {
        ReviewResponseModel created = api.reviews.createReview(accessToken,
                new ReviewBodyModel(clubId, REVIEW_TEXT, REVIEW_ASSESSMENT, REVIEW_READ_PAGES));
        reviewId = created.id();

        ReviewPatchBodyModel patch = new ReviewPatchBodyModel(UPDATED_REVIEW_TEXT, null, null);

        var response = api.reviews.patchReviewWithSpec(
                accessToken, reviewId, patch, reviewBadRequestResponseSpec);

        step("Проверка: статус 400", () -> assertThat(response.statusCode()).isEqualTo(400));
        step("Проверка: ошибка про assessment", () ->
                assertThat(response.path("assessment[0]").toString())
                        .isEqualTo("This field may not be null."));
        step("Проверка: ошибка про readPages", () ->
                assertThat(response.path("readPages[0]").toString())
                        .isEqualTo("This field may not be null."));
    }

    @Test
    @DisplayName("Позитивный: Удаление ревью (204 No Content)")
    public void deleteReviewTest() {
        ReviewResponseModel created = api.reviews.createReview(accessToken,
                new ReviewBodyModel(clubId, REVIEW_TEXT, REVIEW_ASSESSMENT, REVIEW_READ_PAGES));
        int id = created.id();

        api.reviews.deleteReview(accessToken, id);

        step("Проверка: GET по удалённому ID возвращает 404", () -> {
            var response = api.reviews.getReviewByIdWithSpec(id, reviewNotFoundResponseSpec);
            assertThat(response.statusCode()).isEqualTo(404);
        });

        reviewId = null;
    }

    @Test
    @DisplayName("Негативный: Создание ревью без токена (401 Unauthorized)")
    public void createReviewWithoutTokenTest() {
        ReviewBodyModel body = new ReviewBodyModel(
                clubId, REVIEW_TEXT, REVIEW_ASSESSMENT, REVIEW_READ_PAGES);

        var response = api.reviews.createReviewWithSpec(null, body, reviewUnauthorizedResponseSpec);

        step("Проверка: статус 401", () -> assertThat(response.statusCode()).isEqualTo(401));
    }

    @Test
    @DisplayName("Негативный: Получение несуществующего ревью (404 Not Found)")
    public void getNonExistentReviewTest() {
        var response = api.reviews.getReviewByIdWithSpec(
                NON_EXISTENT_REVIEW_ID, reviewNotFoundResponseSpec);

        step("Проверка: статус 404", () -> assertThat(response.statusCode()).isEqualTo(404));
    }

    @Test
    @DisplayName("Негативный: Удаление несуществующего ревью (404 Not Found)")
    public void deleteNonExistentReviewTest() {
        var response = api.reviews.deleteReviewWithSpec(
                accessToken, NON_EXISTENT_REVIEW_ID, reviewNotFoundResponseSpec);

        step("Проверка: статус 404", () -> assertThat(response.statusCode()).isEqualTo(404));
    }

    @Test
    @DisplayName("Негативный: Создание ревью с пустым текстом (400 Bad Request)")
    public void createReviewWithEmptyTextTest() {
        ReviewBodyModel body = new ReviewBodyModel(
                clubId, EMPTY_STRING, REVIEW_ASSESSMENT, REVIEW_READ_PAGES);

        var response = api.reviews.createReviewWithSpec(
                accessToken, body, reviewBadRequestResponseSpec);

        step("Проверка: статус 400", () -> assertThat(response.statusCode()).isEqualTo(400));
    }
}
