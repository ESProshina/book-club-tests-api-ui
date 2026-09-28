package tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import models.clubs.ClubModel;
import models.login.LoginBodyModel;
import models.registration.RegistrationBodyModel;
import models.reviews.ReviewBodyModel;
import models.reviews.ReviewPatchBodyModel;
import models.reviews.ReviewResponseModel;
import org.junit.jupiter.api.*;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.reviews.ReviewsSpec.*;
import static tests.TestData.*;

@Owner("Elena Black")
@Epic("Отзывы на клубы")
@Feature("Права доступа к отзывам")
@Story("Только автор может изменять и удалять свой отзыв")
public class ReviewsRightsTests extends TestBase {

    private String ownerToken;
    private String ownerUsername;
    private String foreignToken;
    private String foreignUsername;
    private Integer clubId;
    private Integer reviewId;

    @BeforeEach
    public void prepareTestData() {
        ownerUsername = faker.name().username() + "_owner_" + System.currentTimeMillis();
        foreignUsername = faker.name().username() + "_foreign_" + System.currentTimeMillis();

         api.users.register(new RegistrationBodyModel(ownerUsername, passwordDef));
        ownerToken = api.auth.login(new LoginBodyModel(ownerUsername, passwordDef)).access();

         ClubModel club = api.clubs.createClub(ownerToken, uniqueClubBody());
        clubId = club.id();
        ReviewResponseModel review = api.reviews.createReview(ownerToken, uniqueReviewBody());
        reviewId = review.id();


        api.users.register(new RegistrationBodyModel(foreignUsername, passwordDef));
        foreignToken = api.auth.login(new LoginBodyModel(foreignUsername, passwordDef)).access();
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

    private models.clubs.CreateClubBodyModel uniqueClubBody() {
        return new models.clubs.CreateClubBodyModel(
                "QA Guru, " + faker.book().title() + "_" + System.currentTimeMillis(),
                faker.book().author(),
                faker.number().numberBetween(2000, 2026),
                faker.lorem().sentence(),
                "https://t.me/" + faker.internet().uuid()
        );
    }

    private ReviewBodyModel uniqueReviewBody() {
        return new ReviewBodyModel(
                clubId,
                faker.lorem().sentence(),
                faker.number().numberBetween(1, 6),
                faker.number().numberBetween(1, 500)
        );
    }


    @Test
    @Description("Попытка изменить чужой отзыв через PUT → 403, отзыв не изменился")
    @DisplayName("Негативный: PUT чужого отзыва запрещён")
    @Tags({@Tag("regression"), @Tag("negative")})
    @Severity(SeverityLevel.CRITICAL)
    public void errorUpdateForeignReviewPutTest() {
        ReviewResponseModel before = api.reviews.getReviewById(reviewId);

        ReviewBodyModel hacked = new ReviewBodyModel(clubId, "Hacked!", 1, 1);

        Response response = step("PUT чужого отзыва", () ->
                api.reviews.updateReviewWithSpec(foreignToken, reviewId, hacked, reviewForbiddenResponseSpec));

        step("Проверка текста ошибки", () ->
                assertThat(response.path("detail").toString()).isEqualTo(errorPermission));

        step("Проверка, что отзыв не изменился", () -> {
            ReviewResponseModel after = api.reviews.getReviewById(reviewId);
            assertThat(after.review()).isEqualTo(before.review());
            assertThat(after.assessment()).isEqualTo(before.assessment());
            assertThat(after.readPages()).isEqualTo(before.readPages());
            assertThat(after.modified()).isNull();  // не должно быть updated
        });
    }


    @Test
    @Description("Попытка изменить чужой отзыв через PATCH → 403")
    @DisplayName("Негативный: PATCH чужого отзыва запрещён")
    @Tags({@Tag("regression"), @Tag("negative")})
    @Severity(SeverityLevel.CRITICAL)
    public void errorUpdateForeignReviewPatchTest() {
        ReviewPatchBodyModel hacked = new ReviewPatchBodyModel("Hacked!", 1, 1);

        Response response = api.reviews.patchReviewWithSpec(
                foreignToken, reviewId, hacked, reviewForbiddenResponseSpec);

        step("Проверка текста ошибки", () ->
                assertThat(response.path("detail").toString()).isEqualTo(errorPermission));
    }


    @Test
    @Description("Попытка удалить чужой отзыв → 403, отзыв существует")
    @DisplayName("Негативный: DELETE чужого отзыва запрещён")
    @Tags({@Tag("regression"), @Tag("negative")})
    @Severity(SeverityLevel.CRITICAL)
    public void errorDeleteForeignReviewTest() {
        Response response = step("DELETE чужого отзыва", () ->
                api.reviews.deleteReviewWithSpec(foreignToken, reviewId, reviewForbiddenResponseSpec));

        step("Проверка текста ошибки", () ->
                assertThat(response.path("detail").toString()).isEqualTo(errorPermission));

        step("Проверка, что отзыв не удалился", () -> {
            ReviewResponseModel after = api.reviews.getReviewById(reviewId);
            assertThat(after).isNotNull();
            assertThat(after.id()).isEqualTo(reviewId);
        });
    }


    @Test
    @Description("Попытка удалить отзыв без токена → 401")
    @DisplayName("Негативный: DELETE без токена")
    @Tags({@Tag("regression"), @Tag("negative")})
    @Severity(SeverityLevel.NORMAL)
    public void errorDeleteReviewWithoutTokenTest() {
        Response response = api.reviews.deleteReviewWithSpec(null, reviewId, reviewUnauthorizedResponseSpec);

        step("Проверка статуса 401", () -> assertThat(response.statusCode()).isEqualTo(401));
    }

    @Test
    @Description("Попытка изменить отзыв без токена → 401")
    @DisplayName("Негативный: PUT без токена")
    @Tags({@Tag("regression"), @Tag("negative")})
    @Severity(SeverityLevel.NORMAL)
    public void errorUpdateReviewWithoutTokenTest() {
        ReviewBodyModel body = new ReviewBodyModel(clubId, "Test", 5, 100);

        Response response = api.reviews.updateReviewWithSpec(null, reviewId, body, reviewUnauthorizedResponseSpec);

        step("Проверка статуса 401", () -> assertThat(response.statusCode()).isEqualTo(401));
    }


    @Test
    @Description("Автор может удалить свой отзыв")
    @DisplayName("Позитивный: автор удаляет свой отзыв (204)")
    @Tags({@Tag("regression"), @Tag("positive")})
    @Severity(SeverityLevel.CRITICAL)
    public void ownerCanDeleteOwnReviewTest() {
        Response response = api.reviews.deleteReviewWithSpec(ownerToken, reviewId, reviewNoContentResponseSpec);

        step("Проверка статуса 204", () -> assertThat(response.statusCode()).isEqualTo(204));
        reviewId = null;
    }
}