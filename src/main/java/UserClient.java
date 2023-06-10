import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends RestClient {
private static final String CREATE_USER = "auth/register";

private static final String LOGIN_USER = "auth/login";

private static final String DELETE_USER = "auth/user";

private static final String CHANGE_USERS_DATA = "auth/user";
private static final String LOGOUT_USER = "auth/logout";

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

public ValidatableResponse deleteUser(String authToken) {
    return given()
            .spec(getBaseSpec())
            .header("Authorization", authToken)
            .when()
            .delete(DELETE_USER)
            .then();
}

public ValidatableResponse changeUsersData(String authToken, UserCredentials newUserCredentials) {
    return given()
            .spec(getBaseSpec())
            .header("Authorization", authToken)
            .body(newUserCredentials)
            .when()
            .patch(CHANGE_USERS_DATA)
            .then()
           ;}

    public ValidatableResponse changeUsersDataWithoutAuth(UserCredentials newUserCredentials) {
        return given()
                .spec(getBaseSpec())
                .body(newUserCredentials)
                .when()
                .patch(CHANGE_USERS_DATA)
                .then()
               ;}

public ValidatableResponse logoutUser(String refreshToken) {
    return given()
            .spec(getBaseSpec())
            .body("{"+'"'+"token" + '"'+ ":" +'"'+refreshToken +'"' +'}')
            .when()
            .post(LOGOUT_USER)
            .then();
}
}
