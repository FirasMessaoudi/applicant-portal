package com.elm.shj.applicant.portal.automation.main;

import com.elm.qa.framework.runner.Driver;
import com.elm.qa.framework.utilities.ReporterX;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.URL;


public class Runner {

    public static void main(String[] args) throws Exception {
        try {

            Driver.ExecuteTestSuite();

        } catch (Exception e) {
            ReporterX.error(e.toString());

        }
    }

    @Test
    public void mainX() throws Exception {
        try {
            Assert.assertEquals(java.util.Optional.of(Driver.ExecuteTestSuite()), java.util.Optional.of(0));

        } catch (Exception e) {
            ReporterX.error(e.toString());
        }
    }
}
