package io.openvidu.basic.java.jwt;

import io.openvidu.basic.java.user.User;
import io.openvidu.basic.java.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;
//    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 사용자 요청 헤더에서 RefreshToken 추출
        // -> RefreshToken이 없거나 유효하지 않다면(DB에 저장된 RefreshToken과 다르다면) null을 반환
        // 사용자의 요청 헤더에 RefreshToken이 있는 경우는, AccessToken이 만료되어 요청한 경우


        String refreshToken = jwtService.extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        String accessToken = jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);
        log.info("accessToken :{}",accessToken);

        if(accessToken==null){
            log.info("유효하지않은 accessToken");
            return;
        }

        // RefreshToken이 요청 헤더에 존재했다면,
        // 사용자가 AccessToken이 만료되어서 RefreshToken까지 보낸 것이므로
        // RefreshToken이 DB의 리프레시 토큰과 일치하는지 판단 후,
        // 일치한다면 AccessToken을 재발급
//        if (refreshToken != null) {
//            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
//            return; // RefreshToken을 보낸 경우에는 AccessToken을 재발급 하고 인증 처리는 하지 않게 하기위해 바로 return
//        }

//     RefreshToken이 없거나 유효하지 않다면, AccessToken을 검사하고 인증을 처리하는 로직 수행
//     AccessToken이 없거나 유효하지 않다면, 인증 객체가 담기지 않은 상태로 다음 필터로 넘어가기 때문에 403 에러 발생
//     AccessToken이 유효하다면, 인증 객체가 담긴 상태로 다음 필터로 넘어가기 때문에 인증 성공
        if (refreshToken == null) {
            checkAccessTokenAndAuthentication(request, response, filterChain);
        }

    }

    public void checkAccessTokenAndAuthentication(HttpServletRequest request,
                                                  HttpServletResponse response,
                                                  FilterChain filterChain) throws ServletException, IOException {

        log.info("checkAccessTokenAndAuthentication() 호출 ");

        jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .ifPresent(accessToken -> jwtService.extractNickName(accessToken)
                        .ifPresent((nickname) ->{
                            log.info("nickname : {}",nickname );
                            saveAuthentication(userRepository.findByNickname(nickname).get());})
                        );
//                .ifPresent(this::saveAuthentication)));
        if (jwtService.extractAccessToken(request).isPresent()) {
            if (!jwtService.isTokenValid(jwtService.extractAccessToken(request).get())) {
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
    public void saveAuthentication(User myUser) {
//        log.info("saveAutehntication : {}",myUser.getNickname());
//        if (myUser == null) {
//            return;
//        }
//
//        UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
//                .username(myUser.getNickname())
//                .roles(myUser.getRole().name())
//                .build();
//
//        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsUser,
//                null,
//                authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
