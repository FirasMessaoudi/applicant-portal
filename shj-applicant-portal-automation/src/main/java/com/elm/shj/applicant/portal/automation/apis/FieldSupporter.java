package com.elm.shj.applicant.portal.automation.apis;

import static com.elm.qa.framework.core.ActionX.Exists;

public class FieldSupporter {


    public void submitFieldSupporterTask() throws Exception {

        //login and update AccessToken in UserData
        UtilitiesAPI.setAccessTokenInTestData();

        //Execute web service call
        UtilitiesAPI.callWebService();
    }





}
