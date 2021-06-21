package com.elm.shj.applicant.portal.automation.apis;

import com.elm.qa.framework.core.DataManager;
import com.elm.qa.framework.core.Global;
import com.elm.qa.framework.faker.Faker;
import com.elm.qa.framework.runner.Executer;
import com.elm.qa.framework.utilities.API;
import com.elm.qa.framework.utilities.APIUtil;
import com.elm.qa.framework.utilities.ReporterX;
import com.elm.shj.applicant.portal.automation.pages.SystemLogin;
import com.elm.shj.applicant.portal.automation.pages.UtilitiesUI;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;

import java.time.Duration;
import java.time.Instant;
import java.util.Hashtable;

import static com.elm.qa.framework.utilities.API.BuildRequest;
import static com.elm.qa.framework.utilities.API.ExecuteRequest;
import static io.restassured.RestAssured.given;

public class UtilitiesAPI {

    public static Faker fk = new Faker();
    public static Hashtable<String, Hashtable<String, String>> AccessTokensTable = new Hashtable<>();
    static RequestSpecBuilder builder;
    public static SystemLogin systemLogin = new SystemLogin();

    public static String getAccessTokenFromBrowser() {
        String accessToken = "";
        try {
            JavascriptExecutor jsExec = (JavascriptExecutor) Global.Test.Browser;
            Object aa = jsExec.executeScript("return sessionStorage.getItem('oidc.user:" + Global.Test.BaseURL + "/identityservice:inspection_spa');");
            Thread.sleep(2000);
            JSONObject obj = new JSONObject(aa.toString());
            accessToken = obj.getString("access_token");
        } catch (Exception ex) {

            ReporterX.error("getAccessTokenFromBrowser() failed for exception: " + ex.getMessage());
        }
        return accessToken;
    }

    public static String getAccessTokenFromAPI(String strLoginRow) throws Exception {
        String accessToken = "";
        Response loginRes = loginAPI(strLoginRow);
        Assert.assertTrue(APIUtil.formatAsOneLine(loginRes.asString()).contains("\"success\":true"), "Login API Response");


        String strCookie = "Identity.TwoFactorUserId=" + loginRes.getCookie("Identity.TwoFactorUserId") + "; idsrv.session=" + loginRes.getCookie("idsrv.session") + "; .AspNetCore.Identity.Application=" + loginRes.getCookie(".AspNetCore.Identity.Application");

        Response res = loginCallBackAPI(strCookie);
        Assert.assertEquals(302, res.getStatusCode(), "Login Callback API Response Code.");
        Assert.assertTrue(res.getHeader("Location").contains("access_token="), "access token in Location Header");

        accessToken = res.getHeader("Location").split("access_token=")[1].split("&token_type")[0];
        return accessToken;
    }


    public static boolean setAccessTokenInTestData() {
        String strUserName;
        try {

            strUserName = DataManager.GetExcelDataTable("Select * from Login where RowID = " + Executer.TestDataRow.get("LoginRow".toUpperCase())).get(1).get("userID".toUpperCase());
            //checking if we have already saved token with in last 60 minutes
            if (AccessTokensTable.containsKey(strUserName.toUpperCase())) {
                String strAccessedTime = AccessTokensTable.get(strUserName.toUpperCase()).get("TimeStamp".toUpperCase());
                String strAccessToken = AccessTokensTable.get(strUserName.toUpperCase()).get("AccessToken".toUpperCase());
                Duration timeElapsed = Duration.between(Instant.parse(strAccessedTime), Instant.now());
                if (timeElapsed.toMinutes() <= 60) {
                    if (!strAccessToken.equalsIgnoreCase("")) {
                        Executer.TestDataRow.put("AccessToken".toUpperCase(), strAccessToken);
                        ReporterX.info("Access Token Is Already available for the user '" + strUserName + "'.!");
                        return true;
                    }
                }
            }

            //Getting token through Web Service Calls
            try {

                String accessToken = getAccessTokenFromAPI(Executer.TestDataRow.get("LoginRow".toUpperCase()));

                if (!"".equalsIgnoreCase(accessToken)) {
                    Executer.TestDataRow.put("AccessToken".toUpperCase(), accessToken);

                    //Saving AccessToken
                    Hashtable<String, String> AccessToken = new Hashtable<>();
                    AccessToken.put("AccessToken".toUpperCase(), accessToken);
                    AccessToken.put("TimeStamp".toUpperCase(), Instant.now().toString());
                    AccessTokensTable.put(strUserName.toUpperCase(), AccessToken);

                    ReporterX.info("AccessToken Successfully retrieved from Sign-In API for the user '" + strUserName + "'.!");
                    //System.out.println("AccessToken = " + AccessToken);
                    return true;
                }

            } catch (Exception e) {
                ReporterX.error(e);
            }

            //if no saved token available and failed to get token through API then login to Web UI and set the token in user data
            systemLogin.SignIn(Executer.TestDataRow.get("LoginRow".toUpperCase()));
            String accessToken = getAccessTokenFromBrowser();
            if ("".equalsIgnoreCase(accessToken)) {
                ReporterX.fail("Failed to get AccessToken from Browser");
            }

            Executer.TestDataRow.put("AccessToken".toUpperCase(), accessToken);

            //Saving AccessToken
            Hashtable<String, String> AccessToken = new Hashtable<>();
            AccessToken.put("AccessToken".toUpperCase(), accessToken);
            AccessToken.put("TimeStamp".toUpperCase(), Instant.now().toString());
            AccessTokensTable.put(strUserName.toUpperCase(), AccessToken);

            ReporterX.info("AccessToken Successfully retrieved from Browser for the user '" + strUserName + "'.!");
            return true;

        } catch (Exception e) {

            ReporterX.error(e);
        }
        return false;
    }

