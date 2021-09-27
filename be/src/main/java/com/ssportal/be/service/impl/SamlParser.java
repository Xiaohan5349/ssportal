package com.ssportal.be.service.impl;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.http.HttpSession;

import com.ssportal.be.pingid.model.PingIdProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class SamlParser extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(SamlParser.class);

    /**
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PingIdProperties pingidprops = new PingIdProperties();
        pingidprops.setProperties ( 0 );
        try {
            // Grab the value of the reference Id from the request, this
            // will be sent by PingFederate as a query parameter 'REF'
            String referenceValue = request.getParameter("REF");
            if (StringUtils.isNotBlank(referenceValue)) {
                referenceValue = referenceValue.replaceAll("[^A-Za-z0-9]", "");
            }
            String base_url = pingidprops.getBaseUrl();

            // Call back to PF to get the attributes associated with the reference
            String pickupLocation = base_url + "/ext/ref/pickup?REF=" + referenceValue;
            LOG.debug(pickupLocation);
            URL pickUrl = new URL(pickupLocation);
            HttpURLConnection httpURLConn = (HttpURLConnection) pickUrl.openConnection();
            httpURLConn.setRequestProperty("ping.uname", pingidprops.getRefidUser());
            httpURLConn.setRequestProperty("ping.pwd", pingidprops.getRefidPwd());
            // ping.instanceId is optional and only needs to be specified if multiple instances of ReferenceId adapter are configured.
            httpURLConn.setRequestProperty("ping.instanceId", pingidprops.getInstanceid());

            // Get the response and parse it into another JSON object which are the
            //'user attributes'.
            // This example uses UTF-8 if encoding is not found in request.
            String encoding = httpURLConn.getContentEncoding();
            try (InputStream is = httpURLConn.getInputStream()) {
                InputStreamReader streamReader = new InputStreamReader(is, encoding != null ? encoding : "UTF-8");

                JSONParser parser = new JSONParser();
                JSONObject spUserAttributes = (JSONObject) parser.parse(streamReader);
            }
        } catch (Exception e) {
            LOG.error("|" + (StringUtils.isBlank(request.getHeader(pingidprops.getClientIP())) ? request.getRemoteAddr() : request.getHeader(pingidprops.getClientIP())) + "|" + e.getMessage());
            request.setAttribute("ErrorDescription", "Error processing user details, Please contact Administrator.");
            //how can we achieve request forward between spring boot and angular
            final RequestDispatcher dispatcher = request.getRequestDispatcher("userportal/error.jsp");
            dispatcher.forward(request, response);
        }

    }
}
