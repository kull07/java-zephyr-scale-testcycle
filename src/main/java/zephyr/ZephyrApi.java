package zephyr;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;

import static io.restassured.RestAssured.given;

@Slf4j
public class ZephyrApi {

    public String createTestCycle(String baseUrl, String token, String testCycleName, String projectKey) {
        log.info("Start create test cycle");
        RestAssured.baseURI = baseUrl;
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", testCycleName);
        requestBody.put("projectKey", projectKey);
        requestBody.put("description", "Automated create cycle");
        requestBody.put("statusName", "In Progress");

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .log().all()
                .when()
                .post("/testcycles")
                .then()
                .statusCode(201)
                .extract()
                .response();
        String testCycleId = response.jsonPath().getString("key");
        log.info("Test Cycle Created with ID: {}", testCycleId);
        return testCycleId;
    }


    public void updateTestCycleLinks(String baseUrl, String token, String testCyclesId, String issueId) {
        log.info("Start update test cycles, link jira issue");
        RestAssured.baseURI = baseUrl;
        JSONObject requestBody = new JSONObject();
        requestBody.put("issueId", issueId);
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .log().all()
                .when()
                .post("/testcycles/" + testCyclesId + "/links/issues")
                .then()
                .statusCode(201)
                .extract()
                .response();
        log.info("Response Status Code: " + response.getStatusCode());
    }

    public void createTestExecution(String baseUrl, String token, String projectKey, String testCaseKey, String testCycleKey, String statusName) {
        log.info("Add test case to execution with status '{}'", statusName);
        RestAssured.baseURI = baseUrl;
        JSONObject requestBody = new JSONObject();
        requestBody.put("projectKey", projectKey);
        requestBody.put("testCaseKey", testCaseKey);
        requestBody.put("testCycleKey", testCycleKey);
        requestBody.put("statusName", statusName);
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .log().all()
                .when()
                .post("/testexecutions")
                .then()
                .statusCode(201)
                .extract()
                .response();
        log.info("Response Status Code: " + response.getStatusCode());
    }
}
