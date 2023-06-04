import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends RestClient {
private static final String CREATE_USER = "auth/register";

private static final String LOGIN_USER = "auth/login";

public ValidatableResponse create(User user) {
    return given()
            .spec(getBaseSpec())
            .body(user)
            .when()
            .post(CREATE_USER)
            .then();
}

public ValidatableResponse login(UserCredentials userCredentials) {
    return given()
            .spec(getBaseSpec())
            .body(userCredentials)
            .when()
            .post(LOGIN_USER)
            .then();

}

}
