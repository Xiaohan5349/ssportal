package com.ssportal.be.Controller;

import com.ssportal.be.controller.MailController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MailControllerTest {
    @Autowired
    private MailController mailController;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;


    @Test
    public void testContexLoads(){
        Assert.assertThat(mailController,notNullValue());
    }

    @Test
    @Disabled
    public void TestEndpoint() throws Exception {
        String region = "US_EAST_1";
        String dent = "4";
        mockMvc.perform(post ("/test")
//                .param("region", region)
//                .param("dent", dent)
        ).andExpect(status().isOk());
    }
}