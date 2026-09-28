package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import models.reviews.*;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.baseRequestSpec;
import static specs.reviews.ReviewsSpec.*;

public class ReviewsApiClient {

    @Step("Получение всех ревью GET /clubs/reviews/")
    public ReviewsListResponseModel getReviews() {
        return given(baseRequestSpec)
                .when()
                .get("/clubs/reviews/")
                .then()
                .spec(reviewsListResponse200Spec)
                .extract()
                .as(ReviewsListResponseModel.class);
    }

    @Step("Получение ревью по ID GET /clubs/reviews/{id}/")
    public ReviewResponseModel getReviewById(int id) {
        return given(baseRequestSpec)
                .pathParam("id", id)
                .when()
                .get("/clubs/reviews/{id}/")
                .then()
                .spec(reviewResponse200Spec)
                .extract()
                .as(ReviewResponseModel.class);
    }

    @Step("Получение ревью по ID со спецификацией")
    public Response getReviewByIdWithSpec(int id, ResponseSpecification spec) {
        return given(baseRequestSpec)
                .pathParam("id", id)
                .when()
                .get("/clubs/reviews/{id}/")
                .then()
                .spec(spec)
                .extract()
                .response();
    }


    @Step("Создание ревью POST /clubs/reviews/")
    public ReviewResponseModel createReview(String accessToken, ReviewBodyModel body) {
        return given(baseRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(body)
                .when()
                .post("/clubs/reviews/")
                .then()
                .spec(reviewCreatedResponseSpec)
                .extract()
                .as(ReviewResponseModel.class);
    }

    @Step("Создание ревью со спецификацией")
    public Response createReviewWithSpec(String accessToken, ReviewBodyModel body,
                                         ResponseSpecification spec) {
        var request = given(baseRequestSpec).body(body);
        if (accessToken != null && !accessToken.isEmpty()) {
            request.header("Authorization", "Bearer " + accessToken);
        }
        return request
                .when()
                .post("/clubs/reviews/")
                .then()
                .spec(spec)
                .extract()
                .response();
    }

    @Step("Создание ревью без оценки со спецификацией")
    public Response createReviewWithSpec(String accessToken, ReviewNotRatingBodyModel body,
                                         ResponseSpecification spec) {
        var request = given(baseRequestSpec).body(body);
        if (accessToken != null && !accessToken.isEmpty()) {
            request.header("Authorization", "Bearer " + accessToken);
        }
        return request
                .when()
                .post("/clubs/reviews/")
                .then()
                .spec(spec)
                .extract()
                .response();
    }


    @Step("Полное обновление ревью PUT /clubs/reviews/{id}/")
    public ReviewResponseModel updateReview(String accessToken, int id, ReviewBodyModel body) {
        return given(baseRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("id", id)
                .body(body)
                .when()
                .put("/clubs/reviews/{id}/")
                .then()
                .spec(reviewResponse200Spec)
                .extract()
                .as(ReviewResponseModel.class);
    }

    @Step("Частичное обновление ревью PATCH /clubs/reviews/{id}/")
    public ReviewResponseModel patchReview(String accessToken, int id, ReviewPatchBodyModel body) {
        return given(baseRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("id", id)
                .body(body)
                .when()
                .patch("/clubs/reviews/{id}/")
                .then()
                .spec(reviewResponse200Spec)
                .extract()
                .as(ReviewResponseModel.class);
    }

    @Step("Обновление ревью (PUT) со спецификацией")
    public Response updateReviewWithSpec(String accessToken, int id, ReviewBodyModel body,
                                         ResponseSpecification spec) {
        var request = given(baseRequestSpec).pathParam("id", id).body(body);
        if (accessToken != null && !accessToken.isEmpty()) {
            request.header("Authorization", "Bearer " + accessToken);
        }
        return request
                .when()
                .put("/clubs/reviews/{id}/")
                .then()
                .spec(spec)
                .extract()
                .response();
    }

    @Step("Обновление ревью (PATCH) со спецификацией")
    public Response patchReviewWithSpec(String accessToken, int id, ReviewPatchBodyModel body,
                                        ResponseSpecification spec) {
        var request = given(baseRequestSpec).pathParam("id", id).body(body);
        if (accessToken != null && !accessToken.isEmpty()) {
            request.header("Authorization", "Bearer " + accessToken);
        }
        return request
                .when()
                .patch("/clubs/reviews/{id}/")
                .then()
                .spec(spec)
                .extract()
                .response();
    }


    @Step("Удаление ревью DELETE /clubs/reviews/{id}/")
    public void deleteReview(String accessToken, int id) {
        given(baseRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("id", id)
                .when()
                .delete("/clubs/reviews/{id}/")
                .then()
                .spec(reviewNoContentResponseSpec);
    }

    @Step("Удаление ревью со спецификацией")
    public Response deleteReviewWithSpec(String accessToken, int id, ResponseSpecification spec) {
        var request = given(baseRequestSpec).pathParam("id", id);
        if (accessToken != null && !accessToken.isEmpty()) {
            request.header("Authorization", "Bearer " + accessToken);
        }
        return request
                .when()
                .delete("/clubs/reviews/{id}/")
                .then()
                .spec(spec)
                .extract()
                .response();
    }
}