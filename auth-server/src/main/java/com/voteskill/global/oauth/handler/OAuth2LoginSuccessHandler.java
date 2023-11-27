package com.voteskill.global.oauth.handler;

import com.voteskill.user.entity.UserEntity;
import com.voteskill.user.repository.UserRepository;
import javax.servlet.ServletException;

import com.voteskill.global.jwt.JwtService;
import com.voteskill.global.oauth.CustomOAuth2User;
import com.voteskill.user.common.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

  private final JwtService jwtService;
  private final UserRepository userRepository;
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    log.info("OAuth2 Login 성공!");
    try {

      CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

      // User의 Role이 GUEST일 경우 처음 요청한 회원이므로 회원가입 페이지로 리다이렉트
      if (oAuth2User.getRole() == Role.GUEST) {

        String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
        response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
//        Cookie cookie = new Cookie("access_token",accessToken);
//        cookie.setPath("/");
////        cookie.setHttpOnly(true);
//        cookie.setMaxAge(3600);
//        response.addCookie(cookie);
        response.sendRedirect("http://localhost:3000/sign-up"); // 프론트의 회원가입 추가 정보 입력 폼으로 리다이렉트

        jwtService.sendAccessAndRefreshToken(response, accessToken, null);
        // Role을 Guest에서 User로
         UserEntity findUser = userRepository.findBySocialId(oAuth2User.getEmail())
         .orElseThrow(() -> new IllegalArgumentException("이메일에 해당하는 유저가 없습니다."));
         findUser.authorizeUser();
      } else {

        loginSuccess(response, oAuth2User); // 로그인에 성공한 경우 access, refresh 토큰 생성
      }
    } catch (Exception e) {

      throw e;
    }

  }

  // 로그인이 성공했을 경우, Access/Refresh Token 발급 후 갱신
  private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {

    String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
    String refreshToken = jwtService.createRefreshToken();
    response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
    response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);

    jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
    jwtService.updateRefreshToken(oAuth2User.getEmail(), refreshToken);
  }
}