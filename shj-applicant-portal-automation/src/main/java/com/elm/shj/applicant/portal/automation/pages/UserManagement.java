package com.elm.shj.applicant.portal.automation.pages;

import com.elm.qa.framework.core.ActionX;
import com.elm.qa.framework.core.Global;
import com.elm.qa.framework.utilities.ReporterX;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Hashtable;

import static com.elm.qa.framework.core.ActionX.*;

public class UserManagement {
    // declaring elements
    @FindBy(xpath = "//app-user-list//button[@routerlink='/users/create']")
    WebElement btnAddNewUser;

    @FindBy(xpath = "//app-user-add-update//input[@formcontrolname='firstName']")
    WebElement txtUserFirstName;

    @FindBy(xpath = "//app-user-add-update//input[@formcontrolname='fatherName']")
    WebElement txtUserFatherName;

    @FindBy(xpath = "//app-user-add-update//input[@formcontrolname='grandFatherName']")
    WebElement txtUserGrandFatherName;

    @FindBy(xpath = "//app-user-add-update//input[@formcontrolname='familyName']")
    WebElement txtUserFamilyName;

    @FindBy(xpath = "//app-user-add-update//input[@formcontrolname='nin']")
    WebElement txtUserIdNumber;

    @FindBy(xpath = "//app-user-add-update//input[@ng-reflect-name='dateOfBirthGregorian']")
    WebElement txtUserDOB;

    @FindBy(xpath = "//app-user-add-update//div[@ng-reflect-selected='true']")
    WebElement btnUserDobDay;

    @FindBy(xpath = "//app-user-add-update//button[contains(text(),'Gregorian') or contains(text(),'ميلاد')]")
    WebElement btnClndrTypeGregorian;

    @FindBy(xpath = "//app-user-add-update//button[contains(text(),'Hijri') or contains(text(),'هجر')]")
    WebElement btnClndrTypeHijri;

    @FindBy(xpath = "//app-user-add-update//input[@formcontrolname='gender' and @value='M']//parent::label")
    WebElement btnUserGenderMale;

    @FindBy(xpath = "//app-user-add-update//input[@formcontrolname='gender' and @value='F']//parent::label")
    WebElement btnUserGenderFemale;

    @FindBy(xpath = "//app-user-add-update//input[@formcontrolname='email']")
    WebElement txtUserEmail;

    @FindBy(xpath = "//app-user-add-update//input[@formcontrolname='mobileNumber']")
    WebElement txtUserMobileNumber;

    @FindBy(xpath = "//app-user-add-update//input[@name='radioActivated' and @ng-reflect-value='true']//parent::label")
    WebElement btnUserStatusActive;

    @FindBy(xpath = "//app-user-add-update//input[@name='radioActivated' and @ng-reflect-value='false']//parent::label")
    WebElement btnUserStatusInActive;

    @FindBy(xpath = "//app-user-add-update//select[@formcontrolname='role']")
    WebElement lstUserMainRole;

    @FindBy(xpath = "//app-user-add-update//ng-multiselect-dropdown[@formcontrolname='additionalRoles']")
    WebElement lstUserAdditionalRole;

    @FindBy(xpath = "(//app-user-add-update//ng-multiselect-dropdown[@formcontrolname='additionalRoles']//ul)[2]")
    WebElement ulUserAdditionalRole;

    @FindBy(xpath = "//app-user-add-update//div[contains(@class,'footer__action')]//button[contains(@class,'btn-outline-dcc-primary')]")
    WebElement btnSaveUser;

    @FindBy(xpath = "//app-user-add-update//div[contains(@class,'footer__action')]//button[contains(@class,'btn-outline-secondary')]")
    WebElement btnCancelSaveUSer;

    @FindBy(xpath = "//app-user-list//input[@formcontrolname='nin']")
    WebElement txtUserNINSearch;

    @FindBy(xpath = "//app-user-list//form//button[contains(@class,'btn-dcc-primary')]")
    WebElement btnUserSearch;

    @FindBy(xpath = "//app-user-list//table//tbody")
    WebElement tblUsersSearchResults;



    public UserManagement() {
        try {
            PageFactory.initElements(Global.Test.Browser, this);
        } catch (Exception ex) {
            ReporterX.error(ex);
        }
    }

