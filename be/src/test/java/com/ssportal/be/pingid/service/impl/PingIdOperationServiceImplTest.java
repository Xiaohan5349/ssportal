package com.ssportal.be.pingid.service.impl;

import com.ssportal.be.pingid.model.Operation;
import com.ssportal.be.pingid.model.PingIdProperties;
import com.ssportal.be.pingid.model.RequestData;
import com.ssportal.be.pingid.service.PingIdOperationService;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class PingIdOperationServiceImplTest {
    @Autowired
    private PingIdOperationService pingIdOperationService;

    private PingIdProperties pingIdProperties;
    public PingIdOperationServiceImplTest() throws IOException {
        this.pingIdProperties = new PingIdProperties();
        pingIdProperties.setProperties(0);
    }



    @Test
    void backupOnline() {

        String authType = "ONE_TIME_DEVICE";
        String spAlias = "rescuecode";
        String username = "bc81883";
        String deviceType = "SMS";
        String deviceData = "12022628086";

        RequestData requestData = new RequestData ();
        requestData.setUsername ( username );
        requestData.setAuthType ( authType );
        requestData.setSpAlias ( spAlias );
        requestData.setDeviceType ( deviceType );
        requestData.setName ( "MFA" );
        requestData.setDeviceData ( deviceData );

        Operation operation = new Operation(pingIdProperties.getOrgAlias(), pingIdProperties.getPingid_token(), pingIdProperties.getPingid_use_base64_key(), pingIdProperties.getApi_url());
        operation.setTargetUser(username);
        //JSONObject response = pingIdOperationService.backupOnline(requestData, authType, operation);



    }

    @Test
    void authenticationOffline() {
    }
}