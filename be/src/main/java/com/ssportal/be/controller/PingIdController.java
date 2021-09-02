package com.ssportal.be.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ssportal.be.pingid.model.PingIdProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pingid")
public class PingIdController {
    private static final Logger LOG = Logger.getLogger(PingIdController.class);

    @RequestMapping(value = "/operation", method = RequestMethod.POST)
    public void doOperation(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    }
}
