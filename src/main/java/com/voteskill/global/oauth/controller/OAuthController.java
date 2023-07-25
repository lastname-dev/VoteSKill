
package com.voteskill.global.oauth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.voteskill.global.jwt.JwtService;
import com.voteskill.global.oauth.service.OAuthService;
import com.voteskill.user.dto.UserOauthInfoDto;
import com.voteskill.user.entity.UserEntity;
import com.voteskill.user.service.UserService;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/oauth")
public class OAuthController {

    private final OAuthService oAuthService;

    /**
     * 카카오 callback [GET] /oauth/kakao/callback
     */
    @CrossOrigin
    @ResponseBody
    @GetMapping("/kakao/callback")
    public ResponseEntity<?> kakaoCallback(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        //  accessToken 발급받기
        String accessToken = oAuthService.getKakaoAccessToken(code);
        //  userInfo 받아오기
        UserOauthInfoDto userInfo = oAuthService.getUserInfo(accessToken);
        System.out.println(userInfo); //userInfo 에 담긴 것 : nickname

        // Todo: 회원 유무 확인 및 jwt token 반환
        UserOauthInfoDto userOauthInfoDto = oAuthService.checkRegistedUser(userInfo, response);
        return ResponseEntity.ok(userOauthInfoDto);
    }
}