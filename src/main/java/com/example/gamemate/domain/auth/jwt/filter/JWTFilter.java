package com.example.gamemate.domain.auth.jwt.filter;

import com.example.gamemate.domain.auth.dto.CustomUserDetailsDTO;
import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.domain.user.entity.User.RoleType;
import com.example.gamemate.domain.auth.jwt.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;

    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

//        boolean isOAuth2User = isOAuth2User();

        //cookie들을 불러온 뒤 Authorization Key에 담긴 쿠키를 찾음
        String authorization = null;
        Cookie[] cookies = request.getCookies();

        if(cookies != null) {
            for (Cookie cookie : cookies) {

                log.info("cookie name" + cookie.getName());
                if (cookie.getName().equals("Authorization")) {
//                if ("Authorization".equals(cookie.getName())) {

                    authorization = cookie.getValue();
                    log.info("token check from cookie: " + authorization);
                    break;

                }
            }
        }
//        else {
//        //request에서 Authorization 헤더를 찾음
//        authorization = "Bearer " + request.getHeader("Authorization");
//        }

        // 쿠키에서 Authorization을 찾지 못한 경우, 헤더에서 가져오기
        if (authorization == null) {
            authorization = request.getHeader("Authorization");
            if (authorization != null) {
                log.info("token check from header: " + authorization);
            }
        }


        //Authorization 헤더 검증
        if(authorization == null) {

            log.info("token is null");
            filterChain.doFilter(request, response);

            //조건에 해당되면 메소드 종료
            return;
        }

        log.info("authorization now");

        //Bearer 부분 제거 후 순수 토큰만 획득
//        String token = authorization.split(" ")[1];
        String token = null;
        String[] authParts = authorization.split(" ");
        if (authParts.length == 2 && "Bearer".equalsIgnoreCase(authParts[0])) {
            token = authParts[1];
        } else {
            log.info("Invalid authorization format");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid authorization format");
            return;
        }

        //토큰 소멸 시간 검증
        //유효기간이 만료한 경우
        if(jwtUtil.isExpired(token)) {

            log.info("token expired");
//            filterChain.doFilter(request, response);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has expired");

            //조건에 해당되면 메소드 종료
            return;
        }

        //토큰에서 username과 role 획득
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        Authentication authToken;

        //OAuth2 사용자의 경우
//        if(isOAuth2User) {
//
//            //oath2DTO를 생성하여 값 set
//            OAuth2DTO oAuth2DTO = new OAuth2DTO();
//            oAuth2DTO.setUsername(username);
//            oAuth2DTO.setRole(role);
//
//            CustomOAuth2UserDTO customOAuth2UserDTO = new CustomOAuth2UserDTO(oAuth2DTO);
//
//            authToken = new UsernamePasswordAuthenticationToken(
//                customOAuth2UserDTO,
//                null,
//                customOAuth2UserDTO.getAuthorities()
//            );

//        } else {

            //user를 생성하여 값 set
            User user = new User();
            user.setUsername(username);
            user.setPassword("temppassword");
            user.setRole(RoleType.valueOf(role));

            //UserDetails에 회원 정보 객체 담기
            CustomUserDetailsDTO customUserDetails = new CustomUserDetailsDTO(user);

            //스프링 시큐리티 인증 토큰 생성
            authToken = new UsernamePasswordAuthenticationToken(
                customUserDetails,
                null,
                customUserDetails.getAuthorities()
            );
//        }

        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);

    }

//    private boolean isOAuth2User() {
//        return SecurityContextHolder
//            .getContext()
//            .getAuthentication() != null
//            &&
//            SecurityContextHolder
//                .getContext()
//                .getAuthentication()
//                .getPrincipal() instanceof CustomOAuth2UserDTO;
//    }

}
