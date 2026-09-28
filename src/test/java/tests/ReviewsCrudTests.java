package tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import models.clubs.ClubModel;
import models.clubs.CreateClubBodyModel;
import models.login.LoginBodyModel;
import models.registration.RegistrationBodyModel;
import models.reviews.*;
import org.junit.jupiter.api.*;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.reviews.ReviewsSpec.*;
import static tests.TestData.*;


@Owner("Elena Black")
@Epic("Отзывы на клубы")
@Feature("Управление отзывами пользователя")
@Story("Создание, чтение, обновление и удаление отзывов")
public class ReviewsCrudTests extends TestBase {

    private String username;
    private String accessToken;
    private Integer createdClubId;

    @BeforeEach
    public void prepareTestData() {
        username = faker.name().username() + "_" + System.currentTimeMillis();

        api.users.register(new RegistrationBodyModel(username, passwordDef));
        accessToken = api.auth.login(new LoginBodyModel(username, passwordDef)).access();

        ClubModel createdClub = api.clubs.createClub(accessToken, uniqueClubBody());
        createdClubId = createdClub.id();
    }

    @AfterEach
    public void cleanup() {
        if (createdClubId != null) {
            step("Очистка: удаление клуба id=" + createdClubId, () ->
                    api.clubs.deleteClub(accessToken, createdClubId));
            createdClubId = null;
        }
    }

    private CreateClubBodyModel uniqueClubBody() {
        return new CreateClubBodyModel(
                "QA Guru, " + faker.book().title() + "_" + System.currentTimeMillis(),
                faker.book().author(),
                faker.number().numberBetween(2000, 2026),
                faker.lorem().sentence(),
                "https://t.me/" + faker.internet().uuid()
        );
    }

    private ReviewBodyModel uniqueReviewBody() {
        return new ReviewBodyModel(
                createdClubId,
                faker.lorem().sentence(),
                faker.number().numberBetween(1, 6),
                faker.number().numberBetween(1, 500)
        );
    }

    @Test
    @Description("Успешное создание отзыва на созданный клуб с проверкой всех полей")
    @DisplayName("Успешное создание отзыва (201)")
    @Tags({@Tag("regression"), @Tag("smoke"), @Tag("positive")})
    @Severity(SeverityLevel.CRITICAL)
    public void successfulCreateReviewTest() {
        ReviewBodyModel reviewData = uniqueReviewBody();

        ReviewResponseModel createdReview = step("Создание отзыва через API", () ->
                api.reviews.createReview(accessToken, reviewData));

        step("Проверка полей созданного отзыва", () -> {
            assertThat(createdReview.id()).isPositive();
            assertThat(createdReview.club()).isEqualTo(createdClubId);
            assertThat(createdReview.review()).isEqualTo(reviewData.review());
            assertThat(createdReview.assessment()).isEqualTo(reviewData.assessment());
            assertThat(createdReview.readPages()).isEqualTo(reviewData.readPages());
            assertThat(createdReview.created()).isNotNull();
            assertThat(createdReview.user()).isNotNull();
            assertThat(createdReview.user().username()).isEqualTo(username);
        });
    }

    @Test
    @Description("Получение отзыва по id с проверкой всех полей")
    @DisplayName("Успешное получение отзыва по id (200)")
    @Tags({@Tag("regression"), @Tag("smoke"), @Tag("positive")})
    @Severity(SeverityLevel.CRITICAL)
    public void successfulGetReviewByIdTest() {
        ReviewResponseModel created = api.reviews.createReview(accessToken, uniqueReviewBody());
        int reviewId = created.id();

        ReviewResponseModel fetched = api.reviews.getReviewById(reviewId);

        step("Проверка полей отзыва", () -> {
            assertThat(fetched.id()).isEqualTo(reviewId);
            assertThat(fetched.club()).isEqualTo(createdClubId);
            assertThat(fetched.review()).isEqualTo(created.review());
            assertThat(fetched.assessment()).isEqualTo(created.assessment());
            assertThat(fetched.readPages()).isEqualTo(created.readPages());
            assertThat(fetched.user()).isNotNull();
            assertThat(fetched.user().username()).isEqualTo(username);
        });
    }

    @Test
    @Description("Получение списка отзывов с пагинацией")
    @DisplayName("Успешное получение списка отзывов (200)")
    @Tags({@Tag("regression"), @Tag("smoke"), @Tag("positive")})
    @Severity(SeverityLevel.CRITICAL)
    public void successfulGetReviewsListTest() {
        ReviewResponseModel created = api.reviews.createReview(accessToken, uniqueReviewBody());

        ReviewsListResponseModel response = api.reviews.getReviews();

        step("Проверка пагинации и полей", () -> {
            assertThat(response).isNotNull();
            assertThat(response.count()).isGreaterThanOrEqualTo(0);
            assertThat(response.results()).isNotNull();
            assertThat(response.results().stream().map(ReviewResponseModel::id))
                    .contains(created.id());
        });
    }