    public static Response loginCallBackAPI(String strCookie) {
        Response res = null;
        try {

            String strFinalURL = Global.Test.EnviromentVars.get("BASE_URL") + "/identityservice/connect/authorize/callback";

            //RestAssured.urlEncodingEnabled = true;
            res = given().redirects().follow(false).header("Cookie", strCookie).when()
                    .queryParam("client_id", "inspection_spa")
                    .queryParam("response_type", "id_token token")
                    .queryParam("redirect_uri", Global.Test.EnviromentVars.get("BASE_URL") + "/#/identity-guards/auth-callback#")
                    .queryParam("scope", "openid profile inspection_profile")
                    .queryParam("state", "444")
                    .queryParam("nonce", "444")
                    .get(strFinalURL);

            if (res.getStatusCode() != 302) {
                res.then().log().all();
            }

            res.then().assertThat().statusCode(302);

        } catch (Exception e) {

            ReporterX.error(e);
        }
        return res;
    }

    public static Response loginAPI(String strLoginRow) {
        Response res = null;
        Hashtable<String, String> loginData = DataManager.GetExcelDataTable("Select * from Login where RowID = " + strLoginRow).get(1);
        try {
            String strRequestBody = "{\"loginName\": \"" + loginData.get("USERID") + "\",\"password\": \"" + loginData.get("PASSWORD") + "\",\"captchaString\": \"123456\"}";
            String strFinalURL = Global.Test.EnviromentVars.get("BASE_URL") + "/identityservice/authentication/sign-in";

            res = given()
                    .header("Content-Type", "application/json")
                    .body(strRequestBody)
                    .when()
                    .post(strFinalURL);

            if (res.getStatusCode() != 200) {
                res.then().log().all();
            }

        } catch (Exception e) {

            ReporterX.error(e);

        }
        return res;
    }


    public static Response loginAdminAPI() {
        Response res = null;
        Hashtable<String, String> dr = DataManager.GetExcelDataTable("Select * from Login where RowID = " + Executer.TestDataRow.get("LoginRow".toUpperCase())).get(1);
        try {
            String strRequestBody = "{\"username\": \"" + dr.get("USERID") + "\",\"password\": \"" + dr.get("PASSWORD") + "\",\"captchaString\": \"123456\"}";
            String strFinalURL = Global.Test.EnviromentVars.get("ADMIN_URL") + "/api/authentication/sign-in";

            res = given()
                    .header("Content-Type", "application/json")
                    .body(strRequestBody)
                    .when()
                    .post(strFinalURL);
            if (res.getStatusCode() != 200) {
                res.then().log().all();
            }

        } catch (Exception e) {

            ReporterX.error(e);

        }
        return res;
    }

    public static void VerifyAPICall(Response res) {
        if (!Executer.TestDataRow.get("WsValidationMessages".toUpperCase()).equals("")) {
            API.VerifyWSResults(res.asString(), Executer.TestDataRow.get("TestCaseDescription".toUpperCase()) + " WS Validation");
        }

        if (!Executer.TestDataRow.get("DBValidationMessages".toUpperCase()).equals("")) {

        }

        try {
            if (!((String) Executer.TestDataRow.get("WsStatusCode".toUpperCase())).equals("")) {
                API.VerifyWsStatusCode(Executer.TestDataRow, res, Executer.TestDataRow.get("TestCaseDescription".toUpperCase()) + " WS StatusCode");
            }
        } catch (Exception var2) {
        }
    }

    public static Response callWebService() throws Exception {
        Response res = null;
        UtilitiesUI.prepareTestData(Executer.TestDataRow);
        res = ExecuteRequest(Executer.TestDataRow);
        VerifyAPICall(res);
        return res;

    }


}
