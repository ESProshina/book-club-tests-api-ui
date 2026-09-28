package specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static helpers.ConfigReader.getBasePath;
import static helpers.ConfigReader.getBaseUrl;
import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.http.ContentType.JSON;

public class BaseSpec {

    public static final RequestSpecification baseRequestSpec = new RequestSpecBuilder()
            .setBaseUri(getBaseUrl())
            .setBasePath(getBasePath())
            .setContentType(JSON)
            .addFilter(withCustomTemplates())
            .build();
}