package com.example.gamemate.domain.auth.oauth2;

import com.example.gamemate.domain.auth.dto.CustomOAuth2UserDTO;
import com.example.gamemate.domain.auth.jwt.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final long expirationTime; // 만료 시간 상수

    public CustomSuccessHandler(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
        this.expirationTime = 60 * 60 * 1000 * 10L;

    }

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
        ) throws IOException, ServletException {

        //OAuth2User
        CustomOAuth2UserDTO customOAuth2UserDTO = (CustomOAuth2UserDTO) authentication.getPrincipal();

        Long id = customOAuth2UserDTO.getId();
        String username = customOAuth2UserDTO.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String token = jwtUtil.createJwt(id, username , auth.getAuthority(), expirationTime);

        response.addCookie(createCookie("Authorization", token));
        response.sendRedirect("http://localhost:3000/");

    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge((int) (expirationTime / 1000));
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;

    }

}
