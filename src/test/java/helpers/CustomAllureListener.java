package helpers;

import io.qameta.allure.restassured.AllureRestAssured;

public class CustomAllureListener {

    private CustomAllureListener() {
    }

    public static AllureRestAssured withCustomTemplates() {
        return new AllureRestAssured()
                .setRequestTemplate("request.ftl")
                .setResponseTemplate("response.ftl");
    }
}