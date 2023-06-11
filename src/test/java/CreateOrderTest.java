import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class CreateOrderTest {
    UserClient userClient;
    User user;
    OrderClient orderClient;
    Order order;

    private String authToken;
    private String[] ingredients = {""};
    private String[] ingredients2 = {"", ""};
    private String[] ingredientsEmpty = {};
    private String ingredientHash;
    private String ingredientHash2;
    private String wrongIngredientHash = "12345678TestWrongHash";

    private String orderNumber;
    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
        ValidatableResponse getIngredientsResponse = orderClient.getIngredients();
        ingredientHash = getIngredientsResponse.extract().path("data[0]._id").toString();
        ingredientHash2 = getIngredientsResponse.extract().path("data[1]._id").toString();
        ingredients[0] = ingredientHash;
        order = new Order(ingredients);
        user = UserGenerator.getRandomWithAllParams();
        ValidatableResponse actualResponse = userClient.create(user);
        authToken = actualResponse.extract().jsonPath().getString("accessToken");

    }

    @Test
    @DisplayName("Create order without authorization")
    public void createOrderWithoutAuthSuccess() {

        ValidatableResponse orderResponse = orderClient.createOrder(order);
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(orderResponse.extract().asString(), JsonObject.class);
        orderNumber = jsonObject.getAsJsonObject("order").get("number").getAsString();
       int intOrderNumber = Integer.parseInt(orderNumber);
        Assert.assertThat(intOrderNumber,greaterThanOrEqualTo(1));
        orderResponse.assertThat()
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .statusCode(200);
    }

    @Test
    @DisplayName("Create order with wuthorization")
    public void createOrderWithAuthSuccess() {
        ingredients2[0] = ingredientHash;
        ingredients2[1] = ingredientHash2;
        order = new Order(ingredients2);
        ValidatableResponse orderResponse = orderClient.createOrderWithAuth(authToken, order);
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(orderResponse.extract().asString(), JsonObject.class);
        orderNumber = jsonObject.getAsJsonObject("order").get("_id").getAsString();
        String status = jsonObject.getAsJsonObject("order").get("status").getAsString();
        String name = jsonObject.getAsJsonObject("order").get("name").getAsString();
        String createdAt = jsonObject.getAsJsonObject("order").get("createdAt").getAsString();
        String updatedAt = jsonObject.getAsJsonObject("order").get("updatedAt").getAsString();
        String number = jsonObject.getAsJsonObject("order").get("number").getAsString();
        String price = jsonObject.getAsJsonObject("order").get("price").getAsString();
        Assert.assertThat(status,notNullValue());
        Assert.assertThat(name,notNullValue());
        Assert.assertThat(createdAt,notNullValue());
        Assert.assertThat(updatedAt,notNullValue());
        Assert.assertThat(number,notNullValue());
        Assert.assertThat(price,notNullValue());
        Assert.assertThat(orderNumber,notNullValue());
        orderResponse.assertThat()
                .body("name", notNullValue())
                .body("success", equalTo(true))
                .statusCode(200);
    }


    @Test
    @DisplayName("Create order without ingredients")
    public void createOrderWithNoIngredientsError() {
        order = new Order(ingredientsEmpty);
        ValidatableResponse orderResponse = orderClient.createOrder(order);
        orderResponse.assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"))
                .statusCode(400);
    }
    @Test
    @DisplayName("Create order with wrong ingredient hash")
    public void createOrderWithWrongHasNumbersError() {
        ingredients[0] = wrongIngredientHash;
        order = new Order(ingredients);
        ValidatableResponse actualResponse = orderClient.createOrder(order);
        actualResponse.assertThat()
                .statusCode(500);
}
    @After
    public void deleteUser() {
            userClient.deleteUser(authToken);
    }
}
