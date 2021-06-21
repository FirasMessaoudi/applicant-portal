package com.elm.shj.applicant.portal.automation.pages;

import com.elm.qa.framework.core.ActionX;
import com.elm.qa.framework.core.DataManager;
import com.elm.qa.framework.core.Global;


import java.util.Hashtable;
import java.util.List;

import static com.elm.qa.framework.core.ActionX.Exists;
import static com.elm.qa.framework.core.ActionX.SetValue;

import com.elm.qa.framework.faker.Faker;
import com.elm.qa.framework.utilities.ReporterX;
import com.sun.xml.xsom.impl.scd.Iterators;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class SystemLogin {

    // declaring elements
    @FindBy(xpath = "//input[@formcontrolname='username']")
    WebElement txtUserName;

    @FindBy(xpath = "//input[@formcontrolname='password']")
    WebElement txtPassword;

    @FindBy(xpath = "//input[@name='captcha']")
    WebElement txtCaptcha;

    @FindBy(xpath = "//button[@class='btn btn-dcc-primary btn-block']")
    WebElement btnLogin;

    @FindBy(id = "otpInputsRow")
    WebElement divOtpInputs;

    @FindBy(id = "userMenuLink")
    WebElement lnkUserMenu;

    @FindBy(xpath = "//div[@class='alert alert-danger']")
    WebElement divLoginErrMsg;

    @FindBy(xpath = "//ng-component//span[contains(text(),'عرب')]//parent::button")
    WebElement btnSystemArabLanguage;

    @FindBy(xpath = "//ng-component//span[contains(text(),'En')]//parent::button")
    WebElement btnSystemEnLanguage;

    @FindBy(xpath = "//app-header//ul//li[4]")
    WebElement btnLogOut;



    Home homePage = new Home();


    public SystemLogin() {
        try {
            PageFactory.initElements(Global.Test.Browser, this);
        } catch (Exception ex) {
            ReporterX.error(ex);
        }
    }
    //'*********** LOGIN Test Functions ******************************

    private void setOTP(String strOTP) throws Exception {
        if(Exists(divOtpInputs,10)){
            char[]  otpDigits = strOTP.toCharArray();
            List<WebElement> txtOTPDigits = divOtpInputs.findElements(By.tagName("input"));

            if(otpDigits.length >= txtOTPDigits.size()){
                for (int i=0;i<txtOTPDigits.size();i++){
                    txtOTPDigits.get(i).sendKeys(String.valueOf(otpDigits[i]));
                    Thread.sleep(500);
                }
            }else {
                ReporterX.info("The provided OTP ("+strOTP+") length is too small ... ");
            }
        }else {
            ReporterX.info("The OTP Page not loaded ");
        }

    }

    public boolean Login(String strUserName, String strPassword) {
        boolean retRes = false;
        Faker fk = new Faker();
        try {

            // Change System Language
            if(Global.Test.RunLang.equalsIgnoreCase("arb")){
                if(Exists(btnSystemArabLanguage,2)){
                    btnSystemArabLanguage.click();
                    ReporterX.info("System Language Switched to ** Arabic ** ");
                }
            }else {
                if(Exists(btnSystemEnLanguage,2)){
                    btnSystemEnLanguage.click();
                    ReporterX.info("System Language Switched to ** English ** ");
                }
            }

            String catptach = fk.number().digits(6).toString();
            if (Exists(txtUserName, 30)) {
                SetValue(txtUserName,strUserName);
                SetValue(txtPassword,strPassword);
                Thread.sleep(1000);
                //SetValue(txtCaptcha,catptach);

                if (ActionX.WaitUntilEnabled(btnLogin, 2))
                    btnLogin.click();
                else {
                    ReporterX.fail("User ( " + strUserName + " ) LogIn Failed. Login Button is Disabled.!");
                    Global.Test.LogedInUser = "";
                    Global.Test.IsLogedIn = false;
                    retRes = false;
                }

                if(Exists(divLoginErrMsg, 5)){

                    String errMsg = "incorrect password";
                    try{errMsg = divLoginErrMsg.getText();}catch (Exception e){};
                    ReporterX.fail("User ( " + strUserName + " ) LogIn Failed.!");
                    ReporterX.info("Error Message is : "+errMsg);
                    Global.Test.LogedInUser = "";
                    Global.Test.IsLogedIn = false;
                    retRes = false;

                }else if (Exists(divOtpInputs, 30)) {

                   setOTP("1234");

                    if (Exists(lnkUserMenu, 30)) {

                        ReporterX.info("User ( " + strUserName + " ) LogIn Successfully.!");
                        retRes = true;
                        Global.Test.LogedInUser = strUserName;
                        Global.Test.IsLogedIn = true;

                    }else {
                        ReporterX.fail("User ( " + strUserName + " ) LogIn Failed.!");
                        Global.Test.LogedInUser = "";
                        Global.Test.IsLogedIn = false;
                        retRes = false;

                    }

                } else {
                    ReporterX.fail("User ( " + strUserName + " ) LogIn Failed for OTP .!");
                    Global.Test.LogedInUser = "";
                    Global.Test.IsLogedIn = false;
                    retRes = false;

                }


            }
            return retRes;
        } catch (Exception ex) {
            ReporterX.error( ex);
            Global.Test.LogedInUser = "";
            Global.Test.IsLogedIn = false;
            return false;
        }
    }

    public boolean LogOut() {
        boolean retRes = false;
        try {

            if (Exists(btnLogOut, 2)) {
                if (Exists(homePage.divSaveMsgContent,1))
                    ActionX.WaitUntilHidden(homePage.divSaveMsgContent, 10);
                ActionX.ScrollToElement(btnLogOut);
                btnLogOut.click();
            }

            if (Exists(txtUserName, 60)) {
                retRes = true;
            }
            return retRes;
        } catch (Exception ex) {
            ReporterX.error(ex);
            return false;
        } finally {
            Global.Test.LogedInUser = "";
            Global.Test.IsLogedIn = false;
        }
    }


    private boolean IsLogedIN() {
        boolean res = false;
        try {

            if (Exists(lnkUserMenu, 10)) {
                Global.Test.IsLogedIn = true;
                res = true;
            }
        } catch (Exception ex) {
            ReporterX.error(ex);
            return false;
        }

        return res;
    }

    public boolean SignIn(String iLoginRow) {
        boolean retRes = false;
        Hashtable<String, String> loginRow = null;
        try {

            loginRow = DataManager.GetExcelDataTable("Select * from Login where RowID = " + iLoginRow).get(1);

            if (loginRow != null) {


                if (Global.Test.IsLogedIn) {
                    if (Global.Test.LogedInUser.trim().toUpperCase().equals(loginRow.get("userID".toUpperCase()).trim().toUpperCase())) {
                        ReporterX.info("User(" + loginRow.get("userID".toUpperCase()) + ") Is Already Logged In.!");
                        Global.Test.IsLogedIn = true;
                        retRes = true;
                    } else {
                        if (LogOut()) {
                            retRes = Login(loginRow.get("userID".toUpperCase()), loginRow.get("Password".toUpperCase()));


                        } else {
                            ReporterX.fail("Failed to do LogOut.");
                            retRes = false;
                        }
                    }
                } else {
                    retRes = Login(loginRow.get("userID".toUpperCase()), loginRow.get("Password".toUpperCase()));



                }
            } else {
                ReporterX.fail("User Data Not Exist.!");
                Global.Test.LogedInUser = "";
                Global.Test.IsLogedIn = false;
                retRes = false;
            }

            return retRes;
        } catch (Exception ex) {
            ReporterX.error(ex);
            return false;
        }


    }

    public boolean SignIn(Hashtable<String, String> loginRow) {
        boolean retRes = false;

        try {
            if (loginRow != null) {


                if (Global.Test.IsLogedIn) {
                    if (Global.Test.LogedInUser.trim().toUpperCase().equals(loginRow.get("userID".toUpperCase()).trim().toUpperCase())) {
                        ReporterX.info("User(" + loginRow.get("userID".toUpperCase()) + ") Is Already Logged In.!");
                        Global.Test.IsLogedIn = true;
                        retRes = true;
                    } else {
                        if (LogOut()) {
                            retRes = Login(loginRow.get("userID".toUpperCase()), loginRow.get("Password".toUpperCase()));

                        } else {
                            ReporterX.fail("Failed to do LogOut.");
                            retRes = false;
                        }
                    }
                } else {
                    retRes = Login(loginRow.get("userID".toUpperCase()), loginRow.get("Password".toUpperCase()));


                }
            } else {
                ReporterX.fail("User Data Not Exist.!");
                Global.Test.LogedInUser = "";
                Global.Test.IsLogedIn = false;
                retRes = false;
            }

            return retRes;
        } catch (Exception ex) {
            ReporterX.error(ex);
            return false;
        }


    }


}

