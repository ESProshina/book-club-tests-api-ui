package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import models.clubs.ClubModel;
import models.clubs.ClubsListResponseModel;
import models.clubs.CreateClubBodyModel;
import models.clubs.PatchClubBodyModel;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.baseRequestSpec;
import static specs.clubs.ClubsSpec.*;

public class ClubsApiClient {


    @Step("Получение списка клубов GET /clubs/")
    public ClubsListResponseModel getClubs() {
        return given(baseRequestSpec)
                .when()
                .get("/clubs/")
                .then()
                .spec(successfulClubsListResponseSpec)
                .extract()
                .as(ClubsListResponseModel.class);
    }

    @Step("Получение клуба по ID GET /clubs/{id}/")
    public ClubModel getClubById(int id) {
        return given(baseRequestSpec)
                .pathParam("id", id)
                .when()
                .get("/clubs/{id}/")
                .then()
                .spec(clubResponse200Spec)
                .extract()
                .as(ClubModel.class);
    }

    @Step("Получение клуба по ID со спецификацией")
    public Response getClubByIdWithSpec(int id, ResponseSpecification spec) {
        return given(baseRequestSpec)
                .pathParam("id", id)
                .when()
                .get("/clubs/{id}/")
                .then()
                .spec(spec)
                .extract()
                .response();
    }

    @Step("Создание клуба POST /clubs/")
    public ClubModel createClub(String accessToken, CreateClubBodyModel body) {
        return given(baseRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(body)
                .when()
                .post("/clubs/")
                .then()
                .spec(clubCreatedResponseSpec)
                .extract()
                .as(ClubModel.class);
    }

    @Step("Создание клуба со спецификацией")
    public Response createClubWithSpec(String accessToken, CreateClubBodyModel body, ResponseSpecification spec) {
        var request = given(baseRequestSpec).body(body);
        if (accessToken != null && !accessToken.isEmpty()) {
            request.header("Authorization", "Bearer " + accessToken);
        }
        return request
                .when()
                .post("/clubs/")
                .then()
                .spec(spec)
                .extract()
                .response();
    }

    @Step("Полное обновление клуба PUT /clubs/{id}/")
    public ClubModel updateClub(String accessToken, int id, CreateClubBodyModel body) {
        return given(baseRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("id", id)
                .body(body)
                .when()
                .put("/clubs/{id}/")
                .then()
                .spec(clubResponse200Spec)
                .extract()
                .as(ClubModel.class);
    }

    @Step("Частичное обновление клуба PATCH /clubs/{id}/")
    public ClubModel patchClub(String accessToken, int id, PatchClubBodyModel body) {
        return given(baseRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("id", id)
                .body(body)
                .when()
                .patch("/clubs/{id}/")
                .then()
                .spec(clubResponse200Spec)
                .extract()
                .as(ClubModel.class);
    }

    @Step("Обновление клуба (PUT) со спецификацией")
    public Response updateClubWithSpec(String accessToken, int id, CreateClubBodyModel body, ResponseSpecification spec) {
        var request = given(baseRequestSpec).pathParam("id", id).body(body);
        if (accessToken != null && !accessToken.isEmpty()) {
            request.header("Authorization", "Bearer " + accessToken);
        }
        return request
                .when()
                .put("/clubs/{id}/")
                .then()
                .spec(spec)
                .extract()
                .response();
    }

    @Step("Обновление клуба (PATCH) со спецификацией")
    public Response patchClubWithSpec(String accessToken, int id, PatchClubBodyModel body, ResponseSpecification spec) {
        var request = given(baseRequestSpec).pathParam("id", id).body(body);
        if (accessToken != null && !accessToken.isEmpty()) {
            request.header("Authorization", "Bearer " + accessToken);
        }
        return request
                .when()
                .patch("/clubs/{id}/")
                .then()
                .spec(spec)
                .extract()
                .response();
    }

    @Step("Удаление клуба DELETE /clubs/{id}/")
    public void deleteClub(String accessToken, int id) {
        given(baseRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("id", id)
                .when()
                .delete("/clubs/{id}/")
                .then()
                .spec(clubNoContentResponseSpec);
    }

    @Step("Удаление клуба со спецификацией")
    public Response deleteClubWithSpec(String accessToken, int id, ResponseSpecification spec) {
        var request = given(baseRequestSpec).pathParam("id", id);
        if (accessToken != null && !accessToken.isEmpty()) {
            request.header("Authorization", "Bearer " + accessToken);
        }
        return request
                .when()
                .delete("/clubs/{id}/")
                .then()
                .spec(spec)
                .extract()
                .response();
    }

    // ==================== MEMBERS ====================

    @Step("Вступление в клуб POST /clubs/{id}/members/me/")
    public void joinClub(String accessToken, int id) {
        given(baseRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("id", id)
                .when()
                .post("/clubs/{id}/members/me/")
                .then()
                .spec(clubNoContentResponseSpec);
    }

    @Step("Вступление в клуб со спецификацией")
    public Response joinClubWithSpec(String accessToken, int id, ResponseSpecification spec) {
        var request = given(baseRequestSpec).pathParam("id", id);
        if (accessToken != null && !accessToken.isEmpty()) {
            request.header("Authorization", "Bearer " + accessToken);
        }
        return request
                .when()
                .post("/clubs/{id}/members/me/")
                .then()
                .spec(spec)
                .extract()
                .response();
    }

    @Step("Выход из клуба DELETE /clubs/{id}/members/me/")
    public void leaveClub(String accessToken, int id) {
        given(baseRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("id", id)
                .when()
                .delete("/clubs/{id}/members/me/")
                .then()
                .spec(clubNoContentResponseSpec);
    }

    @Step("Выход из клуба со спецификацией")
    public Response leaveClubWithSpec(String accessToken, int id, ResponseSpecification spec) {
        var request = given(baseRequestSpec).pathParam("id", id);
        if (accessToken != null && !accessToken.isEmpty()) {
            request.header("Authorization", "Bearer " + accessToken);
        }
        return request
                .when()
                .delete("/clubs/{id}/members/me/")
                .then()
                .spec(spec)
                .extract()
                .response();
    }
}