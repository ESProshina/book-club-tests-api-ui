package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import models.registration.RegistrationBodyModel;
import models.registration.SuccessfulRegistrationResponseModel;
import models.user.UpdateUserBodyModel;
import models.user.UserResponseModel;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.baseRequestSpec;
import static specs.registration.RegistrationSpec.registrationResponse201Spec;
import static specs.user.UserSpec.userResponse200Spec;

public class UsersApiClient {

    @Step("Регистрация пользователя")
    public SuccessfulRegistrationResponseModel register(RegistrationBodyModel registrationBody) {
        return given(baseRequestSpec)
                .body(registrationBody)
                .when()
                .post("/users/register/")
                .then()
                .spec(registrationResponse201Spec)
                .extract()
                .as(SuccessfulRegistrationResponseModel.class);
    }

    @Step("Регистрация пользователя со спецификацией")
    public Response registerWithSpec(RegistrationBodyModel registrationBody, ResponseSpecification spec) {
        return given(baseRequestSpec)
                .body(registrationBody)
                .when()
                .post("/users/register/")
                .then()
                .spec(spec)
                .extract()
                .response();
    }

    @Step("Обновление данных пользователя")
    public UserResponseModel updateUser(String accessToken, UpdateUserBodyModel updateData) {
        return given(baseRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(updateData)
                .when()
                .patch("/users/me/")
                .then()
                .spec(userResponse200Spec)
                .extract()
                .as(UserResponseModel.class);
    }

    @Step("Обновление данных пользователя со спецификацией")
    public Response updateUserWithSpec(String accessToken, UpdateUserBodyModel updateData, ResponseSpecification spec) {
        var request = given(baseRequestSpec)
                .body(updateData);

        // Добавляем заголовок только если токен не null и не пустой
        if (accessToken != null && !accessToken.isEmpty()) {
            request.header("Authorization", "Bearer " + accessToken);
        }

        return request
                .when()
                .patch("/users/me/")
                .then()
                .spec(spec)
                .extract()
                .response();
    }

    @Step("Получение текущего пользователя")
    public UserResponseModel getCurrentUser(String accessToken) {
        return given(baseRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .get("/users/me/")
                .then()
                .spec(userResponse200Spec)
                .extract()
                .as(UserResponseModel.class);
    }
}