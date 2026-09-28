package specs.user;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.ALL;
import static specs.BaseSpec.baseRequestSpec;

public class UserSpec {

    public static RequestSpecification userRequestSpec = baseRequestSpec;

    public static ResponseSpecification userResponse200Spec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .build();

    public static ResponseSpecification userResponse400Spec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(400)
            .build();

    public static ResponseSpecification userResponse401Spec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(401)
            .build();
}