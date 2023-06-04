import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserRegisterTest {

    UserClient userClient;

    User user;

    @Test
    public void userRegisterAllCorrectParamsSuccess() {
        userClient = new UserClient();
        user = UserGenerator.getRandomWithAllParams();
        ValidatableResponse actualResponse = userClient.create(user);
        actualResponse.assertThat().body("success", equalTo(true))
                .body("email", equalTo(user.getEmail()))
                .body("name", equalTo(user.getName()))
                .body("refreshToken", notNullValue())
                .body("accessToken", notNullValue())
                .statusCode(200);
    }
    @Test
    public void userRegisterTheSameError() {
        userClient = new UserClient();
        user = UserGenerator.getRandomWithAllParams();
        userClient.create(user);
        ValidatableResponse actualResponse = userClient.create(user);
        actualResponse.assertThat().body("success", equalTo(false))
                .body("message", equalTo("User already exists"))
                .statusCode(403);
    }

    @Test
    public void userRegisterWithoutPassword() {
        userClient = new UserClient();
        user = UserGenerator.getRandomWithoutPassword();
        userClient.create(user);
        ValidatableResponse actualResponse = userClient.create(user);
        actualResponse.assertThat().body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"))
                .statusCode(403);
    }
    @Test
    public void userRegisterWithoutEmail() {
        userClient = new UserClient();
        user = UserGenerator.getRandomWithoutLogin();
        userClient.create(user);
        ValidatableResponse actualResponse = userClient.create(user);
        actualResponse.assertThat().body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"))
                .statusCode(403);
    }
}
