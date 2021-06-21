package com.elm.shj.applicant.portal.automation.pages;


import com.elm.qa.framework.core.DataManager;
import com.elm.qa.framework.core.Global;
import com.elm.qa.framework.utilities.ReporterX;
import org.json.JSONObject;

import javax.sql.rowset.CachedRowSet;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import static org.testng.Assert.assertTrue;

public class DatabaseQueryManagement {


    public String getTenantId(String colName) {
        try {
            String SQLQuery = "Select * from " + UtilitiesUI.fullTableName.get("Tenant") + "where TenantUrl = '" + Global.Test.EnviromentVars.get("BASE_URL") + "'";
            ReporterX.info(SQLQuery);
            ResultSet rs = DataManager.GetDBResultSet(SQLQuery, 30);
            rs.next();
            if (colName == "JsonConfiguration") {
                String json = rs.getString(colName).replace("[", "").replace("]", "").trim();
                JSONObject jo = new JSONObject(json);
                jo.get("Value");
                ReporterX.info("Value is " + jo);
                return json;
            } else {
                String id = rs.getString(colName);
                ReporterX.info("Tenant id is " + id);
                return id;
            }
        } catch (Exception ex) {
            ReporterX.error(ex);
        }
        return null;
    }

    public String getNumberOfEmployees() {
        try {
            String numberOfEmployees = "SELECT TOP (1) NumberofEmployee FROM " + UtilitiesUI.fullTableName.get("Facility") + " where Id = '" + getFacilityId() + "'";
            ReporterX.info(numberOfEmployees);
            ResultSet rs = DataManager.GetDBResultSet(numberOfEmployees, 30);
            rs.next();
            String count = rs.getString("NumberofEmployee").trim();
            ReporterX.info("Employee count is: " + count);
            return count;
        } catch (Exception ex) {
            ReporterX.error(ex);
        }
        return "record not found in DB";
    }

    public String getFieldSupportProcessIdForSurvey() {
        try {
            if (!UtilitiesUI.strSupportId.equalsIgnoreCase("")) {
                return UtilitiesUI.strSupportId;
            }
            String getFieldSupportId = "SELECT [Id] FROM " + UtilitiesUI.fullTableName.get("FieldSupportProcess") + " where SurveyorProcessId = '" + getSurveyProcessId() + "'";
            ReporterX.info(getFieldSupportId);
            ResultSet rs = DataManager.GetDBResultSet(getFieldSupportId, 30);
            rs.next();
            UtilitiesUI.strSupportId = rs.getString("Id");
            return UtilitiesUI.strSupportId;
        } catch (Exception ex) {
            ReporterX.error(ex);
        }
        return "record not found in DB";
    }

    public String getFieldSupportProcessId() {
        try {
            if (!UtilitiesUI.strSupporterId.equalsIgnoreCase("")) {
                return UtilitiesUI.strSupporterId;
            }
            String fieldSupporterId = "SELECT [Id] FROM " + UtilitiesUI.fullTableName.get("FieldSupportProcess") + " where InspectionProcessId = '" + getInsProcessId() + "'";
            ReporterX.info(fieldSupporterId);
            ResultSet rs = DataManager.GetDBResultSet(fieldSupporterId, 40);
            rs.next();
            UtilitiesUI.strSupporterId = rs.getString("Id");
            return UtilitiesUI.strSupporterId;
        } catch (Exception ex) {
            ReporterX.error(ex);
        }
        return "Field support process Id not found.";
    }

    public String getFacilityBoardName() {
        try {
            if (!UtilitiesUI.strBoardName.equalsIgnoreCase("")) {
                return UtilitiesUI.strBoardName;
            }
            String facilityBoardName = "Select FacilityBoardName from " + UtilitiesUI.fullTableName.get("SurveyorCloseAction") + " where FieldSupportProcessId = '" + getFieldSupportProcessIdForSurvey() + "'";
            ResultSet rs = DataManager.GetDBResultSet(facilityBoardName, 60);
            rs.next();
            UtilitiesUI.strBoardName = rs.getString("FacilityBoardName");
            ReporterX.info("FacilityBoardName is" + UtilitiesUI.strBoardName);
            return UtilitiesUI.strBoardName;
        } catch (Exception ex) {
            ReporterX.error(ex);
        }
        return "Not found";
    }

    public String getInspectionItemsIdWithoutViolation() {
        try {
            //Adding wait because failing in bulk
            ReporterX.info("Wait for 15 secs for db update as query is not unique");
            Thread.sleep(15000);
            ReporterX.info("Wait ends");
            String SQLQueryForInsItem = "SELECT Id FROM " + UtilitiesUI.fullTableName.get("InspectionItem") + " order by CreationTime desc";
            ResultSet rs = DataManager.GetDBResultSet(SQLQueryForInsItem);
            rs.next();
            return rs.getString("Id");
        } catch (Exception ex) {
            ReporterX.error(ex);
            return "Inspection Item ID not found from DB";
        }

    }

    public String getFacilityName() {
        try {
            if (!UtilitiesUI.strFacilityName.equalsIgnoreCase("")) {
                return UtilitiesUI.strFacilityName;
            }

            String SQLQueryForFacilityName = "SELECT TOP (1) * FROM " + UtilitiesUI.fullTableName.get("Facility") + " order by CreationTime desc";
            ReporterX.info(SQLQueryForFacilityName);
            ResultSet rs = DataManager.GetDBResultSet(SQLQueryForFacilityName, 30);
            if (rs.next()) {
                UtilitiesUI.strFacilityName = rs.getString("Name");
                ReporterX.info("Facility name fetched from DB is :- " + UtilitiesUI.strFacilityName);
            }

            return UtilitiesUI.strFacilityName;
        } catch (Exception ex) {
            ReporterX.error(ex);
        }
        return "Facility name not found in DB";
    }

