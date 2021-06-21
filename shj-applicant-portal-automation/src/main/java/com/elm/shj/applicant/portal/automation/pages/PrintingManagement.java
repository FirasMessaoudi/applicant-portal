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

import static com.elm.qa.framework.core.ActionX.Exists;
import static com.elm.qa.framework.core.ActionX.SetValue;

public class PrintingManagement {


    @FindBy(id="createPrintingRequest")
    WebElement btnPrintingRequest;

    @FindBy(id="requestNumber")
    WebElement txtRequestNumber;

    @FindBy(xpath = "//input[@ng-reflect-name='printingStartDate']")
    WebElement txtPrintingStartDate;

    @FindBy(xpath = "//input[@ng-reflect-name='printingEndDate']")
    WebElement txtPrintingEndDate;

    @FindBy(id="batchNumber")
    WebElement txtBatchNumber;

    @FindBy(id="idNumber")
    WebElement txtIdNumber;

    @FindBy(id="cardNumber")
    WebElement txtCardNumber;

    @FindBy(id="requestStatus")
    WebElement lstRequestStatus;


    @FindBy(id="printingRequests")
    WebElement tblPrintingRequests;

    @FindBy(id="btnSearch")
    WebElement btnSearch;

    @FindBy(id="exportToExcel")
    WebElement btnExportExcel;

    @FindBy(id="exportToPDF")
    WebElement btnExportPDF;



    public PrintingManagement() {
        try {
            PageFactory.initElements(Global.Test.Browser, this);
        } catch (Exception ex) {
            ReporterX.error(ex);
        }
    }

    private boolean searchPrintingRequest(Hashtable<String,String> dataRow) throws Exception{
        String requestNumber= "";
        if(Exists(txtRequestNumber,30)) {

            // waiting
            ActionX.Sync();
            ActionX.WaitChildItemsCountGreaterThan(tblPrintingRequests, By.tagName("tr"),1,5);

            requestNumber = dataRow.get("RequestNumber".toUpperCase());

            SetValue(txtRequestNumber, requestNumber);
            btnSearch.click();

            //waiting load results
            ActionX.Sync();
            ActionX.WaitChildItemsCountLessThan(tblPrintingRequests,By.tagName("tr"),2,5);

        }else {
            ReporterX.fail("Search Printing Request Page not Loaded.!!");
        }

        if(tblPrintingRequests.findElements(By.tagName("tr")).size() > 1){
            ReporterX.pass("Printing Request ["+requestNumber+"] Search Success.!");
            return true;
        }else {
            ReporterX.fail("Printing Request ["+requestNumber+"] Search Failed.!");
            return false;
        }

    }

    private boolean selectPrintingRequest(Hashtable<String,String> dataRow, String action) throws Exception{
        boolean isSelected = false;
        if( searchPrintingRequest(dataRow)) {

            switch (action) {
                case "View":
                    tblPrintingRequests.findElements(By.tagName("tr")).get(1).findElements(By.tagName("a")).get(0).click();
                    isSelected = true;
                    break;
                default:
                    tblPrintingRequests.findElements(By.tagName("tr")).get(1).findElements(By.tagName("a")).get(0).click();
            }
        }


        return isSelected;
    }

    private WebElement getWebElement(String requestID) {
        WebDriverWait wait = new WebDriverWait(Global.Test.Browser, 5);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//app-printing-request-details//span[contains(text(),'"+ requestID +"')]")));
        return element;
    }


    public void viewPrintingRequest(Hashtable<String,String> dataRow) throws Exception{
        if(selectPrintingRequest(dataRow,"View")){
            //wait for request details
            String requestID = dataRow.get("RequestNumber".toUpperCase());

            WebElement element = getWebElement(requestID);

            if(null != element){
                ReporterX.pass("View Printing Request Details ["+requestID+"] Success.!");
            }else {
                ReporterX.fail("View Printing Request Details ["+requestID+"] Failed.!");
            }

        }
    }


}
