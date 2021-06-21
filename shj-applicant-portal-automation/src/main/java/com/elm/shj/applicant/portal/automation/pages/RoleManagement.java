package com.elm.shj.applicant.portal.automation.pages;

import com.elm.qa.framework.core.ActionX;
import com.elm.qa.framework.core.DataManager;
import com.elm.qa.framework.core.Global;
import com.elm.qa.framework.utilities.Common;
import com.elm.qa.framework.utilities.ReporterX;
import org.joda.time.DateTime;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Hashtable;
import java.util.List;

import static com.elm.qa.framework.core.ActionX.Exists;
import static com.elm.qa.framework.core.ActionX.SetValue;

public class RoleManagement {
    // declaring elements
    @FindBy(xpath = "//app-add-update-role//input[@formcontrolname='nameArabic']")
    WebElement txtRoleArbName;

    @FindBy(xpath = "//app-add-update-role//input[@formcontrolname='nameEnglish']")
    WebElement txtRoleEngName;

    @FindBy(xpath = "//app-add-update-role//div[@formcontrolname='activated']//input[@ng-reflect-value='false']//parent::label")
    WebElement btnRoleNotActive;

    @FindBy(xpath = "//app-add-update-role//div[@formcontrolname='activated']//input[@ng-reflect-value='true']//parent::label")
    WebElement btnRoleActive;

    @FindBy(xpath = "//app-add-update-role//button[contains(@class,'btn-outline-dcc-primary')]")
    WebElement btnSave;

    @FindBy(xpath = "//app-applicant-list//a[@routerlink='/roles/create']")
    WebElement btnAddNewRole;

    @FindBy(xpath = "//app-applicant-list//input[contains(@formcontrolname,'roleName')]")
    WebElement txtRoleNameSearch;

    @FindBy(xpath = "//app-applicant-list//input[@formcontrolname='authorityId']")
    WebElement lstRoleAuthorityName;

    @FindBy(xpath = "//app-applicant-list//button[contains(@class,'btn-dcc-primary')]")
    WebElement btnRoleSearch;

    @FindBy(xpath = "//app-applicant-list//table//tbody")
    WebElement tblRolesSearchResults;







    public RoleManagement() {
        try {
            PageFactory.initElements(Global.Test.Browser, this);
        } catch (Exception ex) {
            ReporterX.error(ex);
        }
    }

    private void validateSuccess(Hashtable<String, String> dataRow, Home home) {
        if(Exists(home.divSaveMsgContent,30)){
            String msg = home.divSaveMsgContent.getText().toLowerCase();
            if(msg.contains("success") || msg.contains("نجاح") ){
                ReporterX.info("User Role System Message : "+msg);
                ReporterX.pass("User Role ["+dataRow.get("RoleEngName".toUpperCase())+"] Success.!");
            }else {
                ReporterX.info("User Role System Message : "+msg);
                ReporterX.fail("User Role ["+dataRow.get("RoleEngName".toUpperCase())+"] Failed.!");
            }
        }else {
            ReporterX.fail("User Role ["+dataRow.get("RoleEngName".toUpperCase())+"] Failed.!");
        }
    }

    private boolean searchUserRole(Hashtable<String,String> dataRow) throws Exception{
        String roleName= "";
        if(Exists(txtRoleNameSearch,30)) {

            // waiting
            ActionX.Sync();
            ActionX.WaitChildItemsCountGreaterThan(tblRolesSearchResults,By.tagName("tr"),0,5);

            //filling  search Data

            if(Global.Test.RunLang.equalsIgnoreCase("arb"))
                roleName = dataRow.get("RoleArbName".toUpperCase());
            else
                roleName = dataRow.get("RoleEngName".toUpperCase());

            SetValue(txtRoleNameSearch, roleName);
            btnRoleSearch.click();

           //waiting load results
            ActionX.Sync();
            ActionX.WaitChildItemsCountLessThan(tblRolesSearchResults,By.tagName("tr"),2,5);

        }else {
            ReporterX.fail("Search User Role Page not Loaded.!!");
        }

        if(tblRolesSearchResults.findElements(By.tagName("tr")).size() > 0){
            ReporterX.pass("User Role ["+roleName+"] Success.!");
            return true;
        }else {
            ReporterX.fail("User Role ["+roleName+"] Failed.!");
            return false;
        }

    }

