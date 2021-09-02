package com.ssportal.be.pingid.utils;

import com.ssportal.be.pingid.model.Operation;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jose4j.base64url.Base64;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class OperationHelpers {
    private static final Logger LOG = Logger.getLogger(OperationHelpers.class);

    @SuppressWarnings("unchecked")
    public static String buildRequestToken(JSONObject requestBody, Operation operation) {
        // Building request header using tenant details
        JSONObject requestHeader = buildRequestHeader(operation);

        JSONObject payload = new JSONObject();
        payload.put("reqHeader", requestHeader);
        payload.put("reqBody", requestBody);

        JsonWebSignature jws = new JsonWebSignature();

        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
        jws.setHeader("org_alias", operation.getOrgAlias());
        jws.setHeader("token", operation.getToken());

        jws.setPayload(payload.toJSONString());

        // Set the verification key
        HmacKey key = new HmacKey(Base64.decode(operation.getUseBase64Key()));
        jws.setKey(key);

        String jwsCompactSerialization = null;
        try {
            jwsCompactSerialization = jws.getCompactSerialization();
        } catch (JoseException e) {
            LOG.error(e.getMessage());
        }
        // Signed request token
        operation.setRequestToken(jwsCompactSerialization);

        return jwsCompactSerialization;
    }

    @SuppressWarnings("unchecked")
    public static JSONObject buildRequestHeader(Operation operation) {

        JSONObject reqHeader = new JSONObject();
        reqHeader.put("locale", "en");
        reqHeader.put("orgAlias", operation.getOrgAlias());
        reqHeader.put("secretKey", operation.getToken());
        reqHeader.put("timestamp", getCurrentTimeStamp());
        reqHeader.put("version", operation.getApiVersion());

        return reqHeader;
    }

    public static String getCurrentTimeStamp() {

        Date currentDate = new Date();
        SimpleDateFormat PingIDDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        PingIDDateFormat.setTimeZone(TimeZone.getTimeZone("America/Denver"));

        return PingIDDateFormat.format(currentDate);
    }

    private static void sendRequest(Operation operation) {
        OutputStreamWriter outputStreamWriter = null;
        try {
            URL restUrl = new URL(operation.getEndpoint());
            HttpURLConnection urlConnection = (HttpURLConnection) restUrl.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.addRequestProperty("Content-Type", "application/json");
            urlConnection.addRequestProperty("Accept", "*/*");

            urlConnection.setDoOutput(true);
            outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8");
            outputStreamWriter.write(operation.getRequestToken());
            outputStreamWriter.flush();
            outputStreamWriter.close();

            int responseCode = urlConnection.getResponseCode();
            operation.setResponseCode(responseCode);

            if (responseCode == 200) {

                String encoding = urlConnection.getContentEncoding();
                try (InputStream is = urlConnection.getInputStream()) {
                    String stringJWS = IOUtils.toString(is, encoding);
                    operation.setRequestToken(stringJWS);
                    operation.setWasSuccessful(true);
                }
                urlConnection.disconnect();
            } else {

                String encoding = urlConnection.getContentEncoding();
                try (InputStream is = urlConnection.getErrorStream()) {
                    String stringJWS = IOUtils.toString(is, encoding);
                    operation.setRequestToken(stringJWS);
                    operation.setWasSuccessful(false);
                }
                urlConnection.disconnect();
            }
        } catch (IOException ex) {
            LOG.error(ex.getMessage());
            operation.setResponseCode(500);
            operation.setWasSuccessful(false);
        } finally {
            if (outputStreamWriter != null) {
                try {
                    outputStreamWriter.close();
                } catch (IOException ex) {
                    LOG.error(ex.getMessage());
                }
            }
        }
    }

    public static JSONObject parseResponse(Operation operation) {

        JSONParser parser = new JSONParser();
        JSONObject responsePayloadJSON = null;

        try {

            JsonWebSignature responseJWS = new JsonWebSignature();
            responseJWS.setCompactSerialization(operation.getRequestToken());
            HmacKey key = new HmacKey(Base64.decode(operation.getUseBase64Key()));
            responseJWS.setKey(key);
            // Getting response payload
            responsePayloadJSON = (JSONObject) parser.parse(responseJWS.getPayload());

            // workaround for PingID API 4.5 beta
            if (responsePayloadJSON.containsKey("responseBody")) {
                responsePayloadJSON = (JSONObject) responsePayloadJSON.get("responseBody");
            }

        } catch (JoseException | ParseException e) {
            LOG.error(e.getMessage());
        }

        if (responsePayloadJSON != null) {
            operation.setErrorId((long) responsePayloadJSON.get("errorId"));
            operation.setErrorMsg((String) responsePayloadJSON.get("errorMsg"));
            operation.setUniqueMsgId((String) responsePayloadJSON.get("uniqueMsgId"));
            operation.setClientData((String) responsePayloadJSON.get("clientData"));
        } else {
            operation.setErrorId(501);
            operation.setErrorMsg("Could not parse JWS");
            operation.setUniqueMsgId("");
            operation.setClientData("");
            operation.setWasSuccessful(false);
        }

        return responsePayloadJSON;
    }
}
