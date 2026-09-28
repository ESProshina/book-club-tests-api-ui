package tests;

import models.clubs.ClubModel;
import models.clubs.CreateClubBodyModel;
import models.login.LoginBodyModel;
import models.registration.RegistrationBodyModel;
import models.reviews.ReviewBodyModel;
import models.reviews.ReviewPatchBodyModel;
import models.reviews.ReviewResponseModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.reviews.ReviewsSpec.*;
import static tests.TestData.*;

public class ReviewsRightsTests extends TestBase {

    private String ownerToken;
    private String foreignToken;
    private Integer clubId;
    private Integer reviewId;

    @BeforeEach
    public void setup() {
        ownerToken = api.auth.loginAndGetAccessToken(
                new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD));

        ClubModel club = api.clubs.createClub(ownerToken,
                new CreateClubBodyModel(CLUB_BOOK_TITLE, CLUB_BOOK_AUTHORS,
                        CLUB_PUBLICATION_YEAR, CLUB_DESCRIPTION, CLUB_TELEGRAM_LINK));
        clubId = club.id();

        ReviewResponseModel review = api.reviews.createReview(ownerToken,
                new ReviewBodyModel(clubId, REVIEW_TEXT, REVIEW_ASSESSMENT, REVIEW_READ_PAGES));
        reviewId = review.id();

        String foreignUser = "foreign_" + System.currentTimeMillis();
        api.users.register(new RegistrationBodyModel(foreignUser, "pass123456"));
        foreignToken = api.auth.loginAndGetAccessToken(
                new LoginBodyModel(foreignUser, "pass123456"));
    }

    @AfterEach
    public void cleanup() {
        if (reviewId != null) {
            try { api.reviews.deleteReview(ownerToken, reviewId); } catch (Exception ignored) {}
        }
        if (clubId != null) {
            try { api.clubs.deleteClub(ownerToken, clubId); } catch (Exception ignored) {}
        }
    }

    @Test
    @DisplayName("Негативный: PUT чужого ревью (403 Forbidden)")
    public void updateForeignReviewPutTest() {
        ReviewBodyModel updated = new ReviewBodyModel(clubId, "Hacked!", 1, 1);

        var response = api.reviews.updateReviewWithSpec(
                foreignToken, reviewId, updated, reviewForbiddenResponseSpec);

        step("Проверка: статус 403", () -> assertThat(response.statusCode()).isEqualTo(403));
    }

    @Test
    @DisplayName("Негативный: PATCH чужого ревью (403 Forbidden)")
    public void updateForeignReviewPatchTest() {
        ReviewPatchBodyModel patch = new ReviewPatchBodyModel("Hacked!", null, null);

        var response = api.reviews.patchReviewWithSpec(
                foreignToken, reviewId, patch, reviewForbiddenResponseSpec);

        step("Проверка: статус 403", () -> assertThat(response.statusCode()).isEqualTo(403));
    }

    @Test
    @DisplayName("Негативный: DELETE чужого ревью (403 Forbidden)")
    public void deleteForeignReviewTest() {
        var response = api.reviews.deleteReviewWithSpec(
                foreignToken, reviewId, reviewForbiddenResponseSpec);

        step("Проверка: статус 403", () -> assertThat(response.statusCode()).isEqualTo(403));
    }

    @Test
    @DisplayName("Позитивный: Автор может удалить своё ревью (204)")
    public void ownerCanDeleteOwnReviewTest() {
        var response = api.reviews.deleteReviewWithSpec(
                ownerToken, reviewId, reviewNoContentResponseSpec);

        step("Проверка: статус 204", () -> assertThat(response.statusCode()).isEqualTo(204));
        reviewId = null;
    }

    @Test
    @DisplayName("Негативный: DELETE ревью без токена (401)")
    public void deleteReviewWithoutTokenTest() {
        var response = api.reviews.deleteReviewWithSpec(
                null, reviewId, reviewUnauthorizedResponseSpec);

        step("Проверка: статус 401", () -> assertThat(response.statusCode()).isEqualTo(401));
    }

    @Test
    @DisplayName("Негативный: PUT ревью без токена (401)")
    public void updateReviewWithoutTokenTest() {
        ReviewBodyModel body = new ReviewBodyModel(clubId, "Test", 5, 100);

        var response = api.reviews.updateReviewWithSpec(
                null, reviewId, body, reviewUnauthorizedResponseSpec);

        step("Проверка: статус 401", () -> assertThat(response.statusCode()).isEqualTo(401));
    }
}