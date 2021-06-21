package com.elm.shj.applicant.portal.automation.pages;

import com.elm.qa.framework.core.ActionX;
import com.elm.qa.framework.core.DataManager;
import com.elm.qa.framework.core.Global;
import com.elm.qa.framework.faker.Faker;
import com.elm.qa.framework.utilities.APIUtil;
import com.elm.qa.framework.utilities.ReporterX;
import com.elm.shj.applicant.portal.automation.apis.ShjApiManagement;
import io.restassured.response.Response;
import org.openqa.selenium.*;
import org.testng.Assert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Locale;

import static com.elm.qa.framework.core.ActionX.*;
import static com.elm.qa.framework.utilities.Common.jsonStringToMap;
import static com.elm.qa.framework.utilities.Common.mapToJsonString;
import static io.restassured.RestAssured.given;

public class UtilitiesUI {

    public static String strBoardName = "";
    public static String strSupportId = "";
    public static String strSupporterId = "";
    public static String strInsProcessId = "";
    public static String strFacilityName = "";
    public static String strPersonNameEng = "";
    public static String strInspItemID = "";
    public static String strInsCentreID = "";
    public static String strFacilityId = "";
    public static String strPersonNameArb = "";
    public static String strSurveyId = "";
    public static String strPersonFullNameEng = "";
    public static String strPersonFullNameArb = "";
    public static String strGeneralStringArb = "";
    public static String strViolationTypeId = "";
    public static String streetNameArb = "";
    public static String strAutoEmpNum = "";
    public static String strViolationName = "";
    public static String strAutoNumber12 = "";
    public static String strIdNum = "";
    public static String strAutoMob = "";
    public static String strIncidentId = "";
    public static String strInspItemName = "";
    public static String strInspItemNameDB = "";
    public static String strInspItemCode = "";
    public static String strGeneralStringEng = "";
    public static String strAutoNumber6 = "";
    public static String strAutoNumber3 = "";
    public static String strCategoryName = "";
    public static String strCategoryNameDB = "";
    public static String strCategoryOrder = "";
    public static String strAutoNumber2 = "";
    public static String strInspectorName = "";
    public static String strAutoDate = "";
    public static String strCenterName = "";
    public static String strCenterID = "";
    public static String strFacilityNumber = "";
    public static String strProcessedFacilityName = "";
    public static String strInspectorTaskID = "";
    public static String strAttachID = "";
    public static String strApproverManagerName = "";
    public static String strApproverName = "";
    public static String strViolationDBName = "";
    public static String strSupporterName = "";
    public static String strSupporterTaskID = "";
    public static String strCloseActionID = "";


    public static Hashtable<String, String> fullTableName;


    private static Faker fk = new Faker();
    private static Faker fkArb = new Faker(new Locale("ar"));
    private static DatabaseQueryManagement dbMgmt = new DatabaseQueryManagement();
    private static ShjApiManagement insProcMgmt = new ShjApiManagement();

    private static Home homePage = new Home();