    public String getFacilityNameWithProcessedRequestNdViolation() {
        try {
            if (!UtilitiesUI.strProcessedFacilityName.equalsIgnoreCase("")) {
                return UtilitiesUI.strProcessedFacilityName;
            }

            String SQLQueryForFacilityName = "SELECT TOP (1) * FROM " + UtilitiesUI.fullTableName.get("InspectionProcess") + " where Status ='Processed' AND FacilityName != '' AND Notes != '' order by CreationTime desc";
            ReporterX.info(SQLQueryForFacilityName);
            ResultSet rs = DataManager.GetDBResultSet(SQLQueryForFacilityName, 30);
            if (rs.next()) {
                UtilitiesUI.strProcessedFacilityName = rs.getString("FacilityName");
                ReporterX.info("Facility name fetched from DB is :- " + UtilitiesUI.strProcessedFacilityName);
            }

            return UtilitiesUI.strProcessedFacilityName;
        } catch (Exception ex) {
            ReporterX.error(ex);
        }
        return "Facility name not found in DB";
    }

    public String getFacilityNumber() {
        try {
            if (!UtilitiesUI.strFacilityNumber.equalsIgnoreCase("")) {
                return UtilitiesUI.strFacilityNumber;
            }
            String SQLQueryForFacilityName = "SELECT TOP (1) * FROM " + UtilitiesUI.fullTableName.get("Facility") + " order by CreationTime desc";
            ReporterX.info(SQLQueryForFacilityName);
            ResultSet rs = DataManager.GetDBResultSet(SQLQueryForFacilityName, 30);
            if (rs.next()) {
                UtilitiesUI.strFacilityNumber = rs.getString("FacilityNumber");
                ReporterX.info("Facility name fetched from DB is :- " + UtilitiesUI.strFacilityName);
            }

            return UtilitiesUI.strFacilityNumber;
        } catch (Exception ex) {
            ReporterX.error(ex);
        }
        return "Facility Number not found in DB";
    }

    public String getFirstInspectionItemID() {
        try {

            String SQLQueryForInsItems = "SELECT * FROM " + UtilitiesUI.fullTableName.get("InspectionItem") + " Order by CreationTime asc";
            ReporterX.info(SQLQueryForInsItems);
            ResultSet rs = DataManager.GetDBResultSet(SQLQueryForInsItems, 30);
            rs.next();
            UtilitiesUI.strInspItemID = rs.getString("Id").toLowerCase();
            ReporterX.info("1st Inspection item ID fetched from DB is :-" + UtilitiesUI.strInspItemID);
            return UtilitiesUI.strInspItemID;
        } catch (Exception ex) {
            ReporterX.error(ex);
            return "Inspection Item 1 ID not found from DB";
        }

    }

    public List<String> getInsItems() {
        try {
            List<String> lstItems = new ArrayList<String>();
            String SQLQueryForInsItems = "SELECT A.[InspectionItemId] FROM " + UtilitiesUI.fullTableName.get("InspectionItemActivity") + " AS A INNER JOIN " + UtilitiesUI.fullTableName.get("FacilityActivity") + " AS B ON A.ActivityId = B.ActivityId WHERE B.FacilityId = '" + getFacilityId() + "'";
            ReporterX.info(SQLQueryForInsItems);
            ResultSet rs = DataManager.GetDBResultSet(SQLQueryForInsItems, 30);

            while (rs.next()) {
                String strInsRepItemId = rs.getString("InspectionItemId");
                lstItems.add(strInsRepItemId);
            }
            return lstItems;
        } catch (Exception ex) {
            ReporterX.error(ex);
            return null;
        }

    }

    public String getSurveyProcessId() {
        try {
            if (!UtilitiesUI.strSurveyId.equalsIgnoreCase("")) {
                return UtilitiesUI.strSurveyId;
            }

            String SQLQueryForSurveyorTaskID = "SELECT [Id] FROM " + UtilitiesUI.fullTableName.get("SurveyProcess") + " order by CreationTime desc";
            ReporterX.info(SQLQueryForSurveyorTaskID);
            ResultSet rs = DataManager.GetDBResultSet(SQLQueryForSurveyorTaskID, 30);
            rs.next();
            UtilitiesUI.strSurveyId = rs.getString("Id");
            return UtilitiesUI.strSurveyId;
        } catch (Exception ex) {
            ReporterX.error(ex);
        }
        return "Id not found";
    }

    public String getInspectionTypeId(String id) {
        try {
            String SQLQueryForInsType = "SELECT TOP (1) * FROM " + UtilitiesUI.fullTableName.get("InspectionType") + " where Id = " + id;
            ReporterX.info(SQLQueryForInsType);
            ResultSet rs = DataManager.GetDBResultSet(SQLQueryForInsType, 30);
            rs.next();
            String insType = rs.getString("Id");
            return insType;
        } catch (Exception ex) {
            ReporterX.fail("SQLQueryForInsType failed with error message " + ex.getMessage());
        }
        return "record not found in DB";
    }