    private boolean selectActionUserRole(Hashtable<String,String> dataRow,String action) throws Exception{
        boolean isSelected = false;
       if( searchUserRole(dataRow)) {

           switch (action) {
               case "Edit":
                   tblRolesSearchResults.findElements(By.tagName("tr")).get(0).findElement(By.xpath("//svg-icon[@ng-reflect-icon='edit']//parent::a")).click();
                   isSelected = true;
                   break;
               case "Deactivate":
                   tblRolesSearchResults.findElements(By.tagName("tr")).get(0).findElement(By.xpath("//svg-icon[@ng-reflect-icon='user-tag-minus']//parent::a")).click();
                   isSelected = true;
                   break;
               case "Activate":
                   tblRolesSearchResults.findElements(By.tagName("tr")).get(0).findElement(By.xpath("//svg-icon[@ng-reflect-icon='user-tag-check']//parent::a")).click();
                   isSelected = true;
                   break;
               case "Delete":
                   tblRolesSearchResults.findElements(By.tagName("tr")).get(0).findElement(By.xpath("//svg-icon[@ng-reflect-icon='user-tag-times']//parent::a")).click();
                   isSelected = true;
                   break;
               case "View":
                   tblRolesSearchResults.findElements(By.tagName("tr")).get(0).findElement(By.xpath("//svg-icon[@ng-reflect-icon='eye']//parent::a")).click();
                   isSelected = true;
                   break;
               default:
                   tblRolesSearchResults.findElements(By.tagName("tr")).get(0).findElement(By.xpath("//svg-icon[@ng-reflect-icon='eye']//parent::a")).click();
           }
       }


        return isSelected;
    }

    public void addNewUserRole(Hashtable<String,String> dataRow) throws Exception{

        Home home = new Home();

        if (ActionX.Exists(btnAddNewRole, 3)) {
            btnAddNewRole.click();
        }

        if(Exists(txtRoleArbName,30)) {

            // preparing test data randomization

//            UtilitiesUI.prepareTestData(dataRow);

            //filling  Data

            SetValue(txtRoleArbName, dataRow.get("RoleArbName".toUpperCase()));

            SetValue(txtRoleEngName, dataRow.get("RoleEngName".toUpperCase()));

            if (dataRow.get("UserRoleActive".toUpperCase()).equalsIgnoreCase("yes")) {
                btnRoleActive.click();
            } else {
                btnRoleNotActive.click();
            }

            // Selecting list of Authorities
            List<String> lstRows = Common.GetIterations(dataRow.get("RoleAuthorities".toUpperCase()),",");
            String query = "";
            if(Global.Test.RunLang.equalsIgnoreCase("eng")){
                query = "Select RowID,AuthorityName FROM RoleAuthorities";
            }else{
                query = "Select RowID,AuthorityNameArb FROM RoleAuthorities";
            }
            Hashtable<String,String> lstAuthorities = DataManager.GetExcelDictionary(query);
            for (String row: lstRows ) {
                ReporterX.info("Select Authority : "+lstAuthorities.get(row));
                try {
                    Global.Test.Browser.
                            findElements(By.xpath("//app-add-update-role//form//label[contains(text(),'" + lstAuthorities.get(row) + "') and contains(@class,'roleCheck')]")).get(0).click();
                }catch (Exception e){}

            }
            ActionX.ScrollToElement(btnSave);
            btnSave.click();

            //validate add item
            validateSuccess(dataRow, home);


        }else {
            ReporterX.fail("Add New User Role Page not Loaded.!!");
        }

    }

    public void editUserRole(Hashtable<String,String> dataRow) throws Exception{

        if(selectActionUserRole(dataRow,"Edit")){
            addNewUserRole(dataRow);
        }


    }

    public void deleteUserRole(Hashtable<String,String> dataRow) throws Exception{
        Home home = new Home();
        if(selectActionUserRole(dataRow,"Delete")){
            if(Exists(home.btnActionMsgConfirmYes,5)){
                home.btnActionMsgConfirmYes.click();
                //validate delete item
                validateSuccess(dataRow, home);
            }
        }



    }

    public void changeStatusUserRole(Hashtable<String,String> dataRow) throws Exception{
        Home home = new Home();
        if (dataRow.get("UserRoleActive".toUpperCase()).equalsIgnoreCase("yes")) {
            selectActionUserRole(dataRow,"Activate");
        } else {
            selectActionUserRole(dataRow,"Deactivate");
        }
      if(Exists(home.btnActionMsgConfirmYes,5)) {
          home.btnActionMsgConfirmYes.click();
          //validate delete item
          validateSuccess(dataRow, home);
      }

    }

    public void viewDetailsUserRole(Hashtable<String,String> dataRow) throws Exception{

        if(selectActionUserRole(dataRow,"View")){
            //wait for element details
            String roleName = "";
            if(Global.Test.RunLang.equalsIgnoreCase("arb"))
                roleName = dataRow.get("RoleArbName".toUpperCase());
            else
                roleName = dataRow.get("RoleEngName".toUpperCase());

            WebDriverWait wait = new WebDriverWait(Global.Test.Browser, 5);
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//app-user-details//span[contains(text(),'"+roleName+"')]")));

            if(null != element){
                ReporterX.pass("View User Role ["+roleName+"] Success.!");
            }else {
                ReporterX.fail("View User Role ["+roleName+"] Failed.!");
            }
        }
    }






}
