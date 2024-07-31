package com.team13.serviceuser.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team13.serviceuser.service.SignInService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = IndexController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
        "app.test-string=test",
        "app.jwt.keep=1800"
})
class IndexControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SignInService signInService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void IndexController_TestString(@Value("${app.test-string}") String configTestString) throws Exception {
        ResultActions response = mockMvc.perform(get("/config"));

        response.andExpect(MockMvcResultMatchers.content().string(configTestString));
    }

    @Test
    public void IndexController_SignIn_Success() throws Exception {
        Map<String, String> loginInfo = new HashMap<>();
        loginInfo.put("user_id", "ypjun100");

        when(signInService.verifyLoginInfo(Mockito.anyMap())).thenReturn(true);
        when(signInService.createToken(Mockito.anyString())).thenReturn("test");

        ResultActions response = mockMvc.perform(post("/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginInfo)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.cookie().exists("LTK"));
    }

    @Test
    public void IndexController_SignIn_Unauthorized() throws Exception {
        Map<String, String> loginInfo = new HashMap<>();

        when(signInService.verifyLoginInfo(Mockito.anyMap())).thenReturn(false);

        ResultActions response = mockMvc.perform(post("/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginInfo)));

        response.andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}