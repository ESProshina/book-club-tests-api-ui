package tests;

import api.ApiClient;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

import static specs.BaseSpec.baseRequestSpec;

public class TestBase {

    protected static ApiClient api;

    @BeforeAll
    public static void setUp() {
        RestAssured.requestSpecification = baseRequestSpec;
        api = new ApiClient();
    }
}