package com.elm.shj.applicant.portal.automation.pages;

import com.elm.qa.framework.core.ActionX;
import com.elm.qa.framework.core.Global;
import com.elm.qa.framework.utilities.ReporterX;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class Navigators {

    // declaring elements

    @FindBy(xpath = "//a[@ng-reflect-router-link='/users/list']")
    WebElement lnkManageUsers;

    @FindBy(xpath = "//app-user-list//button[@routerlink='/users/create']")
    WebElement btnAddNewUser;

    @FindBy(xpath = "//app-user-add-update//input[@formcontrolname='firstName']")
    WebElement txtUserFirstName;


    @FindBy(xpath = "//a[@ng-reflect-router-link='/roles/list']")
    WebElement lnkManageRoles;

    @FindBy(xpath = "//app-applicant-list//a[@routerlink='/roles/create']")
    WebElement btnAddNewRole;

    @FindBy(xpath = "//app-add-update-role//input[@formcontrolname='nameArabic']")
    WebElement txtRoleArbName;


    @FindBy(xpath = "//a[@ng-reflect-router-link='/cards/list']")
    WebElement lnkManageCards;

    @FindBy(xpath = "//app-user-list//button[@routerlink='/users/create']")
    WebElement btnAddNewCard;

    @FindBy(xpath = "//app-user-add-update//input[@formcontrolname='firstName']")
    WebElement txtCardName;


    @FindBy(xpath = "//a[@ng-reflect-router-link='/print-requests/list']")
    WebElement lnkManagePrinting;

    @FindBy(id="addPrintingRequest")
    WebElement btnPrintingRequest;




    public Navigators() {
        try {
            PageFactory.initElements(Global.Test.Browser, this);
        } catch (Exception ex) {
            ReporterX.error(ex);
        }
    }
    //'*********** Navigation Functions ******************************


    public boolean goToRoleManagement() {
        boolean _goToRoles = false;
        try {
            if (ActionX.Exists(btnAddNewRole, 3))
                _goToRoles = true;
            else if (ActionX.Exists(lnkManageRoles, 3)) {
                lnkManageRoles.click();

                if (ActionX.Exists(btnAddNewRole, 3)) {

                    ReporterX.info("Page << Roles Management >> Loaded .!!");
                    _goToRoles = true;
                }
            } else {
                ReporterX.fail("go To Role Management Failed.");
            }
            return _goToRoles;
        } catch (Exception e) {
            ReporterX.error(e);
            return false;
        }

    }

    public boolean goToCardManagement() {
        boolean _goToUsers = false;
        try {
            if (ActionX.Exists(btnAddNewCard, 3))
                _goToUsers = true;
            else if (ActionX.Exists(lnkManageCards, 3)) {
                btnAddNewCard.click();

                if (ActionX.Exists(btnAddNewCard, 3)) {

                    ReporterX.info("Page << Cards Management >> Loaded .!!");
                    _goToUsers = true;
                }
            } else {
                ReporterX.fail("go To Card Management Failed.");
            }
            return _goToUsers;
        } catch (Exception e) {
            ReporterX.error(e);
            return false;
        }

    }

    public boolean goToUserManagement() {
        boolean _goToUsers = false;
        try {
            if (ActionX.Exists(btnAddNewUser, 3))
                _goToUsers = true;
            else if (ActionX.Exists(lnkManageUsers, 3)) {
                lnkManageUsers.click();

                if (ActionX.Exists(btnAddNewUser, 3)) {

                    ReporterX.info("Page << Users Management >> Loaded .!!");
                    _goToUsers = true;
                }
            } else {
                ReporterX.fail("go To User Management Failed.");
            }
            return _goToUsers;
        } catch (Exception e) {
            ReporterX.error(e);
            return false;
        }

    }

    public boolean goToPrintingManagement() {
        boolean _goToPrinting = false;
        try {
            if (ActionX.Exists(btnPrintingRequest, 3))
                _goToPrinting = true;
            else if (ActionX.Exists(lnkManagePrinting, 3)) {
                lnkManagePrinting.click();

                if (ActionX.Exists(btnPrintingRequest, 3)) {

                    ReporterX.info("Page << Printing Management >> Loaded .!!");
                    _goToPrinting = true;
                }
            } else {
                ReporterX.fail("go To Printing Management Failed.");
            }
            return _goToPrinting;
        } catch (Exception e) {
            ReporterX.error(e);
            return false;
        }

    }


}












