package com.voteskill.global.oauth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voteskill.global.oauth.controller.OAuthController;
import com.voteskill.global.oauth.service.OAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class OAuthControllerTest {

    @Mock
    private OAuthService oAuthService;

    @InjectMocks
    private OAuthController oAuthController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("인증 코드로 kakao 토큰 받아오기")
    public void testKakaoCallback() throws JsonProcessingException {
        String mockCode = "mock_code";
        String mockAccessToken = "mock_access_token";
        String mockUserInfo = getMockUserInfo();

        // OAuthService의 getKakaoAccessToken() 메소드가 mockAccessToken을 반환하도록 설정
        when(oAuthService.getKakaoAccessToken(anyString())).thenReturn(mockAccessToken);
        // OAuthService의 getUserInfo() 메소드가 mockUserInfo를 반환하도록 설정
        when(oAuthService.getUserInfo(anyString())).thenReturn(mockUserInfo);

        ResponseEntity<?> responseEntity = oAuthController.kakaoCallback(mockCode);

        // 예상한 결과와 실제 반환값이 일치하는지 확인
        assertEquals(ResponseEntity.ok("성공~!"), responseEntity);
    }

    public String getMockUserInfo() throws JsonProcessingException {
        Map<String, Object> mockUserInfo = new HashMap<>();
        mockUserInfo.put("nickname", "jm");
        mockUserInfo.put("email", "jm@naver.com");

        // Jackson ObjectMapper를 사용하여 JSON 형태의 문자열로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String mockUserInfoJson = objectMapper.writeValueAsString(mockUserInfo);
        return mockUserInfoJson;
    }
}