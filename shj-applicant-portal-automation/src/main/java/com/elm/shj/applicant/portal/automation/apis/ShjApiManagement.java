package com.elm.shj.applicant.portal.automation.apis;

import com.elm.qa.framework.core.DataManager;
import com.elm.qa.framework.runner.Executer;
import com.elm.qa.framework.utilities.API;
import com.elm.qa.framework.utilities.ReporterX;
import com.elm.shj.applicant.portal.automation.pages.UtilitiesUI;
import io.restassured.response.Response;

import java.sql.ResultSet;
import java.util.Hashtable;

import static com.elm.qa.framework.utilities.API.BuildRequest;

public class ShjApiManagement {



    public void addInspectionProcess() throws Exception {
        UtilitiesUI.prepareTestData(Executer.TestDataRow);
        Response res = BuildRequest();
        API.VerifyAPICall(res);

    }

    public String getAttachId() {
        Response res;
        try {
            ReporterX.info("----------- Starting attachment WS since previous WS has flag -'Auto-AttachID' -------------");
            Hashtable<String, String> htTemp = Executer.TestDataRow;
            Executer.TestDataRow = DataManager.GetExcelDataTable("Select * from Attachment where RowID=1").get(1);
            UtilitiesAPI.setAccessTokenInTestData();
            UtilitiesUI.prepareTestData(Executer.TestDataRow);
            res = BuildRequest();
            API.VerifyAPICall(res);
            Executer.TestDataRow = htTemp;

            ReporterX.info("------------ End Attachment WS ------------");

            String SQLQueryForGetAttachId = "SELECT TOP (1) Id FROM " + UtilitiesUI.fullTableName.get("FileInfo") + " order by CreationTime desc";
            ReporterX.info(SQLQueryForGetAttachId);
            ResultSet sourceRowSet = DataManager.GetDBResultSet(SQLQueryForGetAttachId);
            sourceRowSet.next();
            return sourceRowSet.getString("Id");
        } catch (Exception ex) {
            ReporterX.error(ex);
        }
        return "Attachment ID not found.";
    }

    public void createInspectionCentreWithoutRefId() throws Exception {
        UtilitiesAPI.setAccessTokenInTestData();
        UtilitiesAPI.callWebService();
    }

    public void submitInspectionProcessTask() throws Exception {
//        int a[] = {4, 6, 5, 1, 2, 3};
//        for (int i = 0; i < a.length; i++) {
//            //Loop 0 - Add Violation
//            //Loop 1 - Create Inspection Item
//            //Loop 2 - Edit Violation
//            //Loop 3 - Add Facility
//            //Loop 4 - Create Inspection task
//            //Loop 5 - Submit Inspection Task by Inspector
//            Executer.TestDataRow = Executer.MasterTestDataTable.get(a[i]);
//            UtilitiesAPI.setAccessTokenInTestData(); //login and update AccessToken in UserData
//            UtilitiesAPI.callWebService();
//
//        }

        UtilitiesAPI.setAccessTokenInTestData(); //login and update AccessToken in UserData
        UtilitiesAPI.callWebService();
    }

    public void SubmitInspectionProcessTaskWithoutViolationAndActions() throws Exception {
        int a[] = {4, 6, 5, 1, 2, 3};
        for (int i = 0; i < a.length; i++) {
            Executer.TestDataRow = Executer.MasterTestDataTable.get(a[i]);
            UtilitiesAPI.setAccessTokenInTestData();
            UtilitiesAPI.callWebService();
        }
    }

    public void SubmitInspectionProcessTaskWithoutViolationAndActions_UI() throws Exception {
        int a[] = {6, 8, 7, 1, 2, 3, 4};
        for (int i = 0; i < a.length; i++) {

            Executer.TestDataRow = Executer.MasterTestDataTable.get(a[i]);
            UtilitiesAPI.setAccessTokenInTestData();
            UtilitiesAPI.callWebService();
        }
    }

}