    private void validateSuccess(Hashtable<String, String> dataRow) {
        Home home = new Home();
        if(Exists(home.divSaveMsgContent,30)){
            String msg = home.divSaveMsgContent.getText().toLowerCase();
            if(msg.contains("success") || msg.contains("نجاح") ){
                ReporterX.info("User Manage, System Message : "+msg);
                ReporterX.pass("User ["+dataRow.get("UserIdNumber".toUpperCase())+"] Success.!");
            }else {
                ReporterX.info("User Manage,System Message : "+msg);
                ReporterX.fail("User ["+dataRow.get("UserIdNumber".toUpperCase())+"] Failed.!");
            }
        }else {
            ReporterX.fail("User ["+dataRow.get("UserIdNumber".toUpperCase())+"] Failed.!");
        }
    }

    private boolean searchUser(Hashtable<String,String> dataRow) throws Exception{
        String userNIN= "";
        if(Exists(txtUserNINSearch,30)) {

            // waiting
            ActionX.Sync();
            ActionX.WaitChildItemsCountGreaterThan(tblUsersSearchResults,By.tagName("tr"),0,5);
            userNIN = dataRow.get("UserIdNumber".toUpperCase());

            SetValue(txtUserNINSearch, userNIN);
            btnUserSearch.click();

            //waiting load results
            ActionX.Sync();
            ActionX.WaitChildItemsCountLessThan(tblUsersSearchResults,By.tagName("tr"),2,5);

        }else {
            ReporterX.fail("Search User Page not Loaded.!!");
        }

        if(tblUsersSearchResults.findElements(By.tagName("tr")).size() > 0){
            ReporterX.pass("User ["+userNIN+"] Search Success.!");
            return true;
        }else {
            ReporterX.fail("User ["+userNIN+"] Search Failed.!");
            return false;
        }

    }

    private boolean selectUserAction(Hashtable<String,String> dataRow, String action) throws Exception{
        boolean isSelected = false;
        if( searchUser(dataRow)) {

            switch (action) {
                case "Edit":
                    tblUsersSearchResults.findElements(By.tagName("tr")).get(0).findElement(By.xpath("//svg-icon[@ng-reflect-icon='user-edit']//parent::a")).click();
                    isSelected = true;
                    break;
                case "Deactivate":
                    tblUsersSearchResults.findElements(By.tagName("tr")).get(0).findElement(By.xpath("//svg-icon[@ng-reflect-icon='user-slash']//parent::a")).click();
                    isSelected = true;
                    break;
                case "Activate":
                    tblUsersSearchResults.findElements(By.tagName("tr")).get(0).findElement(By.xpath("//svg-icon[@ng-reflect-icon='user-check']//parent::a")).click();
                    isSelected = true;
                    break;
                case "Delete":
                    tblUsersSearchResults.findElements(By.tagName("tr")).get(0).findElement(By.xpath("//svg-icon[@ng-reflect-icon='user-times']//parent::a")).click();
                    isSelected = true;
                    break;
                case "View":
                    tblUsersSearchResults.findElements(By.tagName("tr")).get(0).findElement(By.xpath("//svg-icon[@ng-reflect-icon='eye']//parent::a")).click();
                    isSelected = true;
                    break;
                case "ResetPassword":
                    tblUsersSearchResults.findElements(By.tagName("tr")).get(0).findElement(By.xpath("//svg-icon[@ng-reflect-icon='user-lock']//parent::a")).click();
                    isSelected = true;
                    break;
                default:
                    tblUsersSearchResults.findElements(By.tagName("tr")).get(0).findElement(By.xpath("//svg-icon[@ng-reflect-icon='eye']//parent::a")).click();
            }
        }


        return isSelected;
    }


