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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.HttpsURLConnection;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<AuthToken> authenticate(
            @RequestParam(name = "REF") String RefID
    ) throws IOException {

        if (StringUtils.isNotBlank ( RefID )) {
            RefID = RefID.replaceAll ( "[^A-Za-z0-9]", "" );
        }
        ///bbb
        String base_url = "https://ssoqa.bedbath.com";
//        String base_url = "https://localhost:9031";
        String pickupLocation = base_url + "/ext/ref/pickup?REF=" + RefID;
        java.util.Properties prop = new java.util.Properties ();
        String propFileName = "application.properties";
        try (InputStream inputStream = getClass ().getClassLoader ().getResourceAsStream ( propFileName )) {
            if (inputStream != null) {
                prop.load ( inputStream );
            } else {
                throw new FileNotFoundException ( "property file '" + propFileName + "' not found in the classpath" );
            }

            String ping_uname, rping_pwd, instance_id;
            ping_uname = prop.getProperty ( "refid_user" );
            rping_pwd = prop.getProperty ( "refid_pwd" );
            instance_id = prop.getProperty ( "instance_id" );
            URL pickUrl = new URL ( pickupLocation );
            HttpURLConnection httpsURLConn = (HttpURLConnection) pickUrl.openConnection ();

            httpsURLConn.setRequestProperty ( "ping.uname", ping_uname );
            httpsURLConn.setRequestProperty ( "ping.pwd", rping_pwd );
            // ping.instanceId is optional and only needs to be specified if multiple instances of ReferenceId adapter are configured.
            httpsURLConn.setRequestProperty ( "ping.instanceId", instance_id);

            String encoding = httpsURLConn.getContentEncoding ();



//        PF Part
            try (InputStream is = httpsURLConn.getInputStream ()) {
                InputStreamReader streamReader = new InputStreamReader ( is, encoding != null ? encoding : "UTF-8" );

                JSONParser parser = new JSONParser ();
                JSONObject spUserAttributes = (JSONObject) parser.parse ( streamReader );

                if (spUserAttributes.size () == 0) {
                    return ResponseEntity.badRequest ().body ( new AuthToken () );
                }
                String username = spUserAttributes.get ( "subject" ).toString ();

                if (username != null) {
                    //register user when user can't be found in pingid
                    User user = new User ( spUserAttributes );
                    PingIdProperties pingIdProperties = new PingIdProperties ();
                    pingIdProperties.setProperties ( 0 );
                    Operation operation = new Operation ( pingIdProperties.getOrgAlias (), pingIdProperties.getPingid_token (), pingIdProperties.getPingid_use_base64_key (), pingIdProperties.getApi_url () );
                    operation.setTargetUser ( username );
                    JSONObject userDetails = pingIdOperationService.getUserDetails ( operation );
                    if (userDetails == null) {
                        pingIdOperationService.addUser ( user, false, operation );

                    }else{
                        pingIdOperationService.editUser ( user, false, operation );
                    }


                    //username = username.toLowerCase ();
                    try {
                        final String token = jwtTokenUtil.generateToken ( user );
                        return ResponseEntity.status ( HttpStatus.OK ).body ( new AuthToken ( token, username ) );
//                    return new ApiResponse<> ( 200, "success", new AuthToken ( token, username ) );
                    } catch (Exception ex) {
                        LOG.error ( "", ex );
                        return HelperUtil.handleException ( ex );
                    }
                }
            } catch (ParseException e) {
                System.out.println ( "Certificate Error!!" );
                LOG.error ( "Error processing user details, Please contact Administrator", e );
            }
            return null;
        }

    }
}

