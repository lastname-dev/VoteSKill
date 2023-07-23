package com.voteskill.global.oauth;

import com.voteskill.global.oauth.controller.OAuthController;
import com.voteskill.global.oauth.service.OAuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OAuthController.class)
class OAuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OAuthService oAuthService;

    @Test
    void kakaoCallback() {
    }
}