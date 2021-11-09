package com.ssportal.be.controller;

import com.ssportal.be.config.JwtTokenUtil;
import com.ssportal.be.model.User;
import com.ssportal.be.pingid.model.Operation;
import com.ssportal.be.pingid.model.PingIdProperties;
import com.ssportal.be.pingid.service.PingIdOperationService;
import com.ssportal.be.service.UserService;
import com.ssportal.be.utilility.ApiResponse;
import com.ssportal.be.utilility.AuthToken;
import com.ssportal.be.utilility.HelperUtil;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
public class LoginController {
    private static final Logger LOG = LoggerFactory.getLogger ( LoginController.class );

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PingIdOperationService pingIdOperationService;

    @Autowired
    private final UserService userService;


    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.GET)
    public Object authenticate(
            @RequestParam(name = "REF") String RefID
    ) throws IOException {

        if (StringUtils.isNotBlank ( RefID )) {
            RefID = RefID.replaceAll ( "[^A-Za-z0-9]", "" );
        }
        String base_url = "https://localhost:9031";
        String pickupLocation = base_url + "/ext/ref/pickup?REF=" + RefID;
        LOG.debug ( pickupLocation );
        URL pickUrl = new URL ( pickupLocation );
        HttpURLConnection httpURLConn = (HttpURLConnection) pickUrl.openConnection ();
        httpURLConn.setRequestProperty ( "ping.uname", "admin" );
        httpURLConn.setRequestProperty ( "ping.pwd", "Password1!!" );
        // ping.instanceId is optional and only needs to be specified if multiple instances of ReferenceId adapter are configured.
        httpURLConn.setRequestProperty ( "ping.instanceId", "ssoSPadapter" );
        String encoding = httpURLConn.getContentEncoding ();

//        PF Part
        try (InputStream is = httpURLConn.getInputStream()) {
            InputStreamReader streamReader = new InputStreamReader(is, encoding != null ? encoding : "UTF-8");

            JSONParser parser = new JSONParser();
            JSONObject spUserAttributes = (JSONObject) parser.parse(streamReader);

            String username = spUserAttributes.get( "subject" ).toString ();
            if (username != null) {
                //register user when user can't be found in pingid
                User user = new User (spUserAttributes);
                PingIdProperties pingIdProperties = new PingIdProperties();
                pingIdProperties.setProperties ( 0 );
                Operation operation = new Operation(pingIdProperties.getOrgAlias(), pingIdProperties.getPingid_token(), pingIdProperties.getPingid_use_base64_key(), pingIdProperties.getApi_url());
                operation.setTargetUser(username);
                JSONObject userDetails = pingIdOperationService.getUserDetails(operation);
                if (userDetails == null){
                    pingIdOperationService.addUser (user, false, operation);

                }


                //username = username.toLowerCase ();
                try {
                    final String token = jwtTokenUtil.generateToken (user);
                    return new ApiResponse<> ( 200, "success", new AuthToken ( token, username ) );
                } catch (Exception ex) {
                    LOG.error ( "", ex );
                    return HelperUtil.handleException ( ex );
                }
            }
        } catch (ParseException e) {
            System.out.println ( "Certificate Error!!" );
            LOG.error("Error processing user details, Please contact Administrator", e);
        }
        return null;
    }
}

