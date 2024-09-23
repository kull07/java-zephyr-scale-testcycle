package zephyr;

import jira.JiraApi;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class CreateAndUpdateCycleInZephyr {
    static JiraApi jiraApi = new JiraApi();
    static ZephyrApi zephyrApi = new ZephyrApi();

    private static final String JIRA_URL = "https://<your-jira-domain>.atlassian.net";
    // API  https://support.smartbear.com/zephyr-scale-cloud/api-docs/
    private static final String ZEPHYR_SCALE_URL = "https://api.zephyrscale.smartbear.com/v2";
    // Your email in Jira
    private static final String JIRA_USERNAME = "your-email@example.com";
    // https://support.atlassian.com/atlassian-account/docs/manage-api-tokens-for-your-atlassian-account/
    private static final String API_TOKEN_JIRA = "your-jira-api-token";
    // https://support.smartbear.com/zephyr-scale-cloud/docs/en/rest-api/generating-api-access-tokens.html
    private static final String API_TOKEN_ZEPHYR = "your-zephyr-scale-api-token";

    static String jiraIssueKey = "TP-123";
    static String projectKey = "TP";
    static String testCycleId;

    @BeforeAll
    public static void init() {
        log.info("Init test cycles");
        String ticketName = jiraApi.getJiraIssueNameByKey(JIRA_URL, JIRA_USERNAME, API_TOKEN_JIRA, jiraIssueKey);
        String testCycleName = jiraIssueKey + ", Automated Test Cycle, " + ticketName;
        testCycleId = zephyrApi.createTestCycle(ZEPHYR_SCALE_URL, API_TOKEN_ZEPHYR, testCycleName, projectKey);
        String issueId = jiraApi.getJiraIssueIdByKey(JIRA_URL, JIRA_USERNAME, API_TOKEN_JIRA, jiraIssueKey);
        zephyrApi.updateTestCycleLinks(ZEPHYR_SCALE_URL, API_TOKEN_ZEPHYR, testCycleId, issueId);
    }

    @Test
    @DisplayName("TP-T123 - Positive")
    public void positiveTest() {
        try {
            //Your test here
            Assertions.assertTrue(true);
            setExecutionCaseStatus("TP-T123", "Pass");
        } catch (AssertionError | Exception er) {
            setExecutionCaseStatus("TP-T123", "Fail");
        }

    }

    @Test
    @DisplayName("TP-T321 Negative")
    public void negativeTest() {
        try {
            //Your test here
            Assertions.fail();
            setExecutionCaseStatus("TP-T321", "Pass");
        } catch (AssertionError | Exception er) {
            setExecutionCaseStatus("TP-T321", "Fail");
        }
    }

    private void setExecutionCaseStatus(String testCaseKey, String status) {
        zephyrApi.createTestExecution(ZEPHYR_SCALE_URL, API_TOKEN_ZEPHYR, projectKey, testCaseKey, testCycleId, status);
    }
}