    public String getIncidentIdStatus(String colName) {
        try {
            Thread.sleep(3000);
            String strQuery = "SELECT TOP (1) * FROM " + UtilitiesUI.fullTableName.get("Incident") + " where Id='" + UtilitiesUI.strIncidentId + "' ORDER BY CREATIONTIME DESC";
            ReporterX.info(strQuery);
            ResultSet rs = DataManager.GetDBResultSet(strQuery, 30);
            rs.next();
            String strIncidentIdStatus = rs.getString(colName);
            ReporterX.info("Incident Id status from DB is :- " + strIncidentIdStatus);
            return strIncidentIdStatus;

        } catch (Exception ex) {
            ReporterX.error(ex);
            return "Incident ID status not found.";
        }

    }

    public String getViolationTypeId() {
        try {


            Thread.sleep(5000);
            UtilitiesUI.strViolationTypeId = "";
            String SQLQueryForViolationType = "SELECT TOP (1) * FROM " + UtilitiesUI.fullTableName.get("ViolationType") + " ORDER BY CREATIONTIME DESC";
            //" ORDER BY CREATIONTIME DESC";
            ReporterX.info(SQLQueryForViolationType);
            ResultSet rs = DataManager.GetDBResultSet(SQLQueryForViolationType);

            if (rs.next()) {
                UtilitiesUI.strViolationTypeId = rs.getString("Id");
                ReporterX.info("getViolationTypeId from DB is :- " + UtilitiesUI.strViolationTypeId);
            }
            return UtilitiesUI.strViolationTypeId;

        } catch (Exception ex) {
            ReporterX.error(ex);
            return "Violation Type ID not found.";
        }

    }


    public String getViolationName() {
        try {
            if (!UtilitiesUI.strViolationDBName.equalsIgnoreCase("")) {
                return UtilitiesUI.strViolationDBName;
            }
            String strVioName = "";
            String strQuery = "Select Name from " + UtilitiesUI.fullTableName.get("ViolationType") + " ORDER BY CREATIONTIME DESC";
            ReporterX.info("DB Query is:- " + strQuery + "");
            ResultSet rs = DataManager.GetDBResultSet(strQuery, 30);
            if (rs.next()) {
                strVioName = rs.getString("Name");
                ReporterX.info("Violation name found from DB is : " + strVioName);
            }
            return strVioName;
        } catch (Exception ex) {
            ReporterX.error(ex);
        }
        return "Violation Name not found";
    }

    public String getFacilityId() {
        try {
            if (!UtilitiesUI.strFacilityId.equalsIgnoreCase("")) {
                return UtilitiesUI.strFacilityId;
            }
            String SQLQueryForFacilityId = "SELECT TOP (1) Id FROM " + UtilitiesUI.fullTableName.get("Facility") + "  order by CreationTime desc";
            ReporterX.info(SQLQueryForFacilityId);
            ResultSet rs = DataManager.GetDBResultSet(SQLQueryForFacilityId, 30);
            rs.next();
            UtilitiesUI.strFacilityId = rs.getString("Id");
            ReporterX.info("Facility Id fetched from DB is :- " + UtilitiesUI.strFacilityId);
            return UtilitiesUI.strFacilityId;
        } catch (Exception ex) {
            ReporterX.error(ex);
        }
        return "Facility Id not found in DB";
    }

    public String getInspectorId(String colName) {
        try {
            String SQLQueryForInspector = "SELECT TOP (1) Id FROM " + UtilitiesUI.fullTableName.get("User") + " where Status = 1 and Roles = '" + colName + "' order by CreationTime desc";
            ResultSet rs = DataManager.GetDBResultSet(SQLQueryForInspector);
            rs.next();
            String columnValue = rs.getString("Id");
            ReporterX.info("ColumnValue is " + columnValue);
            return columnValue;

        } catch (Exception ex) {
            ReporterX.error(ex);
        }
        return "record not found in DB";
    }

    public String getUserNameByRole(String strRole) {
        try {
            String columnValue = "";
            String SQLQueryForInspector = "SELECT TOP (1) FullName FROM " + UtilitiesUI.fullTableName.get("User") + " where Status = 1 and Roles = '" + strRole + "' order by CreationTime desc";
            ResultSet rs = DataManager.GetDBResultSet(SQLQueryForInspector);
            if (rs.next()) {
                columnValue = rs.getString("FullName");
                ReporterX.info("ColumnValue is " + columnValue);
            }
            return columnValue;

        } catch (Exception ex) {
            ReporterX.error(ex);
        }
        return "record not found in DB";
    }

    public String getInspectionCenterId() throws Exception {
        if (!UtilitiesUI.strCenterID.equalsIgnoreCase("")) {
            return UtilitiesUI.strCenterID;
        }
        String getCount = "SELECT count (Id) as count FROM " + UtilitiesUI.fullTableName.get("InspectionCenter");
        ReporterX.info(getCount);
        ResultSet rs = DataManager.GetDBResultSet(getCount);
        rs.next();
        if (rs.getString("count").equalsIgnoreCase("0")) {
            UtilitiesUI.AddInspectionCentreAPI(); //Create Inspection center if no data in DB
        }
        String SQLQueryForInspectionCenterId = "SELECT TOP (1) Id FROM " + UtilitiesUI.fullTableName.get("InspectionCenter") + " order by CreationTime desc";
        ReporterX.info(SQLQueryForInspectionCenterId);
        rs = DataManager.GetDBResultSet(SQLQueryForInspectionCenterId);
        rs.next();
        return rs.getString("Id");
    }

