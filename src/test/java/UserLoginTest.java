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

public class UserLoginTest {

    UserClient userClient = new UserClient();
    UserCredentials userCredentials;
    UserGenerator userGenerator;
    User user;
    ValidatableResponse actualResponse;
   private String authToken;

    @Before
    public void setUp() {
        user = userGenerator.getRandomWithAllParams();
        userCredentials = new UserCredentials(user.getEmail(), user.getPassword());
    }
    @Test
    @DisplayName("Login user with all correct params")
    public void correctParamsLoginSuccess() {
        userClient.create(user);
        ValidatableResponse actualResponse =
                 userClient.login(userCredentials);
        authToken = actualResponse.extract().path("accessToken").toString();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(actualResponse.extract().asString(), JsonObject.class);
        String email = jsonObject.getAsJsonObject("user").get("email").getAsString();
        String name = jsonObject.getAsJsonObject("user").get("name").getAsString();
        Assert.assertThat(email, equalTo(user.getEmail().toLowerCase()));
        Assert.assertThat(name, equalTo(user.getName()));
        actualResponse.assertThat()
                .body("success", equalTo(true))
                .body("refreshToken", notNullValue())
                .body("accessToken", notNullValue())
                .statusCode(200);
    }

    @Test
    @DisplayName("Login user with incorrect params")
    public void incorrectParamsLoginError() {
        // тут использую рандомные логин + пароль, но не регая юзера, то есть вероятность того, что эта рандомная комбинация будет корректна, близится к нулю
        actualResponse = userClient.login(userCredentials);
        actualResponse.assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"))
                .statusCode(401);
    }

    @After
    public void deleteUser() {
        if (authToken != null) {
            userClient.deleteUser(authToken);
        }
    }
}
