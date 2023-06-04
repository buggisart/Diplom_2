import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserLoginTest {

    UserClient userClient = new UserClient();
    UserCredentials userCredentials;
    UserGenerator userGenerator;
    User user;
    @Test
    public void correctParamsLoginSuccess() {
        user = userGenerator.getRandomWithAllParams();
        userClient.create(user);
        userCredentials = new UserCredentials(user.getEmail(), user.getPassword());
        ValidatableResponse actualResponse =
                 userClient.login(userCredentials);
        actualResponse.assertThat()
                .body("success", equalTo(true))
                .body("email", equalTo(user.getEmail()))
                .body("name", equalTo(user.getName()))
                .body("refreshToken", notNullValue())
                .body("accessToken", notNullValue())
                .statusCode(200);
    }

    @Test
    public void incorrectParamsLoginError() {
        // тут использую рандомные логин + пароль, но не регая юзера, то есть вероятность того, что эта рандомная комбинация будет корректна близиться к нулю
        user = userGenerator.getRandomWithAllParams();
        userCredentials = new UserCredentials(user.getEmail(), user.getPassword());
        ValidatableResponse actualResponse =
                userClient.login(userCredentials);
        actualResponse.assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"))
                .statusCode(401);
    }
}
