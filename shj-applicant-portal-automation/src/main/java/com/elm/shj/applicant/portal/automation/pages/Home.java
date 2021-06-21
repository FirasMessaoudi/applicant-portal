package com.elm.shj.applicant.portal.automation.pages;

import com.elm.qa.framework.core.Global;
import com.elm.qa.framework.utilities.ReporterX;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Home {

    @FindBy(xpath = "//div[@id='spinner']")
    public WebElement divLoading;

    @FindBy(xpath = "//app-toasts//div[@class='toast-body']")
    public WebElement divSaveMsgContent;

    @FindBy(xpath = "//app-toasts//div[contains(@class,'toast-header')]")
    public WebElement divSaveMsgTitle;

    @FindBy(xpath = "//app-confirm-dialog//button[contains(@class,'btn-danger')]")
    public WebElement btnActionMsgConfirmNo;

    @FindBy(xpath = "//app-confirm-dialog//button[contains(@class,'btn-primary')]")
    public WebElement btnActionMsgConfirmYes;


    public Home() {
        try {
            PageFactory.initElements(Global.Test.Browser, this);
        } catch (Exception ex) {
            ReporterX.error(ex);
        }
    }
}
