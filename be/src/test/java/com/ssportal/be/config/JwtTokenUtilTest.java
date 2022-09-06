package com.ssportal.be.config;

import com.cedarsoftware.util.io.JsonObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.ssportal.be.model.User;
import io.jsonwebtoken.JwtParser;
import org.apache.commons.lang3.RandomStringUtils;
import org.eclipse.jetty.util.Callback;
import org.json.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith (SpringRunner.class)
@SpringBootTest
class JwtTokenUtilTest {

    private static final String ALGORITHM = "AES";
    private static final byte[] KEY = "ADBSJHJS12547896".getBytes();

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @BeforeEach
    void setUp() {
        System.out.println ( "before each" );
    }

    public static class Config {
        public String amBaseURL;
        public String amSesionCookeName;
        public String fidoBaseURL;
        public String type;
        public String realm;
    }
    @Test
    void TestOthers(){
        long nowInSeconds = System.currentTimeMillis() / 1000;
        System.out.println ( nowInSeconds );
        System.out.println ( nowInSeconds + 10 * 60 );

    }
    @Test
    public void testObjectMapper() throws IOException {



    }
    @Test
    public void testPath() throws IOException {
        Path testPath = Paths.get("test1/test2", "test3/test4");
        byte[] test = testPath.toString ().getBytes ();
        System.out.println ( testPath.toString ());
        String abc = new String(test);

        System.out.println ( abc );
    }
    @Test
    void TestCompletableFuture() throws ExecutionException, InterruptedException {
        String message = "test Completed Future";
//        how to create completable future
//        CompletableFuture<String> future1 = new CompletableFuture<> ();
//        CompletableFuture<String> future1 = CompletableFuture.completedFuture (message);
//        CompletableFuture<String> future1 = ompletableFuture.supplyAsync (

        CompletableFuture<Integer> future0 = new CompletableFuture<> ();

        future0.supplyAsync ( () -> {
            int res = 0;
            for (int i = 0; i < 100; i++ ){
                res += i;
            }
            future0.complete ( res );
            return res;
        } );


        System.out.println ( future0.get () );

        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync ( () -> {
            int res = 0;
            for (int i = 0; i < 100; i++ ){
                res += i;
            }

            return res;
        } );

        CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(() -> {
            int i =1/0;
            return 1;
        });
//        System.out.println (  f1.getNow (  2 ));

//        join will throw unchecked exception so we won't need to add catch, get will throw checked exception, we need to throw or add try catch
//        f1.join ();
//          f1.get ();
//        CompletableFuture.allOf(f1).join();
        System.out.println("CompletableFuture Test");
        System.out.println ( future1.get () );
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            int a = 0;
            for(int i = 0; i < 100; i++){
                a += i;
            }
            int s = 1/0;
            return 10086;
        });
        //future.complete ( 2 );
        future.whenCompleteAsync ((result, error) -> {
            System.out.println("拨打"+result);
            error.printStackTrace();
        });

        System.out.println ( "test2" );
    }
    @Test
    void TestCompletableFuture2() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        Integer valueToSet = 0;
        boolean flag = true;
        System.out.println ( "start decode" );
        String abc = "test123434234drfser";


        byte[] bytes = abc.getBytes ();
        System.out.println ( new String( bytes, StandardCharsets.UTF_8) );
        byte[] encodeBytes = Base64.getEncoder ().encode ( abc.getBytes () );
        String decodeAbc = new String( Base64.getDecoder ().decode ( encodeBytes), StandardCharsets.UTF_8);

        String key = "7Bw&r5oM*GQz4%gx";
        byte[] keyByteArray  = key.getBytes();
        String aes = new String( Base64.getEncoder ().encode ( keyByteArray ), StandardCharsets.UTF_8);
        System.out.println ( "encoded key: " + aes );
        String decodeKey = new String (Base64.getDecoder ().decode ( aes ),StandardCharsets.UTF_8 );
        assertEquals ( key, decodeKey   );
        assertEquals ( abc, decodeAbc );

        flag = completableFuture.complete(3);
        Integer result = completableFuture.get();
        if(flag) System.out.println("Future moved to complete state with value - " + result);
        else System.out.println("Future not moved to complete state");
    }
//    @Test
//    void generateToken() throws JSONException {
//        String token = jwtTokenUtil.generateToken ( "test" );
//        String[] tokens = token.split ( "\\." );
//        Base64.Decoder decoder = Base64.getDecoder ();
//        String payload = new String ( decoder.decode ( tokens[1] ) );
//        JSONObject payload_json = new JSONObject(payload);
//        int startDate = (int)payload_json.get("iat");
//        int endDate = (int) payload_json.get("exp");
//        int expiration = endDate - startDate;
//        assertEquals ( 60*20,endDate - startDate );
//        System.out.println ( "\"test\"" );
//    }

    @Test
    @Disabled
    void testFunction() throws Exception {
//        StringBuilder sb = new StringBuilder (  );
//
//        List<String> configs = new ArrayList<String> ( Arrays.asList ( "abc", "test1", "test2", "edf" ));
//
//        configs.stream ().forEach ( c -> {
//            System.out.println ( c );
//        } );

//        String test = RandomStringUtils.random ( 8, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890" );
//        System.out.println ( test );
        DateFormat df = new SimpleDateFormat ("yyyyMMddHHmmss");
        Date date = new Date ();
        String date2 = df.format ( date ) + "Z";
        long date1 = Long.parseLong ( date2 );
        System.out.println ( df.format ( date ) );

    }

    @Test
    public void SimpleTest(){
        String abc = "2022628086";
        abc = "1"+abc;
        System.out.println (abc);
    }


}