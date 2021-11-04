package com.ssportal.be.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("")
    public String test() {
        return "Test success.";
    }
}