    private static Response loginCallBackAPI(String strCookie) {
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

    private static Response loginAPI(String strLoginRow) {
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

    private static String getAccessTokenFromAPI(String strLoginRow) throws Exception {
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

    public static Response AddInspectionCentreAPI() {
        Response res = null;
        try {
            String strRequestBody = "{\n" +
                    "    \"submissionJSON\": \"{\\\"name\\\":\\\"Olaya\\\",\\\"refCode\\\":\\\"005\\\",\\\"inspectionCenterCoverages\\\":[{\\\"city\\\":\\\"\\\",\\\"district\\\":\\\"\\\",\\\"cityId\\\":1,\\\"fieldsupportemail\\\":\\\"\\\",\\\"districtId\\\":1,\\\"newData\\\":false}],\\\"roleEmails\\\":[{\\\"role\\\":\\\"Inspector\\\",\\\"roleEmail\\\":\\\"a@aaaa.com\\\",\\\"newData\\\":false}],\\\"submit\\\":true}\",\n" +
                    "    \"formCode\": \"InspectionCenterCreateForm\"\n" +
                    "}";
            String strFinalURL = Global.Test.EnviromentVars.get("BASE_URL") + "/inspectioncenterservice/api/inspection-centers";

            res = given()
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + getAccessTokenFromAPI(DataManager.getPropertyFile("Admin")))

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

    public static String stripNonDigits(final CharSequence input) {
        final StringBuilder sb = new StringBuilder(input.length());
        for (int i = 0; i < input.length(); i++) {
            final char c = input.charAt(i);
            if (c > 47 && c < 58) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static void resetVariables() {

        strBoardName = "";
        strSupportId = "";
        strSupporterId = "";
        strInsProcessId = "";
        strFacilityName = "";
        strPersonNameEng = "";
        strInspItemID = "";
        strInsCentreID = "";
        strFacilityId = "";
        strPersonNameArb = "";
        strSurveyId = "";
        strPersonFullNameEng = "";
        strPersonFullNameArb = "";
        strGeneralStringArb = "";
        strViolationTypeId = "";
        streetNameArb = "";
        strAutoEmpNum = "";
        strViolationName = "";
        strAutoNumber12 = "";
        strIdNum = "";
        strAutoMob = "";
        strIncidentId = "";
        strInspItemName = "";
        strInspItemNameDB = "";
        strInspItemCode = "";
        strGeneralStringEng = "";
        strAutoNumber6 = "";
        strAutoNumber3 = "";
        strCategoryName = "";
        strCategoryNameDB = "";
        strCategoryOrder = "";
        strAutoNumber2 = "";
        strInspectorName = "";
        strAutoDate = "";
        strCenterName = "";
        strCenterID = "";
        strFacilityNumber = "";
        strProcessedFacilityName = "";
        strInspectorTaskID = "";
        strAttachID = "";
        strApproverManagerName = "";
        strApproverName = "";
        strViolationDBName = "";
        strSupporterName = "";
        strSupporterTaskID = "";
        strCloseActionID = "";


    }

    public static void prepareTestData(Hashtable<String, String> dataRow) throws Exception {

        // loop through the data row and randomize test data

        String strURI = dataRow.get("Uri".toUpperCase()) == null ? "" : dataRow.get("Uri".toUpperCase());
        if (!strURI.equalsIgnoreCase("")) {
            if (! strURI.contains(Global.Test.BaseURL)) {
                strURI = Global.Test.BaseURL + strURI;
                dataRow.put("Uri".toUpperCase(), strURI);
            }
        }

//        String strRequestData = dataRow.get("RequestData".toUpperCase()) == null ? "" : dataRow.get("RequestData".toUpperCase());
//        if (strRequestData.contains("Auto-InsReportItemID")) {
//            //this variable should be use only to create multiple inspection item json body in single array.
//            List<String> strInsRepItemIds = dbMgmt.getInsItems();
//
//            JSONObject jo = new JSONObject(strRequestData);
//            String strInsItemTemplate = jo.getJSONArray("inspectionItems").get(0).toString();
//
//            for (String strInsRepItemId : strInsRepItemIds) {
//                String strTemp = strInsItemTemplate.replace("Auto-InsReportItemID", strInsRepItemId);
//                jo.getJSONArray("inspectionItems").put(new JSONObject(strTemp));
//            }
//            jo.getJSONArray("inspectionItems").remove(0);
//
//            strRequestData = jo.toString();
//            dataRow.put("RequestData".toUpperCase(),strRequestData);
//        }

        String strTestDataJson = mapToJsonString(dataRow);


        ReporterX.info("Preparing Test Data Started ------->");
        ReporterX.info("Initial  data is :- " + strTestDataJson);


        if (strTestDataJson.contains("Auto-CloseActionID")) {
            strCloseActionID = dbMgmt.getCloseActionId();
            strTestDataJson = strTestDataJson.replace("Auto-CloseActionID", strCloseActionID);
        }

        if (strTestDataJson.contains("Auto-InsReportItemID")) {
            strInspItemID = dbMgmt.getFirstInspectionItemID();
            strTestDataJson = strTestDataJson.replace("Auto-InsReportItemID", strInspItemID);

        }

        if (strTestDataJson.contains("Auto-CatName")) {
            strCategoryName = "AutoCategory_" + fk.letterify("??????????");
            strTestDataJson = strTestDataJson.replace("Auto-CatName", strCategoryName);

        }

        if (strTestDataJson.contains("Auto-AttachID")) {
            strAttachID = dbMgmt.getAttachID();//insProcMgmt.getAttachId();
            strTestDataJson = strTestDataJson.replace("Auto-AttachID", strAttachID);

        }

        if (strTestDataJson.contains("Auto-InspectorTaskID")) {
            strInspectorTaskID = dbMgmt.getTaskIdByTaskDefName("InspectorTask", "ID_");
            strTestDataJson = strTestDataJson.replace("Auto-InspectorTaskID", strInspectorTaskID);

        }

        if (strTestDataJson.contains("Auto-SupporterTaskID")) {
            strSupporterTaskID = dbMgmt.getTaskIdByTaskDefName("FieldSupportTask", "ID_");
            strTestDataJson = strTestDataJson.replace("Auto-SupporterTaskID", strSupporterTaskID);

        }

        if (strTestDataJson.contains("Auto-CategoryNameDB")) {
            strCategoryNameDB = dbMgmt.getInspectionCategory("Name");
            strTestDataJson = strTestDataJson.replace("Auto-CategoryNameDB", strCategoryNameDB);

        }

        if (strTestDataJson.contains("Auto-NameEng")) {
            String strNameEng = fk.letterify("?????????");
            strTestDataJson = strTestDataJson.replace("Auto-NameEng", strNameEng);

        }

        if (strTestDataJson.contains("Auto-FacilityName")) {
            strFacilityName = "Facility_" + fk.letterify("?????????");
            strTestDataJson = strTestDataJson.replace("Auto-FacilityName", strFacilityName);

        }

        if (strTestDataJson.contains("Auto-PersonFullNameEng")) {
            strPersonFullNameEng = fk.name().firstName() + " " + fk.name().lastName();
            strTestDataJson = strTestDataJson.replace("Auto-PersonFullNameEng", strPersonFullNameEng);
        }

        if (strTestDataJson.contains("Auto-PersonFullNameArb")) {
            strPersonFullNameArb = fkArb.name().firstName() + " " + fkArb.name().lastName();
            strTestDataJson = strTestDataJson.replace("Auto-PersonFullNameArb", strPersonFullNameArb);
        }

        if (strTestDataJson.contains("Auto-PersonNameEng")) {
            strPersonNameEng = fk.name().firstName();
            strTestDataJson = strTestDataJson.replace("Auto-PersonNameEng", strPersonNameEng);

        }

        if (strTestDataJson.contains("Auto-PersonNameArb")) {
            strPersonNameArb = fkArb.name().firstName();
            strTestDataJson = strTestDataJson.replace("Auto-PersonNameArb", strPersonNameArb);

        }

        if (strTestDataJson.contains("Auto-ViolationName")) {
            strViolationName = "AutoViolation_" + fk.letterify("??????????");
            strTestDataJson = strTestDataJson.replace("Auto-ViolationName", strViolationName);

        }

        if (strTestDataJson.contains("Auto-DBViolationName")) {
            strViolationDBName = "" + dbMgmt.getViolationName();
            strTestDataJson = strTestDataJson.replace("Auto-DBViolationName", strViolationDBName);
        }

        if (strTestDataJson.contains("Auto-ItemName")) {
            strInspItemName = "AutoItem_" + fk.letterify("??????????");
            strTestDataJson = strTestDataJson.replace("Auto-ItemName", strInspItemName);

        }

        if (strTestDataJson.contains("Auto-StreetName")) {
            String streetName = "" + fk.address().streetName();
            strTestDataJson = strTestDataJson.replace("Auto-StreetName", streetName);

        }

        if (strTestDataJson.contains("Auto-StreetNameArb")) {
            streetNameArb = "" + fkArb.address().streetName();
            strTestDataJson = strTestDataJson.replace("Auto-StreetNameArb", streetNameArb);

        }

        if (strTestDataJson.contains("Auto-InspItemNameDB")) {
            strInspItemNameDB = dbMgmt.getInspectionItems("Name");
            strTestDataJson = strTestDataJson.replace("Auto-InspItemNameDB", strInspItemNameDB);

        }

        if (strTestDataJson.contains("Auto-StringEng")) {
            strGeneralStringEng = "" + fk.letterify("????????????");
            strTestDataJson = strTestDataJson.replace("Auto-StringEng", strGeneralStringEng);
        }

        if (strTestDataJson.contains("Auto-StringArb")) {
            strGeneralStringArb = "" + fkArb.letterify("????????????");
            strTestDataJson = strTestDataJson.replace("Auto-StringArb", strGeneralStringArb);
        }

        if (strTestDataJson.contains("Auto-Number3")) {
            strAutoNumber3 = "" + fk.number().randomNumber(3, true);
            strTestDataJson = strTestDataJson.replace("Auto-Number3", strAutoNumber3);
        }

        if (strTestDataJson.contains("Auto-Number2")) {
            strAutoNumber2 = "" + fk.number().randomNumber(2, true);
            strTestDataJson = strTestDataJson.replace("Auto-Number2", strAutoNumber2);
        }

        if (strTestDataJson.contains("Auto-Number6")) {
            strAutoNumber6 = "" + fk.number().randomNumber(6, true);
            strTestDataJson = strTestDataJson.replace("Auto-Number6", strAutoNumber6);
        }

        if (strTestDataJson.contains("Auto-Number12")) {
            strAutoNumber12 = "" + fk.number().randomNumber(6, true) + fk.number().randomNumber(6, true);
            strTestDataJson = strTestDataJson.replace("Auto-Number12", strAutoNumber12);
        }

        if (strTestDataJson.contains("Auto-SupporterName")) {
            strSupporterName = dbMgmt.getUserNameByRole("FieldSupporter");
            strTestDataJson = strTestDataJson.replace("Auto-SupporterName", strSupporterName);
        }

        if (strTestDataJson.contains("Auto-NIN")) {
            strIdNum = "" + fk.idNumber().validNIN();
            strTestDataJson = strTestDataJson.replace("Auto-NIN", strIdNum);

        }

        if (strTestDataJson.contains("Auto-Mobile")) {
            strAutoMob = UtilitiesUI.fk.numerify("05########");
            strTestDataJson = strTestDataJson.replace("Auto-Mobile", strAutoMob);
        }

        if (strTestDataJson.contains("Auto-CenterID")) {
            strCenterID = dbMgmt.getInspectionCenterId();
            strTestDataJson = strTestDataJson.replace("Auto-CenterID", strCenterID);
        }

        if (strTestDataJson.contains("Auto-CenterName")) {
            strCenterName = dbMgmt.getInspectionCenterName();
            strTestDataJson = strTestDataJson.replace("Auto-CenterName", strCenterName);
        }

        if (strTestDataJson.contains("Auto-InspectorName")) {
            strInspectorName = dbMgmt.getUserNameByRole("Inspector");
            strTestDataJson = strTestDataJson.replace("Auto-InspectorName", strInspectorName);
        }

        if (strTestDataJson.contains("Auto-ApproverManagerName")) {
            strApproverManagerName = dbMgmt.getUserNameByRole("InspectionApproverManager");
            strTestDataJson = strTestDataJson.replace("Auto-ApproverManagerName", strApproverManagerName);
        }

        if (strTestDataJson.contains("Auto-ApproverName")) {
            strApproverName = dbMgmt.getUserNameByRole("InspectionApprover");
            strTestDataJson = strTestDataJson.replace("Auto-ApproverName", strApproverName);
        }

        if (strTestDataJson.contains("Auto-Date")) {
            DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 0);
            strAutoDate = date.format(calendar.getTime());
            strTestDataJson = strTestDataJson.replace("Auto-Date", strAutoDate);
        }

        if (strTestDataJson.contains("Auto-FutureDate")) {
            DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            strAutoDate = date.format(calendar.getTime());
            strTestDataJson = strTestDataJson.replace("Auto-FutureDate", strAutoDate);
        }

        if (strTestDataJson.contains("Auto-FacNameDB")) {
            strFacilityName = dbMgmt.getFacilityName();
            strTestDataJson = strTestDataJson.replace("Auto-FacNameDB", strFacilityName);
        }

        if (strTestDataJson.contains("Auto-ProcessedFacNameDB")) {
            strProcessedFacilityName = dbMgmt.getFacilityNameWithProcessedRequestNdViolation();
            strTestDataJson = strTestDataJson.replace("Auto-ProcessedFacNameDB", strProcessedFacilityName);
        }

        if (strTestDataJson.contains("Auto-FacNumberDB")) {
            strFacilityNumber = dbMgmt.getFacilityNumber();
            strTestDataJson = strTestDataJson.replace("Auto-FacNumberDB", strFacilityNumber);
        }

        dataRow.clear();
        dataRow.putAll(jsonStringToMap(strTestDataJson));

        ReporterX.info("Final  data is :- " + strTestDataJson);
        ReporterX.info("Preparing Test Data Ended ------->");
    }

    public static void waitForLoaderHidden(int timeout) throws InterruptedException {
        ActionX.Sync();

        ActionX.WaitUntilHidden(homePage.divLoading, timeout);
    }

    public static void selectFromPortalDropdownList(WebElement btnShowList, WebElement list, String strOption) throws Exception {
        if (Exists(btnShowList, 1)) {
            ActionX.ScrollToElementCenter(btnShowList);
            btnShowList.click();
            if (Exists(list, 1)) {
                WebElement option1 = list.findElement(By.cssSelector("span:nth-child(1)"));
                if (option1.getText().contains(strOption.trim())) {
                    ActionX.pressKey(Keys.ENTER);
                } else {
                    String strXpath = "//span[contains(text(),'" + strOption.trim() + "')]";
                    WebElement option = list.findElements(By.xpath(strXpath)).get(0);
                    ActionX.retryingFindClick(option);
                }
            }
        }
    }


    public static void selectWithInputText(WebElement btnShowList, WebElement txtInput, String strOption) throws Exception {
        if (ExistsInDOM(btnShowList)) {
            ActionX.ScrollToElementCenter(btnShowList);
            btnShowList.click();
            if (Exists(txtInput, 1)) {
                SetValue(txtInput, strOption);
                ActionX.pressKey(Keys.ENTER);
            }
        }
    }


}