    public String getInspectionCenterName() throws Exception {
        if (!UtilitiesUI.strCenterName.equalsIgnoreCase("")) {
            return UtilitiesUI.strCenterName;
        }
        String getCount = "SELECT count (Id) as count FROM " + UtilitiesUI.fullTableName.get("InspectionCenter");
        ReporterX.info(getCount);
        ResultSet rs = DataManager.GetDBResultSet(getCount);
        rs.next();
        if (rs.getString("count").equalsIgnoreCase("0")) {
            UtilitiesUI.AddInspectionCentreAPI(); //Create Inspection center if no data in DB
        }
        String SQLQueryForInspectionCenterId = "SELECT TOP (1) Name FROM " + UtilitiesUI.fullTableName.get("InspectionCenter") + " order by CreationTime desc";
        ReporterX.info(SQLQueryForInspectionCenterId);
        rs = DataManager.GetDBResultSet(SQLQueryForInspectionCenterId);
        rs.next();
        return rs.getString("Name");
    }

    public String getInsProcessId() {
        try {
            if (!UtilitiesUI.strInsProcessId.equalsIgnoreCase("")) {
                return UtilitiesUI.strInsProcessId;
            }
            String SQLQueryForInsProcessId = "SELECT TOP (1) Id FROM " + UtilitiesUI.fullTableName.get("InspectionProcess") + " where FacilityId = '" + getFacilityId() + "' ORDER BY CreationTime DESC";
            ReporterX.info(SQLQueryForInsProcessId);
            ResultSet rs = DataManager.GetDBResultSet(SQLQueryForInsProcessId, 30);
            rs.next();
            UtilitiesUI.strInsProcessId = rs.getString("Id");
            ReporterX.info(UtilitiesUI.strInsProcessId);
            return UtilitiesUI.strInsProcessId;
        } catch (Exception ex) {
            ReporterX.error(ex);
        }
        return "record not found in DB";
    }

    public String getInsProcessReportId() {
        try {

            String SQLQueryForInsProcessReportId = "SELECT Id FROM " + UtilitiesUI.fullTableName.get("InspectionProcessReport") + " where InspectionProcessId = '" + getInsProcessId() + "'";
            ResultSet rs = DataManager.GetDBResultSet(SQLQueryForInsProcessReportId, 30);
            rs.next();
            return rs.getString("Id");
        } catch (Exception ex) {
            ReporterX.error(ex);
        }
        return "record not found in DB";
    }

    public String getInspectionCategory(String colName) throws Exception {
        try {
            if (!UtilitiesUI.strCategoryNameDB.equalsIgnoreCase("")) {
                return UtilitiesUI.strCategoryNameDB;
            }
            String value = "";
            String SQLQueryForInsItems = "SELECT TOP (1) * FROM " + UtilitiesUI.fullTableName.get("InspectionCategory") + " Order by CreationTime Desc";
            ReporterX.info(SQLQueryForInsItems);
            ResultSet rs = DataManager.GetDBResultSet(SQLQueryForInsItems, 30);
            if (rs.next()) {
                value = rs.getString(colName);
                ReporterX.info("Value fetched from DB is :-" + value);
            }
            return value;
        } catch (Exception ex) {
            ReporterX.error(ex);
            return "Inspection Item Category value not found from DB";
        }
    }


    public String getAttachID() throws Exception {
        try {
            if (!UtilitiesUI.strAttachID.equalsIgnoreCase("")) {
                return UtilitiesUI.strAttachID;
            }
            String value = "";
            String SQLQueryForInsItems = "SELECT TOP (1) Id FROM " + UtilitiesUI.fullTableName.get("FileInfo") + " Order by CreationTime Desc";
            ReporterX.info(SQLQueryForInsItems);
            ResultSet rs = DataManager.GetDBResultSet(SQLQueryForInsItems, 30);
            if (rs.next()) {
                value = rs.getString("Id");
                ReporterX.info("Value fetched from DB is :-" + value);
            }
            return value;
        } catch (Exception ex) {
            ReporterX.error(ex);
            return "Attach ID not found from DB";
        }
    }

    public String getTaskIdByTaskDefName(String TaskDefName, String colName) {
        try {
            String strValue = "";
            String SQLQueryForTask = "SELECT TOP (1) " + colName + " FROM " + UtilitiesUI.fullTableName.get("ACT_RU_TASK") + " where TASK_DEF_KEY_ = '" + TaskDefName + "' AND DESCRIPTION_ LIKE '%" + getFacilityName() + "%' order by CREATE_TIME_ desc";
            ReporterX.info(SQLQueryForTask);
            ResultSet rs = DataManager.GetDBResultSet(SQLQueryForTask, 300);
            if (rs.next()) {
                strValue = rs.getString(colName).toLowerCase();
                ReporterX.info("Auto Task ID received from DB is :- " + UtilitiesUI.strInspectorTaskID);
            }
            return strValue;
        } catch (Exception ex) {
            ReporterX.error(ex);
        }
        return "Task ID Not Found in DB";
    }

    public String getInspectionItemsID() {
        try {
            if (!UtilitiesUI.strInspItemID.equalsIgnoreCase("")) {
                return UtilitiesUI.strInspItemID;
            }
            String SQLQueryForInsItems = "SELECT TOP (1) Id FROM " + UtilitiesUI.fullTableName.get("InspectionItem") + " where RelatedViolationId = '" + getViolationTypeId() + "' Order by CreationTime Desc";
            ReporterX.info(SQLQueryForInsItems);
            ResultSet rs = DataManager.GetDBResultSet(SQLQueryForInsItems, 30);
            rs.next();
            UtilitiesUI.strInspItemID = rs.getString("Id");
            ReporterX.info("Inspection item ID fetched from DB is :-" + UtilitiesUI.strInspItemID);
            return UtilitiesUI.strInspItemID;
        } catch (Exception ex) {
            ReporterX.error(ex);
            return "Inspection Item ID not found from DB";
        }

    }

