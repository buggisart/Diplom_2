import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class UserChangeDataTest {

    User user;
    User userWithNewData;
    UserClient userClient;
   private String authToken;
    UserCredentials userCredentials;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandomWithAllParams();
        ValidatableResponse createResponse = userClient.create(user);
        authToken = createResponse.extract().path("accessToken");
        userWithNewData = UserGenerator.getRandomWithAllParams();
    }
    @Test
    @DisplayName("Change users data fields")
    public void changeUsersDataSuccess() {
        ValidatableResponse actualResponse = userClient.changeUsersData(authToken, userCredentials.from(userWithNewData));
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(actualResponse.extract().asString(), JsonObject.class);
        String email = jsonObject.getAsJsonObject("user").get("email").getAsString();
        String name = jsonObject.getAsJsonObject("user").get("name").getAsString();
        Assert.assertThat(email, equalTo(userWithNewData.getEmail().toLowerCase()));
        Assert.assertThat(name, equalTo(userWithNewData.getName()));
        actualResponse.assertThat()
                .body("success", equalTo(true))
                .statusCode(200);

    }

    @Test
    @DisplayName("Change users data fields with empty authorization")
    public void changeUsersDataWithEmptyAuthError() {
        ValidatableResponse actualResponse = userClient.changeUsersData("", userCredentials.from(userWithNewData));
        actualResponse.assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"))
                .statusCode(401);

    }
    @Test
    @DisplayName("Change users data fields without authorization")
    public void changeUsersDataWithoutAuthError() {
        ValidatableResponse actualResponse = userClient.changeUsersDataWithoutAuth(userCredentials.from(userWithNewData));
        actualResponse.assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"))
                .statusCode(401);

    }
    @After
    public void deleteUser() {
            userClient.deleteUser(authToken);
    }
}