    @Test
    @Description("Полное обновление отзыва через PUT")
    @DisplayName("Успешное полное обновление отзыва (200)")
    @Tags({@Tag("regression"), @Tag("positive")})
    @Severity(SeverityLevel.CRITICAL)
    public void successfulPutReviewTest() {
        ReviewResponseModel created = api.reviews.createReview(accessToken, uniqueReviewBody());
        int reviewId = created.id();

        ReviewBodyModel updateData = new ReviewBodyModel(
                createdClubId,
                faker.lorem().sentence(),
                faker.number().numberBetween(1, 6),
                faker.number().numberBetween(1, 500)
        );

        ReviewResponseModel updated = api.reviews.updateReview(accessToken, reviewId, updateData);

        step("Проверка обновления", () -> {
            assertThat(updated.id()).isEqualTo(reviewId);
            assertThat(updated.review()).isEqualTo(updateData.review()).isNotEqualTo(created.review());
            assertThat(updated.assessment()).isEqualTo(updateData.assessment());
            assertThat(updated.readPages()).isEqualTo(updateData.readPages());
            assertThat(updated.user().username()).isEqualTo(username);
        });
    }


    @Test
    @Description("Частичное обновление отзыва через PATCH")
    @DisplayName("Успешное частичное обновление отзыва (200)")
    @Tags({@Tag("regression"), @Tag("positive")})
    @Severity(SeverityLevel.CRITICAL)
    public void successfulPatchReviewTest() {
        ReviewResponseModel created = api.reviews.createReview(accessToken, uniqueReviewBody());
        int reviewId = created.id();

        ReviewPatchBodyModel patch = new ReviewPatchBodyModel(
                faker.lorem().sentence(),
                faker.number().numberBetween(1, 6),
                faker.number().numberBetween(1, 500)
        );

        ReviewResponseModel updated = api.reviews.patchReview(accessToken, reviewId, patch);

        step("Проверка обновления", () -> {
            assertThat(updated.id()).isEqualTo(reviewId);
            assertThat(updated.review()).isEqualTo(patch.review());
            assertThat(updated.assessment()).isEqualTo(patch.assessment());
            assertThat(updated.readPages()).isEqualTo(patch.readPages());
        });
    }

    @Test
    @Description("Удаление отзыва и проверка, что он не находится по ID")
    @DisplayName("Успешное удаление отзыва (204)")
    @Tags({@Tag("regression"), @Tag("smoke"), @Tag("positive")})
    @Severity(SeverityLevel.CRITICAL)
    public void successfulDeleteReviewTest() {
        ReviewResponseModel created = api.reviews.createReview(accessToken, uniqueReviewBody());
        int reviewId = created.id();

        api.reviews.deleteReview(accessToken, reviewId);

        step("Проверка, что отзыв удалён (404)", () -> {
            var response = api.reviews.getReviewByIdWithSpec(reviewId, reviewNotFoundResponseSpec);
            assertThat(response.path("detail").toString()).isEqualTo(errorBookReview);
        });
    }

    @Test
    @Description("Создание отзыва без обязательного поля assessment → 400")
    @DisplayName("Негативный: создание отзыва без assessment")
    @Tags({@Tag("regression"), @Tag("negative")})
    @Severity(SeverityLevel.NORMAL)
    public void errorCreateReviewWithoutAssessmentTest() {
        ReviewNotRatingBodyModel body = new ReviewNotRatingBodyModel(
                createdClubId,
                faker.lorem().sentence(),
                faker.number().numberBetween(1, 500)
        );

        Response response = api.reviews.createReviewWithSpec(accessToken, body, reviewBadRequestResponseSpec);

        step("Проверка сообщения об ошибке", () ->
                assertThat(response.path("assessment[0]").toString()).isEqualTo(errorAssessmentRequired));
    }

    @Test
    @Description("Создание отзыва без токена → 401")
    @DisplayName("Негативный: создание отзыва без токена")
    @Tags({@Tag("regression"), @Tag("negative")})
    @Severity(SeverityLevel.NORMAL)
    public void errorCreateReviewWithoutTokenTest() {
        ReviewBodyModel body = uniqueReviewBody();

        Response response = api.reviews.createReviewWithSpec(null, body, reviewUnauthorizedResponseSpec);

        step("Проверка статуса 401", () -> assertThat(response.statusCode()).isEqualTo(401));
    }

    @Test
    @Description("Получение несуществующего отзыва → 404")
    @DisplayName("Негативный: получение несуществующего отзыва")
    @Tags({@Tag("regression"), @Tag("negative")})
    @Severity(SeverityLevel.NORMAL)
    public void errorGetReviewByIdTest() {
        int invalidReviewId = 999_999_999;

        Response response = api.reviews.getReviewByIdWithSpec(invalidReviewId, reviewNotFoundResponseSpec);

        step("Проверка текста ошибки", () ->
                assertThat(response.path("detail").toString()).isEqualTo(errorBookReview));
    }

    @Test
    @Description("Создание отзыва с assessment = null → 400")
    @DisplayName("Негативный: создание отзыва с assessment = null")
    @Tags({@Tag("regression"), @Tag("negative")})
    @Severity(SeverityLevel.NORMAL)
    public void errorCreateReviewWithNullAssessmentTest() {
        ReviewBodyModel body = new ReviewBodyModel(
                createdClubId,
                faker.lorem().sentence(),
                null,                              // ← явно null
                faker.number().numberBetween(1, 500)
        );

        Response response = api.reviews.createReviewWithSpec(accessToken, body, reviewBadRequestResponseSpec);

        step("Проверка сообщения об ошибке", () ->
                assertThat(response.path("assessment[0]").toString()).isEqualTo(errorAssessment));
    }
}