package com.elm.shj.applicant.portal.automation.scenarios;


import com.elm.qa.framework.runner.Executer;
import com.elm.qa.framework.utilities.ReporterX;
import com.elm.shj.applicant.portal.automation.pages.*;
import com.elm.shj.applicant.portal.automation.pages.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;


public class TestScenarios {

    //Identifying objects:
    SystemLogin systemLogin = new SystemLogin();
    Navigators navigators = new Navigators();
    RoleManagement roleManagement = new RoleManagement();
    UserManagement userManagement = new UserManagement();
    PrintingManagement printingManagement = new PrintingManagement();





    //region Before and After Execution

    @AfterClass
    public void doLogout(){
        try{

            systemLogin.LogOut();
        }catch (Exception e){
            ReporterX.error(e);
        }
    }

//    @BeforeClass
//    public void Initialization() {
//        try {
//
//            ReporterX.info("Build Number: " + System.getenv("BUILD_NUMBER"));
//            ReporterX.info("Job Name: " + System.getenv("JOB_NAME"));
//
//            //Filling Full Database Table names from Excel to fullTableName hashtable
//            UtilitiesUI.fullTableName = new Hashtable<String,String>();
//            Hashtable<Integer, Hashtable<String, String>> htDBTableData = DataManager.GetExcelDataTable("Select Key,FinalTableName from DBTables");
//            for (int i = 1; i <= htDBTableData.size(); i++) {
//                UtilitiesUI.fullTableName.put(htDBTableData.get(i).get("Key".toUpperCase()), htDBTableData.get(i).get("FinalTableName".toUpperCase()));
//            }
//            //Updating DBServerIP in Config.properties file as per Excel value
//             if (!Global.Test.EnviromentVars.get("DBServerIP").trim().equalsIgnoreCase(DataManager.getPropertyFile("DBServerIP"))) {
//                Path path = Paths.get("Configuration/config.properties");
//                String content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
//                content = content.replaceAll(DataManager.getPropertyFile("DBServerIP"), Global.Test.EnviromentVars.get("DBServerIP"));
//                Files.write(path, content.getBytes(StandardCharsets.UTF_8));
//            }
//
//           //Cleaning DB
//            if(Global.Test.EnviromentVars.get("CleanDB").equalsIgnoreCase("yes")) {
//                ReporterX.info("<----- Cleaning DB Started ----->");
//
//                dbMgmt.deleteSurveyTasks();
//                dbMgmt.deleteInspectionItemIDs();
//                dbMgmt.deleteViolations();
//                dbMgmt.deleteFacilityFullTable();
//                dbMgmt.deleteInsProcessFullTable();
//                dbMgmt.deleteTenantInboxTasks();
//
//                ReporterX.info("<----- Cleaning DB Completed ----->");
//
//            }
//            // dbMgmt.deleteInspectionCentre();
//            UtilitiesUI.resetVariables();
//
//            ReporterX.info("@BeforeClass executed Successfully!");
//
//        } catch (Exception e) {
//            ReporterX.error(e);
//        }
//    }

    //endregion

    //region Role Management

    @Test
    public void ValidateSystemLogin() {
        try {

            ReporterX.info("Validate System Login");

            systemLogin.SignIn(Executer.TestDataRow);

        } catch (Exception ex) {
            ReporterX.error(ex);
        }
    }

    @Test
    public void ValidateAddUserRole() {
        try {
            systemLogin.SignIn(Executer.TestDataRow.get("LoginRow".toUpperCase()));
            navigators.goToRoleManagement();
            roleManagement.addNewUserRole(Executer.TestDataRow);

        } catch (Exception ex) {
            ReporterX.error(ex);
        }
    }

    @Test
    public void ValidateEditUserRole() {
        try {
            systemLogin.SignIn(Executer.TestDataRow.get("LoginRow".toUpperCase()));
            navigators.goToRoleManagement();
            roleManagement.editUserRole(Executer.TestDataRow);

        } catch (Exception ex) {
            ReporterX.error(ex);
        }
    }

    @Test
    public void ValidateChangeStatusUserRole() {
        try {
            systemLogin.SignIn(Executer.TestDataRow.get("LoginRow".toUpperCase()));
            navigators.goToRoleManagement();
            roleManagement.changeStatusUserRole(Executer.TestDataRow);

        } catch (Exception ex) {
            ReporterX.error(ex);
        }
    }

    @Test
    public void ValidateDeleteUserRole() {
        try {
            systemLogin.SignIn(Executer.TestDataRow.get("LoginRow".toUpperCase()));
            navigators.goToRoleManagement();
            roleManagement.deleteUserRole(Executer.TestDataRow);

        } catch (Exception ex) {
            ReporterX.error(ex);
        }
    }

    @Test
    public void ValidateViewDetailsUserRole() {
        try {
            systemLogin.SignIn(Executer.TestDataRow.get("LoginRow".toUpperCase()));
            navigators.goToRoleManagement();
            roleManagement.viewDetailsUserRole(Executer.TestDataRow);

        } catch (Exception ex) {
            ReporterX.error(ex);
        }
    }

    //endregion // End Of Role Management

    //region User Management

    @Test
    public void ValidateAddUser() {
        try {
            systemLogin.SignIn(Executer.TestDataRow.get("LoginRow".toUpperCase()));
            navigators.goToUserManagement();
            userManagement.addNewUser(Executer.TestDataRow);

        } catch (Exception ex) {
            ReporterX.error(ex);
        }
    }

    @Test
    public void ValidateEditUserData() {
        try {
            systemLogin.SignIn(Executer.TestDataRow.get("LoginRow".toUpperCase()));
            navigators.goToUserManagement();
            userManagement.editUser(Executer.TestDataRow);

        } catch (Exception ex) {
            ReporterX.error(ex);
        }
    }

    @Test
    public void ValidateChangeUserStatus() {
        try {
            systemLogin.SignIn(Executer.TestDataRow.get("LoginRow".toUpperCase()));
            navigators.goToUserManagement();
            userManagement.changeUserStatus(Executer.TestDataRow);

        } catch (Exception ex) {
            ReporterX.error(ex);
        }
    }

    @Test
    public void ValidateDeleteUser() {
        try {
            systemLogin.SignIn(Executer.TestDataRow.get("LoginRow".toUpperCase()));
            navigators.goToUserManagement();
            userManagement.deleteUser(Executer.TestDataRow);

        } catch (Exception ex) {
            ReporterX.error(ex);
        }
    }

    @Test
    public void ValidateViewUserDetails() {
        try {
            systemLogin.SignIn(Executer.TestDataRow.get("LoginRow".toUpperCase()));
            navigators.goToUserManagement();
            userManagement.viewUserDetails(Executer.TestDataRow);

        } catch (Exception ex) {
            ReporterX.error(ex);
        }
    }

    @Test
    public void ValidateResetUserPassword() {
        try {
            systemLogin.SignIn(Executer.TestDataRow.get("LoginRow".toUpperCase()));
            navigators.goToUserManagement();
            userManagement.resetUserPassword(Executer.TestDataRow);

        } catch (Exception ex) {
            ReporterX.error(ex);
        }
    }



    //endregion // End Of User Management



    //region Printing Management

    @Test
    public void ValidateViewPrintingRequest() {
        try {
            systemLogin.SignIn(Executer.TestDataRow.get("LoginRow".toUpperCase()));
            navigators.goToPrintingManagement();
            printingManagement.viewPrintingRequest(Executer.TestDataRow);

        } catch (Exception ex) {
            ReporterX.error(ex);
        }
    }

    //endregion // End Of Printing Management


}



