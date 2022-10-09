package restassured;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import models.LoginModel;
import models.RegisterModel;
import models.UpdateUserModel;
import org.testng.annotations.Test;

import java.io.File;

import static org.hamcrest.Matchers.*;

public class RestAssuredTest {

    @Test
    public void checkUserNotFoundTest() {
        RestAssured
                .given()
                .when()
                .get("https://reqres.in/api/users/23")
                .then()
                .statusCode(404);
    }

    @Test
    public void checkBodyValuesTest() {
        RestAssured
                .given()
                .when()
                .get("https://reqres.in/api/unknown")
                .then()
                .statusCode(200)
                .body("page", instanceOf(Integer.class))
                .body("per_page", equalTo(6));
    }

    @Test
    public void checkStaticResponseTest() {
        JsonPath expectedJson = new JsonPath(new File("src/test/resources/user.json"));
        RestAssured
                .given()
                .when()
                .get("https://reqres.in/api/unknown/2")
                .then()
                .statusCode(200)
                .body("", equalTo(expectedJson.getMap("")));

    }

    @Test
    public void getWithQueryParamTest() {
        RestAssured
                .given()
                .queryParam("page", "2")
                .when()
                .get("https://reqres.in/api/users")
                .then()
                .statusCode(200)
                .body("page", equalTo(2));
    }

    @Test
    public void updateUserTest() {
        UpdateUserModel updateUser = UpdateUserModel
                .builder()
                .name("Dasha")
                .job("AQA")
                .build();

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .and()
                .body(updateUser)
                .when()
                .put("https://reqres.in/api/users/2")
                .then()
                .statusCode(200);
    }

    @Test
    public void updateUserNameTest() {
        UpdateUserModel updateUserName = UpdateUserModel
                .builder()
                .name("Dasha")
                .build();

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .and()
                .body(updateUserName)
                .when()
                .patch("https://reqres.in/api/users/2")
                .then()
                .statusCode(200);
    }

    @Test
    public void deleteUserTest() {
        RestAssured
                .given()
                .when()
                .delete("https://reqres.in/api/users/2")
                .then()
                .statusCode(204);
    }

    @Test
    public void registerSuccessTest() {
        RegisterModel registerModel = RegisterModel
                .builder()
                .email("eve.holt@reqres.in")
                .password("pistol")
                .build();

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .and()
                .body(registerModel)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .statusCode(200);
    }

    @Test
    public void registerUnSuccessTest() {
        RegisterModel registerModel = RegisterModel
                .builder()
                .email("sydney@fife")
                .build();

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .and()
                .body(registerModel)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }

    @Test
    public void loginSuccessTest() {
        LoginModel loginModel = LoginModel
                .builder()
                .email("eve.holt@reqres.in")
                .password("cityslicka")
                .build();

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .and()
                .body(loginModel)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .statusCode(200)
                .body(containsString("token"));
    }

    @Test
    public void loginUnSuccessTest() {
        LoginModel loginModel = LoginModel
                .builder()
                .email("eve.holt@reqres.in")
                .build();

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .and()
                .body(loginModel)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }

    @Test
    public void getDelayedResponseTest() {
        JsonPath expectedJson = new JsonPath(new File("src/test/resources/users.json"));
        RestAssured
                .given()
                .when()
                .get("https://reqres.in/api/users?delay=3")
                .then()
                .statusCode(200)
                .body("", equalTo(expectedJson.getMap("")));
    }


}
