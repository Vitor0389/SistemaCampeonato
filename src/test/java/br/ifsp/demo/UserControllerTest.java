package br.ifsp.demo;

import br.ifsp.demo.security.auth.AuthRequest;
import br.ifsp.demo.security.user.User;
import io.restassured.filter.log.LogDetail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

class UserControllerTest extends BaseApiIntegrationTest { // reuse base test class
    @Test
    @DisplayName("Should register user and return 201 with id as payload")
    void shouldRegisterUserAndReturn201WithIdAsPayload() {
        final User user = EntityBuilder.createRandomUser("123password");
        given().contentType("application/json").port(port).body(user) // request specification
                .when().post("/api/v1/register") // invocation
                .then().log().ifValidationFails(LogDetail.BODY).statusCode(201).body("id", notNullValue()); // validable response
    }
    @Test
    @DisplayName("Should login with valid credentials")
    void shouldLoginWithValidCredentials() {
        final String plainTextPassword = "123password";
        final User user = registerUser(plainTextPassword); // base class method – directly inserts the user in the database.
        AuthRequest authRequest = new AuthRequest(user.getEmail(), plainTextPassword);
        given().contentType("application/json").port(port).body(authRequest).
                when().post("/api/v1/authenticate").
                then().log().ifValidationFails(LogDetail.BODY).statusCode(200).body("token", notNullValue());
    }
}