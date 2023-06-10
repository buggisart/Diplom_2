import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient {

    private static final String CREATE_ORDER = "orders";
    private static final String GET_INGREDIENTS = "ingredients";
    private static final String GET_USER_ORDERS = "orders";

    public ValidatableResponse getIngredients() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(GET_INGREDIENTS)
                .then();
    }

    public ValidatableResponse createOrder(Order ingredients) {
        return given()
                .spec(getBaseSpec())
                .body(ingredients)
                .when()
                .post(CREATE_ORDER)
                .then();
    }
    public ValidatableResponse createOrderWithAuth(String authToken, Order ingredients) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", authToken)
                .body(ingredients)
                .when()
                .post(CREATE_ORDER)
                .then();
    }
    public ValidatableResponse getUserOrdersWithAuth(String authToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", authToken)
                .when()
                .get(GET_USER_ORDERS)
                .then();    }
    public ValidatableResponse getUserOrdersWithoutAuth() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(GET_USER_ORDERS)
                .then();
    }
}
