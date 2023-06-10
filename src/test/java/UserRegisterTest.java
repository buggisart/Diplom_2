import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserRegisterTest {

    UserClient userClient;

    User user;

    private String authToken;
    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Register user with all correct params")
    public void userRegisterAllCorrectParamsSuccess() {
        user = UserGenerator.getRandomWithAllParams();
        ValidatableResponse actualResponse = userClient.create(user);
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(actualResponse.extract().asString(), JsonObject.class);
        authToken = actualResponse.extract().jsonPath().getString("accessToken");
        String userEmail = jsonObject.getAsJsonObject("user").get("email").getAsString();
        String userName = jsonObject.getAsJsonObject("user").get("name").getAsString();
        Assert.assertThat(userEmail, equalTo(user.getEmail().toLowerCase()));
        Assert.assertThat(userName, equalTo(user.getName()));
                actualResponse.assertThat()
                .body("success", equalTo(true))
                        .body("refreshToken", notNullValue())
                .body("accessToken", notNullValue())
                .statusCode(200);
    }
    @Test
    @DisplayName("Register user twice")
    public void userRegisterTheSameError() {
        user = UserGenerator.getRandomWithAllParams();
        ValidatableResponse firstResponse = userClient.create(user);
        authToken = firstResponse.extract().jsonPath().getString("accessToken");
        ValidatableResponse actualResponse = userClient.create(user);
        actualResponse.assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"))
                .statusCode(403);
    }

    @Test
    @DisplayName("Register user without password")
    public void userRegisterWithoutPasswordError() {
        user = UserGenerator.getRandomWithoutPassword();
        userClient.create(user);
        ValidatableResponse actualResponse = userClient.create(user);
        actualResponse.assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"))
                .statusCode(403);
    }
    @Test
    @DisplayName("Register user without email")
    public void userRegisterWithoutEmailError() {
        user = UserGenerator.getRandomWithoutLogin();
        userClient.create(user);
        ValidatableResponse actualResponse = userClient.create(user);
        actualResponse.assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"))
                .statusCode(403);
    }
    @After
    public void deleteUser() {
        if (authToken != null) {
            userClient.deleteUser(authToken);
        }
    }
}