    public String getInspectionItems(String colName) {
        try {
            if (!UtilitiesUI.strInspItemNameDB.equalsIgnoreCase("")) {
                return UtilitiesUI.strInspItemNameDB;
            }
            Thread.sleep(2000);
            String value = "";
            String SQLQueryForInsItems = "SELECT TOP (1) * FROM " + UtilitiesUI.fullTableName.get("InspectionItem") + " where Status = 'Active' AND ClientConfiguration Not Like '%shape%' Order by CreationTime Desc";
            ReporterX.info(SQLQueryForInsItems);
            ResultSet rs = DataManager.GetDBResultSet(SQLQueryForInsItems, 30);
            if (rs.next()) {
                value = rs.getString(colName);
                ReporterX.info("Value fetched from DB is :-" + value);
            }
            return value;
        } catch (Exception ex) {
            ReporterX.error(ex);
            return "Inspection Item value not found from DB";
        }

    }

    public String getInspectionItemWithoutViolation(String colName) {
        try {
            //Adding wait because failing in bulk
            String SQLQueryForInsItem = "SELECT TOP(1) * FROM " + UtilitiesUI.fullTableName.get("InspectionItem") + " order by CreationTime desc";
            ResultSet rs = DataManager.GetDBResultSet(SQLQueryForInsItem, 60);
            rs.next();
            return rs.getString(colName).toLowerCase();
        } catch (Exception ex) {
            ReporterX.error(ex);
            return "Inspection Item  not found from DB";
        }

    }

    public String getWorkerActionId() {
        try {

            String SQLQueryForWorkerActionId = "SELECT Id FROM " + UtilitiesUI.fullTableName.get("StopWorkersAction_Process") + " where InspectionProcessReportId = '" + getInsProcessReportId() + "'";
            ResultSet rs = DataManager.GetDBResultSet(SQLQueryForWorkerActionId, 30);
            rs.next();
            return rs.getString("Id");
        } catch (Exception ex) {
            ReporterX.error(ex);
        }
        return "record not found in DB";
    }

    public String getInsCentreIdFromUserTable() {
        try {

            String SQLQueryForInsCentreId = "SELECT [InspectionCenterId] FROM " + UtilitiesUI.fullTableName.get("UserInspectionCenter") + " where UserId in (SELECT [Id] FROM " + UtilitiesUI.fullTableName.get("User") + " where Roles = 'CenterAdmin')";
            ReporterX.info(SQLQueryForInsCentreId);
            CachedRowSet rs = DataManager.GetDBResultSet(SQLQueryForInsCentreId, 30);
            if (rs.size() == 0) {
                return "0";
            } else {
                rs.next();
                UtilitiesUI.strInsCentreID = rs.getString("InspectionCenterId");
                ReporterX.info("Inspection centre id received from DB is :- " + UtilitiesUI.strInsCentreID);
            }
        } catch (Exception ex) {
            ReporterX.error(ex);
        }
        return UtilitiesUI.strInsCentreID;
    }

    public String getFacilityViolationData(String strColName) {
        try {
            String strQuery = "SELECT * FROM " + UtilitiesUI.fullTableName.get("FacilityViolations") + " where InspectionProcessId= '" + getInsProcessId() + "'";
            ReporterX.info(strQuery);
            Thread.sleep(2000);
            ResultSet rs = DataManager.GetDBResultSet(strQuery, 30);
            rs.next();
            String strValue = rs.getString(strColName);
            if (strColName == "ViolationValue") {
                String trimValue = strValue.replace(".00", "").trim();
                ReporterX.info(strColName + " Value received from DB is :- " + trimValue);
                return strValue;
            } else {
                ReporterX.info(strColName + " Value received from DB is :- " + strValue);
                return strValue;
            }
        } catch (Exception ex) {

            ReporterX.error(ex);
        }
        return "Facility Violations record not found in DB.";
    }

    public String getPymntFacilityId() {
        try {
            String getFacilityId = "Select FacilityId from " + UtilitiesUI.fullTableName.get("FacilityViolations") + " where status = 'paid' order by CreationTime desc";
            ReporterX.info("DB Query for getPymntFacilityId is:- " + getFacilityId + "");
            ResultSet rs = DataManager.GetDBResultSet(getFacilityId, 30);
            rs.next();
            return rs.getString("FacilityId");
        } catch (Exception ex) {
            ReporterX.error(ex);
        }
        return "getPymntFacilityId not found in DB";
    }

    public String getPymntViolationNo() {
        try {
            Thread.sleep(5000);
            String getViolationNumber = "Select ViolationNumber from " + UtilitiesUI.fullTableName.get("FacilityViolations") + " where ViolationTypeId = '" + getViolationTypeId() + "' order by CreationTime desc";
            ReporterX.info(getViolationNumber);
            ReporterX.info("DB Query for getPymntViolationNo is:- " + getViolationNumber + "");
            ResultSet rs = DataManager.GetDBResultSet(getViolationNumber, 30);
            rs.next();
            return rs.getString("ViolationNumber");
        } catch (Exception ex) {
            ReporterX.error(ex);
        }
        return "Violation Number not found in DB";
    }

