package tests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class RequestTest {
    private final Util util = new Util();
    private static final String posts = "/posts/{id}";
    private static RequestSpecification requestSpec;


    @BeforeClass
    public static void setupRequest() {
        requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://jsonplaceholder.typicode.com")
                .setAccept(ContentType.JSON)
                .build();
    }

    @Test
    public void simpleGetAllPosts() {

        List<Map<String, Object>> respObj =
                given()
                        .spec(requestSpec)
                        .when()
                        .get("/posts")
                        .then()
                        .log()
                        .body()
                        .statusCode(200)
                        .extract()
                        .jsonPath()
                        .getList("$");

        Assert.assertTrue(util.checker(respObj));
    }


    @Test
    public void getRequestSomePost() {
        Map<String, Object> respObj =
                given()
                        .spec(requestSpec)
                        .when()
                        .get(posts, 99)
                        .then()
                        .log()
                        .body()
                        .statusCode(200)
                        .extract()
                        .jsonPath()
                        .getMap("$");

        Assert.assertEquals(respObj.get("userId"), 10);
        Assert.assertEquals(respObj.get("id"), 99);
        Assert.assertNotNull(respObj.get("title"));
        Assert.assertNotNull(respObj.get("body"));

    }

    @Test
    public void getRequestInvalidPost() {
        String response =
                given()
                        .spec(requestSpec)
                        .when()
                        .get(posts, 150)
                        .then()
                        .log()
                        .body()
                        .statusCode(404)
                        .extract()
                        .body()
                        .asString();

        Assert.assertEquals(response, "{}");
    }

    @Test
    public void postRequestSomePost() {

        String title = RandomStringUtils.randomAlphabetic(20);
        String body = RandomStringUtils.randomAlphabetic(20);

        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("title", title);
        bodyParam.put("body", body);
        bodyParam.put("userId", 1);

        Map<String, Object> respObj =
                given()
                        .spec(requestSpec)
                        .contentType(ContentType.JSON)
                        .body(bodyParam)
                        .post("posts")
                        .then()
                        .statusCode(201)
                        .log()
                        .body()
                        .extract()
                        .jsonPath()
                        .getMap("$");

        Assert.assertEquals(respObj.get("userId"), 1);
        Assert.assertNotNull(respObj.get("id"));
        Assert.assertEquals(respObj.get("title"), title);
        Assert.assertEquals(respObj.get("body"), body);

    }

    @Test
    public void getRequestUsers() {
        List<Map<String, Object>> respObj =
                given()
                        .spec(requestSpec)
                        .when()
                        .get("users")
                        .then()
                        .log()
                        .body()
                        .statusCode(200)
                        .extract()
                        .jsonPath()
                        .getList("$");


        Assert.assertEquals(util.findUserByID(respObj, 5), util.findUserByID("user5.txt"));
    }

    @Test
    public void getRequestSomeUser() {
        Map<String, Object> respObj =
                given()
                        .spec(requestSpec)
                        .when()
                        .get("users/5")
                        .then()
                        .log()
                        .body()
                        .statusCode(200)
                        .extract()
                        .jsonPath()
                        .getMap("$");

        Assert.assertEquals(respObj.toString(), util.findUserByID("user5.txt"));
    }
}


