package com.ssportal.be.config;

import com.cedarsoftware.util.io.JsonObject;
import io.jsonwebtoken.JwtParser;
import org.json.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@RunWith (SpringRunner.class)
@SpringBootTest
class JwtTokenUtilTest {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @BeforeEach
    void setUp() {
        System.out.println ( "before each" );
    }

    @Test
    void generateToken() throws JSONException {
        String token = jwtTokenUtil.generateToken ( "test" );
        String[] tokens = token.split ( "\\." );
        Base64.Decoder decoder = Base64.getDecoder ();
        String payload = new String ( decoder.decode ( tokens[1] ) );
        JSONObject payload_json = new JSONObject(payload);
        int startDate = (int)payload_json.get("iat");
        int endDate = (int) payload_json.get("exp");
        int expiration = endDate - startDate;
        assertEquals ( 60*20,endDate - startDate );
    }

}