    public String getUserDetailFromTenantUserTable(String strUserRole, String colName) {
        try {
            //Order by asc - to fetch old user mobile if two Inspector users exist
            String SQLQuery = "SELECT TOP (1) " + colName + " from " + UtilitiesUI.fullTableName.get("User") + " where Roles like '%" + strUserRole + "%' order by CreationTime asc";
            ReporterX.info(SQLQuery);
            ResultSet rs = DataManager.GetDBResultSet(SQLQuery, 30);
            rs.next();
            String detail = rs.getString(colName);
            ReporterX.info("Mob No. fetched from DB is :- " + detail);
            return detail;

        } catch (Exception ex) {
            ReporterX.error(ex);
        }
        return "No Record found in DB";

    }

    public String violationCreationTime() throws Exception {
        String getAppealPeriod = "Select JsonConfiguration from " + UtilitiesUI.fullTableName.get("Tenant") + " where TenantUrl = '" + Global.Test.EnviromentVars.get("URL").trim() + "'";
        ReporterX.info(getAppealPeriod);
        ResultSet rs = DataManager.GetDBResultSet(getAppealPeriod, 30);
        rs.next();
        String appeal = rs.getString("JsonConfiguration");
        String firstSplit = appeal.split(":")[2];
        String secondSplit = firstSplit.replace("}]", "").replace("\"", "").trim();
        int appealDate = Integer.parseInt(secondSplit);
        int appealPeriod = appealDate + 1;
        DateFormat date = new SimpleDateFormat("yyyy/MM/dd 00:00:00");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -appealPeriod);
        String violationTime = date.format(calendar.getTime());
        ReporterX.info("Violation creation time is  " + violationTime);
        return violationTime;
    }

    public String getCloseActionId() {
        try {
            String strValue = "";
            String SqlQueryForCloseActionId = "Select TOP (1) Id from " + UtilitiesUI.fullTableName.get("CloseFacilityAction_FieldSupport") + " where FacilityId = '" + getFacilityId() + "' order by CreationTime desc";
            ResultSet rs = DataManager.GetDBResultSet(SqlQueryForCloseActionId, 30);
            if (rs.next()) {
                strValue = rs.getString("Id");
            }
            return strValue;
        } catch (Exception ex) {
            ReporterX.error(ex);
        }
        return "record not found in DB";
    }

    public void deleteFacility() {

        String strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("FacilityLicense") + " where FacilityId = '" + getFacilityId() + "'";
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("FacilityOwner") + " where FacilityId = '" + getFacilityId() + "'";
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "Delete " + UtilitiesUI.fullTableName.get("FacilityWorker") + " where FacilityId = '" + getFacilityId() + "'";
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "Delete " + UtilitiesUI.fullTableName.get("WorkTime") + " where FacilityId = '" + getFacilityId() + "'";
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("FacilityActivity") + " where FacilityId = '" + getFacilityId() + "'";
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("FacilityViolations") + " where FacilityId = '" + getFacilityId() + "'";
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("FacilityVisits") + " where FacilityId = '" + getFacilityId() + "'";
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("OrganizationPhoto") + " where FacilityId = '" + getFacilityId() + "'";
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("Facility") + " where Id = '" + getFacilityId() + "'";
        int iCount = DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);
        if (iCount <= 0) {
            ReporterX.info("Failed to execute query = " + strQuery);
        } else {
            ReporterX.info("Facility deleted successfully");
        }


    }

    public void deleteFacilityFullTable() {


        ReporterX.info("Delete Facilities  ----->");

        String strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("FacilityLicense") + " ;";
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("FacilityOwner")+ " ;";
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "Delete FROM" + UtilitiesUI.fullTableName.get("FacilityWorker")+ " ;";
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("WorkTime")+ " ;";
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("FacilityActivity")+ " ;";
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "Delete FROM  " + UtilitiesUI.fullTableName.get("GenericAction")+ " ;";
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("FacilityViolations")+ " ;";
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("FacilityVisits")+ " ;";
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("OrganizationPhoto")+ " ;";
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("Audit")+ " ;";
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("FacilityClosure")+ " ;";
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("Facility")+ " ;";
        int count = DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        ReporterX.info("Facilities deleted Rows : " + count);


    }

    public void deleteInspectionItemIdWithoutViolation() {

        ReporterX.info("Inspection item delete begins");
        String strInspectionID = getInspectionItemsIdWithoutViolation();

        String strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("InspectionItemSpecialization") + " where InspectionItemId = '" + strInspectionID + "'";
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "DELETE FROM " + UtilitiesUI.fullTableName.get("InspectionItemActivity") + " where InspectionItemId = '" + strInspectionID + "'";
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "DELETE FROM " + UtilitiesUI.fullTableName.get("InspectionItemProcessType");
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "DELETE FROM " + UtilitiesUI.fullTableName.get("InspectionItem") + " where id = '" + strInspectionID + "'";
        int count = DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);
        if (count <= 0) {
            ReporterX.info("Failed to execute query = " + strQuery);
        } else
            ReporterX.info("Inspection item deleted successfully");

    }


    public void deleteTenantInboxTasks() {


        ReporterX.info("Delete Tenant Tasks ----->");

        String deleteTasks = "Delete FROM " + UtilitiesUI.fullTableName.get("ACT_RU_IDENTITYLINK") + " where  TENANT_ID_='3604bbb8-0601-414e-8360-24328a1b837f'";
        DataManager.ExecuteSqlDMLQuery("dbConString", deleteTasks);

        deleteTasks = "Delete FROM " + UtilitiesUI.fullTableName.get("ACT_RU_TASK") + "  where  TENANT_ID_='3604bbb8-0601-414e-8360-24328a1b837f' ";
        int iCount = DataManager.ExecuteSqlDMLQuery("dbConString", deleteTasks);


        ReporterX.info("Tenant Assigned Tasks deleted Rows : " + iCount);


    }

    public void deleteSurveyTasks() {


        ReporterX.info("Delete Survey Tasks ----->");

        String deleteSurveyTask = "Delete from " + UtilitiesUI.fullTableName.get("SurveyLocation");
        DataManager.ExecuteSqlDMLQuery("dbConString", deleteSurveyTask);

        deleteSurveyTask = "Delete from " + UtilitiesUI.fullTableName.get("SurveyFacilityAction");
        DataManager.ExecuteSqlDMLQuery("dbConString", deleteSurveyTask);

        deleteSurveyTask = "Delete from " + UtilitiesUI.fullTableName.get("SpatialPolygonGeometry");
        DataManager.ExecuteSqlDMLQuery("dbConString", deleteSurveyTask);

        deleteSurveyTask = "Delete from " + UtilitiesUI.fullTableName.get("SpatialLineGeometry");
        DataManager.ExecuteSqlDMLQuery("dbConString", deleteSurveyTask);

        deleteSurveyTask = "Delete from " + UtilitiesUI.fullTableName.get("Log_Surveyor");
        DataManager.ExecuteSqlDMLQuery("dbConString", deleteSurveyTask);

        deleteSurveyTask = "Delete from " + UtilitiesUI.fullTableName.get("SurveyFacilityClosureAttachment");
        DataManager.ExecuteSqlDMLQuery("dbConString", deleteSurveyTask);

        deleteSurveyTask = "Delete from " + UtilitiesUI.fullTableName.get("SurveyFacilityClosureStaffList");
        DataManager.ExecuteSqlDMLQuery("dbConString", deleteSurveyTask);

        deleteSurveyTask = "Delete from " + UtilitiesUI.fullTableName.get("SurveyFacilityClosure");
        DataManager.ExecuteSqlDMLQuery("dbConString", deleteSurveyTask);

        deleteSurveyTask = "Delete from " + UtilitiesUI.fullTableName.get("SurveyProcess");
        int iCount = DataManager.ExecuteSqlDMLQuery("dbConString", deleteSurveyTask);

        ReporterX.info("Survey Tasks deleted Rows : " + iCount);


    }

    public String getViolationTypeIdForDeleteAction() {
        try {
            if (UtilitiesUI.strViolationTypeId != "") {
                return UtilitiesUI.strViolationTypeId;
            }
            String SQLQueryForViolationType = "SELECT TOP (1) [Id] FROM " + UtilitiesUI.fullTableName.get("ViolationType") + " ORDER BY CREATIONTIME DESC";
            ResultSet rs = DataManager.GetDBResultSet(SQLQueryForViolationType, 30);
            rs.next();
            UtilitiesUI.strViolationTypeId = rs.getString("Id").toLowerCase();
            ReporterX.info("getViolationTypeId from DB is :- " + UtilitiesUI.strViolationTypeId);
            return UtilitiesUI.strViolationTypeId;

        } catch (Exception ex) {
            ReporterX.error(ex);
            return "Violation Type ID not found.";
        }

    }

    public String getInspectionItemsIDForDeleteAction() {
        try {
            if (UtilitiesUI.strInspItemID != "") {
                return UtilitiesUI.strInspItemID;
            }
            String SQLQueryForInsItems = "SELECT Id FROM " + UtilitiesUI.fullTableName.get("InspectionItem") + " where RelatedViolationId = '" + getViolationTypeIdForDeleteAction() + "'";
            ResultSet rs = DataManager.GetDBResultSet(SQLQueryForInsItems, 30);
            rs.next();
            UtilitiesUI.strInspItemID = rs.getString("Id").toLowerCase();
            ReporterX.info("Inspection item ID fetched from DB is :-" + UtilitiesUI.strInspItemID);
            return UtilitiesUI.strInspItemID;
        } catch (Exception ex) {
            ReporterX.error(ex);
            return "Inspection Item ID not found from DB";
        }

    }

    public void DeleteInspectionItemID() {

        try {
            String strInspectionID = getInspectionItemsIDForDeleteAction();

            String strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("InspectionItemSpecialization") + " where InspectionItemId = '" + strInspectionID + "'";
            DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

            strQuery = "DELETE FROM " + UtilitiesUI.fullTableName.get("InspectionItemActivity") + " where InspectionItemId = '" + strInspectionID + "'";
            DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

            strQuery = "DELETE FROM " + UtilitiesUI.fullTableName.get("InspectionItem") + " where id = '" + strInspectionID + "'";
            DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        } catch (Exception ex) {
            DataManager.ExecuteSqlDMLQuery("dbConString", "Delete FROM " + UtilitiesUI.fullTableName.get("InspectionItemSpecialization"));
            DataManager.ExecuteSqlDMLQuery("dbConString", "DELETE FROM " + UtilitiesUI.fullTableName.get("InspectionItemActivity"));
            DataManager.ExecuteSqlDMLQuery("dbConString", "DELETE FROM " + UtilitiesUI.fullTableName.get("InspectionItem"));

        }
    }

    public void deleteInspectionItemIDs() {


        ReporterX.info("Delete Inspection Item IDs ----->");

        String strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("InspectionItemSpecialization");
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "DELETE FROM " + UtilitiesUI.fullTableName.get("InspectionItemActivity");
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "DELETE FROM " + UtilitiesUI.fullTableName.get("InspectionItemProcessType");
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "DELETE FROM " + UtilitiesUI.fullTableName.get("InspectionItem");
        int iCount = DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        ReporterX.info("Inspection Items deleted Rows : " + iCount);


    }

    public void deleteViolations() {
        ReporterX.info("Delete Violations ----->");

        // delete  FROM [Release_india.InspectionPlatform].[Penalties].[ViolationType] for Old 2.3 Version.
        String deleteViolations = "DELETE FROM " + UtilitiesUI.fullTableName.get("ViolationTypeVersions");
        DataManager.ExecuteSqlDMLQuery("dbConString", deleteViolations);

        deleteViolations = "DELETE FROM " + UtilitiesUI.fullTableName.get("ViolationType");
        DataManager.ExecuteSqlDMLQuery("dbConString", deleteViolations);

        deleteViolations = "DELETE FROM " + UtilitiesUI.fullTableName.get("Log_Penalties");
        int iCount = DataManager.ExecuteSqlDMLQuery("dbConString", deleteViolations);

        ReporterX.info("Violations deleted Rows : " + iCount);

    }

    public void deleteInsProcess() {
        try {

            String strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("InspectionReportItemsValues") + " where InspectionProcessReportId = '" + getInsProcessReportId() + "'";
            DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

            strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("InspectionReportActions") + " where InspectionProcessReportId = '" + getInsProcessReportId() + "'";
            DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

            strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("SampleProductsAction_Process") + " where InspectionProcessId = '" + getInsProcessId() + "'";
            DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

            strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("ConfiscateProductsAction_Process") + " where InspectionProcessId = '" + getInsProcessId() + "'";
            DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

            strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("StopWorkersAction_Process") + " where InspectionProcessId = '" + getInsProcessId() + "'";
            DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

            strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("DestroyProductsAction_Process") + " where InspectionProcessId = '" + getInsProcessId() + "'";
            DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

            strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("OwnerSignInfo") + " where InspectionProcessId = '" + getInsProcessId() + "'";
            DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

            strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("InspectionProcessFile") + " where InspectionProcessId = '" + getInsProcessId() + "'";
            DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

            strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("InspectionProcessReport") + " where InspectionProcessId = '" + getInsProcessId() + "'";
            DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

            strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("InspectionProcessInspector") + " where InspectionProcessId = '" + getInsProcessId() + "'";
            DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

            strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("InspectionReportItems") + " where InspectionProcessId = '" + getInsProcessId() + "'";
            DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

            strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("InspectionProcess") + " where Id = '" + getInsProcessId() + "'";
            DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);


        } catch (Exception ex) {
            ReporterX.error(ex);
        }
    }

    public void deleteInsProcessFullTable() {

        ReporterX.info("Delete Inspection Process  ----->");

        String strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("InspectionReportItemsValues");
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("InspectionReportActions");
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("SampleProductsAction_Process");
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("ConfiscateProductsAction_Process");
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("StopWorkersAction_Process");
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("DestroyProductsAction_Process");
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("OwnerSignInfo");
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("InspectionProcessFile");
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("InspectionProcessReport");
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("InspectionProcessInspector");
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("InspectionReportItems");
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        strQuery = "Delete FROM " + UtilitiesUI.fullTableName.get("InspectionProcess");
        int iCount = DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

        ReporterX.info("Inspection Process deleted Rows : " + iCount);


    }

   /* public String deleteAuthority() {
        try {
            String deleteSQLQuery = null;
            deleteSQLQuery = "Delete from " + fullTableName.get("AspNetUsers_Admin") + " where Email = '" + UtilitiesUI.strAutoEmail + "'";
            DataManager.ExecuteSqlDMLQuery("dbConString", deleteSQLQuery);

            deleteSQLQuery = "Delete from " + fullTableName.get("Authority") + " where Name = '" + UtilitiesUI.strAutoName + "'";
            int count = DataManager.ExecuteSqlDMLQuery("dbConString", deleteSQLQuery);
            if (count <= 0) {
                ReporterX.failSoft("DB deletion query failed");
            } else {
                ReporterX.pass("DB Records are deleted successfully");
            }

        } catch (Exception ex) {
            ReporterX.error(ex);
        }
        return "record not found in DB";
    }*/

    public void deleteInspectionCentre() {
        String strQuery = "";

        try {
            String strIspCentreId = getInsCentreIdFromUserTable();
            if (strIspCentreId != "0") {

                strQuery = "delete FROM " + UtilitiesUI.fullTableName.get("RoleEmails") + " where InspectionCenterId not in ('" + strIspCentreId + "')";
                ReporterX.info(strQuery);
                DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

                strQuery = "delete FROM " + UtilitiesUI.fullTableName.get("InspectionCenterCoverage") + " where InspectionCenterId not in ('" + strIspCentreId + "')";
                ReporterX.info(strQuery);
                DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);

                strQuery = "delete FROM " + UtilitiesUI.fullTableName.get("InspectionCenter") + " where Id not in ('" + strIspCentreId + "')";
                ReporterX.info(strQuery);
                DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);
            }
        } catch (Exception ex) {

            ReporterX.error(ex);
        }

    }

    public void deleteInspectionCentreForUser() throws Exception {
        String strQuery = "delete FROM " + UtilitiesUI.fullTableName.get("UserInspectionCenter") + " where UserId = '" + getInspectorId("CloseReviewUser") + "' and InspectionCenterId = '" + getInspectionCenterId() + "'";
        ReporterX.info(strQuery);
        DataManager.ExecuteSqlDMLQuery("dbConString", strQuery);
    }


}

