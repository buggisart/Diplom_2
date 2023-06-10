import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class GetUserOrdersTest {

    UserClient userClient;
    User user;
    OrderClient orderClient;
    Order order;

    private String authToken;
    private String[] ingredients = {""};
    private String ingredientHash;


    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
        ValidatableResponse getIngredientsResponse = orderClient.getIngredients();
        ingredientHash = getIngredientsResponse.extract().path("data[0]._id").toString();
        ingredients[0] = ingredientHash;
        order = new Order(ingredients);
        user = UserGenerator.getRandomWithAllParams();
        ValidatableResponse actualResponse = userClient.create(user);
        authToken = actualResponse.extract().jsonPath().getString("accessToken");
        orderClient.createOrderWithAuth(authToken, order);

    }
    @Test
    @DisplayName("Get users orders with authorization")
    public void getUsersOrdersWithAuthSuccess() {
       ValidatableResponse actualResponse = orderClient.getUserOrdersWithAuth(authToken);
                actualResponse.assertThat()
                        .body("success", equalTo(true))
                        .body("orders", notNullValue())
                        .body("total", greaterThanOrEqualTo(1))
                        .body("totalToday", greaterThanOrEqualTo(1))
                        .statusCode(200);
    }
    @Test
    @DisplayName("Get users orders without authorization")
    public void getUsersOrdersWithoutAuthSuccess() {
        ValidatableResponse actualResponse = orderClient.getUserOrdersWithoutAuth();
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
