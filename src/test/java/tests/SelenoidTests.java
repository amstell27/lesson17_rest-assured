package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SelenoidTests {
    //make request to https://selenoid.autotests.cloud/status
    //total is 20

    @Test
    void checkTotal() {
        given()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .body("total", is(20));
    }

    @Test
    void checkTotalWithoutGiven() {
        get("https://selenoid.autotests.cloud/status")
                .then()
                .body("total", is(20));
    }

    @Test
    void checkChromeVersion() {
        get("https://selenoid.autotests.cloud/status")
                .then()
                .body("browsers.chrome", hasKey("100.0"));
    }

    @Test
    void checkTotalBadPractice() {
        String response = get("https://selenoid.autotests.cloud/status")
                .then()
                .extract().response().asString();
        System.out.println("Response " + response);

        String expectedResponse = "{\"total\":20,\"used\":1,\"queued\":0,\"pending\":0," +
                "\"browsers\":" +
                "{\"chrome\":{\"100.0\":{\"user1\":{\"count\":1,\"sessions\":[{\"id\":\"66775f7e1423c9d53847cef36fd79a1c\",\"container\":\"2c1a9a1f13b8327b8e2b2eb505cc25ac99a9d1118173a15f6f6064ff90d30011\",\"containerInfo\":{\"id\":\"2c1a9a1f13b8327b8e2b2eb505cc25ac99a9d1118173a15f6f6064ff90d30011\",\"ip\":\"172.18.0.4\"},\"vnc\":true,\"screen\":\"1920x1080x24\",\"caps\":" +
                "{\"browserName\":" +
                "\"chrome\",\"version\":\"100.0\",\"screenResolution\":\"1920x1080x24\",\"enableVNC\":true,\"videoScreenSize\":\"1920x1080\",\"name\":\"Manual session\",\"labels\":{\"manual\":\"true\"},\"sessionTimeout\":\"60m\"},\"started\":\"2022-04-20T09:18:18.228213204Z\"}]}},\"99.0\":{}},\"" +
                "firefox\":{\"97.0\":{},\"98.0\":{}}," +
                "\"opera\":{\"84.0\":{},\"85.0\":{}}}}\n";
        assertEquals(expectedResponse, response);
    }

    @Test
    void checkTotalGoodPractice() {
        Integer response = get("https://selenoid.autotests.cloud/status")
                .then()
                .extract()
                .path("total");
        System.out.println("Response " + response);

        Integer expectedResponse = 20;
        assertEquals(expectedResponse, response);

    }

    @Test
    void responseExamples() {
        Response response = get("https://selenoid.autotests.cloud/status")
                .then()
                .extract().response();
        System.out.println("Response: " + response);
        System.out.println("Response .toString(): " + response.toString());
        System.out.println("Response .asString: " + response.asString());
        System.out.println("Response path(\"total\"): " + response.path("total"));
        System.out.println("Response path(\"browsers.chrome\"): " + response.path("browsers.chrome"));
    }

    @Test
    void checkStatus401() {
        get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .statusCode(401);
    }

    @Test
    void checkStatus200() {
        get("https://user1:1234@selenoid.autotests.cloud/wd/hub/status")
                .then()
                .statusCode(200);
    }

    @Test
    void checkStatus200WithAuth() {
        given()
                .auth().basic("user1", "1234")
                .get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .statusCode(200);
    }

}