    public void addNewUser(Hashtable<String,String> dataRow) throws Exception{

        if (ActionX.Exists(btnAddNewUser, 3)) {
            btnAddNewUser.click();
        }

        if(Exists(txtUserFirstName,30)){

            // preparing test data randomization

//            UtilitiesUI.prepareTestData(dataRow);

            //filling  Data

            SetValue(txtUserFirstName,dataRow.get("FirstName".toUpperCase()));
            SetValue(txtUserFatherName,dataRow.get("FatherName".toUpperCase()));
            SetValue(txtUserGrandFatherName,dataRow.get("GrandFatherName".toUpperCase()));
            SetValue(txtUserFamilyName,dataRow.get("FamilyName".toUpperCase()));
            SetValue(txtUserIdNumber,dataRow.get("UserIdNumber".toUpperCase()));
            RemoveAttribute(txtUserDOB,"readonly");
            SetValue(txtUserDOB,dataRow.get("UserDOB".toUpperCase()));
            txtUserDOB.click();
            Thread.sleep(1000);
            btnUserDobDay.click();
            if (dataRow.get("UserGender".toUpperCase()).equalsIgnoreCase("male")) {
                btnUserGenderMale.click();
            } else {
                btnUserGenderFemale.click();
            }
            if (dataRow.get("UserStatus".toUpperCase()).equalsIgnoreCase("active")) {
                btnUserStatusActive.click();
            } else {
                btnUserStatusInActive.click();
            }
            SetValue(txtUserMobileNumber,dataRow.get("MobileNumber".toUpperCase()));

            // select role based on language.
            String mainRole , additionalRole = "";
            if(Global.Test.RunLang.equalsIgnoreCase("arb")) {
                mainRole = dataRow.get("MainRoleArb".toUpperCase());
                additionalRole = dataRow.get("AdditionalRoleArb".toUpperCase());
            }else {
                mainRole = dataRow.get("MainRole".toUpperCase());
                additionalRole = dataRow.get("AdditionalRole".toUpperCase());
            }

            Select(lstUserMainRole,mainRole);
            try{
                lstUserAdditionalRole.click();
                Thread.sleep(1000);
                String xpath = "(//app-user-add-update//ng-multiselect-dropdown[@formcontrolname='additionalRoles']//ul)[2]//div[contains(text(),'"+additionalRole+"')]";
                Global.Test.Browser.findElements(By.xpath(xpath)).get(0).click();
            }catch (Exception e){

            }

            SetValue(txtUserEmail,dataRow.get("UserEmail".toUpperCase()));

            ActionX.ScrollToElement(btnSaveUser);
            btnSaveUser.click();

            //validate add item
            validateSuccess(dataRow);


        }else {
            ReporterX.fail("Add New User Page not Loaded.!!");
        }

    }

    public void editUser(Hashtable<String,String> dataRow) throws Exception{

        if(selectUserAction(dataRow,"Edit")){
            addNewUser(dataRow);
        }


    }

    public void deleteUser(Hashtable<String,String> dataRow) throws Exception{
        Home home = new Home();
        if(selectUserAction(dataRow,"Delete")){
            if(Exists(home.btnActionMsgConfirmYes,5)){
                home.btnActionMsgConfirmYes.click();
                //validate delete item
                validateSuccess(dataRow);
            }
        }
    }

    public void changeUserStatus(Hashtable<String,String> dataRow) throws Exception{
        Home home = new Home();
        if (dataRow.get("UserStatus".toUpperCase()).equalsIgnoreCase("active")) {
            selectUserAction(dataRow,"Activate");
        } else {
            selectUserAction(dataRow,"Deactivate");
        }
        if(Exists(home.btnActionMsgConfirmYes,5)) {
            home.btnActionMsgConfirmYes.click();
            //validate delete item
            validateSuccess(dataRow);
        }

    }

    public void viewUserDetails(Hashtable<String,String> dataRow) throws Exception{

        if(selectUserAction(dataRow,"View")){
            //wait for element details
            String userNIN = dataRow.get("UserIdNumber".toUpperCase());

            WebDriverWait wait = new WebDriverWait(Global.Test.Browser, 5);
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//app-user-details//span[contains(text(),'"+userNIN+"')]")));

            if(null != element){
                ReporterX.pass("View User Details ["+userNIN+"] Success.!");
            }else {
                ReporterX.fail("View User Details ["+userNIN+"] Failed.!");
            }
        }
    }

    public void resetUserPassword(Hashtable<String,String> dataRow) throws Exception{

        Home home = new Home();
        if(selectUserAction(dataRow,"ResetPassword")){
            if(Exists(home.btnActionMsgConfirmYes,5)){
                home.btnActionMsgConfirmYes.click();
                //validate delete item
                validateSuccess(dataRow);
            }
        }
    }




}
