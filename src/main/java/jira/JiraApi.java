package jira;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;

import static io.restassured.RestAssured.given;

@Slf4j
public class JiraApi {
    public String getJiraIssueIdByKey(String baseUrl, String userName, String token, String issueKey) {
        log.info("Get jira ticket ID by key");
        Response response = getResponseFromJira(baseUrl, userName, token, issueKey);
        String issueId = response.jsonPath().getString("id");
        log.info("ID issue - {}", issueId);
        return issueId;
    }

    public String getJiraIssueNameByKey(String baseUrl, String userName, String token, String issueKey) {
        log.info("Get jira ticket Name by key");
        Response response = getResponseFromJira(baseUrl, userName, token, issueKey);
        String issueSummary = response.jsonPath().getString("fields.summary");
        log.info("Issue Summary - {}", issueSummary);
        return issueSummary;
    }

    private Response getResponseFromJira(String baseUrl, String userName, String token, String issueKey) {
        String issueDetailsUrl = baseUrl + "/rest/api/3/issue/" + issueKey;
        String authHeader = "Basic " + new String(Base64.getEncoder().encode(((userName + ":" + token).getBytes())));
        Response response = given()
                .header("Authorization", authHeader)
                .header("Content-Type", "application/json")
                .get(issueDetailsUrl)
                .then()
                .statusCode(200)
                .extract()
                .response();
        log.info("Response Status Code: " + response.getStatusCode());
        return response;
    }
}
