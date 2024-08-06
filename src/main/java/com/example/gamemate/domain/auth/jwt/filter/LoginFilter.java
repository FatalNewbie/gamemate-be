package com.example.gamemate.domain.auth.jwt.filter;

import com.example.gamemate.domain.auth.dto.CustomUserDetailsDTO;
import com.example.gamemate.domain.auth.dto.ExceptionDTO;
import com.example.gamemate.domain.auth.dto.LoginDTO;
import com.example.gamemate.domain.auth.jwt.JWTUtil;
import com.example.gamemate.domain.user.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    //JWTUtil 주입
    private final JWTUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, CustomUserDetailsService customUserDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // request에 있는 username과 password를 파싱해서 자바 Object로 받기
        ObjectMapper om = new ObjectMapper();
        LoginDTO loginDto = null;
        try {
            loginDto = om.readValue(request.getInputStream(), LoginDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            handleLoginAttempt(loginDto);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword(), null);
            return authenticationManager.authenticate(authToken);
        } catch (UsernameNotFoundException e) {
            // JSON 형태의 에러 응답 보내기
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            try {
                om.writeValue(response.getOutputStream(), new ExceptionDTO("USER_NOT_FOUND", "존재하지 않는 계정"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            throw new UsernameNotFoundException("잘못된 로그인 정보입니다.");
        } catch (BadCredentialsException e) {
            // JSON 형태의 에러 응답 보내기
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            try {
                om.writeValue(response.getOutputStream(), new ExceptionDTO("INVALID_PASSWORD", "비밀번호 오류"));
            } catch (IOException ex){
                ex.printStackTrace();
            }
            throw new BadCredentialsException("비밀번호가 틀렸습니다");
        } catch (DisabledException e) {
            // JSON 형태의 에러 응답 보내기
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            try {
                om.writeValue(response.getOutputStream(), new ExceptionDTO("ACCOUNT_DISABLED", "삭제된 계정"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            throw new DisabledException("삭제된 계정입니다. 다시 확인해주세요.");
        }

//        //클라이언트 요청에서 email, password 추출
//        String username = obtainUsername(request);
//        String password = obtainPassword(request);
//
//        //스프링 시큐리티에서 email과 password를 검증하기 위해서는 token에 담아야 함
//        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
//
//        //token에 담은 검증을 위한 AuthenticationManager로 전달
//        return authenticationManager.authenticate(authToken);

    }

    //로그인 성공 시 실행하는 메소드(여기서 JWT 발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        //UserDetails
        CustomUserDetailsDTO customUserDetails = (CustomUserDetailsDTO) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(username, role, 60*60*10L);

        //추가한 로직
        Cookie cookie = new Cookie("jwt",token);
        //cookie.setHttpOnly(true);
        cookie.setHttpOnly(false);
        //cookie.setSecure(true); // HTTPS에서만 작동
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60); // 쿠키 유효 기간 설정 (예: 1시간)
        response.addCookie(cookie);

        String cookieHeader = String.format("%s=%s; Max-Age=%d; Expires=%s; Path=%s; Secure; HttpOnly; SameSite=None",
                cookie.getName(), cookie.getValue(), cookie.getMaxAge(), getExpiryDate(cookie), cookie.getPath());

        response.setHeader("Set-Cookie", cookieHeader);
        //

        response.addHeader("Authorization", "Bearer " + token);

        System.out.println("JWT cokkie : name=" + cookie.getName() + ", value=" + cookie.getValue());

    }

    // 이거도 추가함
    private String getExpiryDate(Cookie cookie) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(new Date(System.currentTimeMillis() + cookie.getMaxAge() * 1000L));
    }

    //로그인 실패 시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

        //로그인 실패 시 401 응답 코드 반환
        response.setStatus(401);

    }

    private void handleLoginAttempt(LoginDTO loginDto) {
        if (customUserDetailsService.loadUserByUsername(loginDto.getUsername()) == null) {
            System.out.println("존재하지 않는 계정입니다. 새로운 계정을 만들어주세요.");
            throw new UsernameNotFoundException("존재하지 않는 계정입니다. 새로운 계정을 만들어주세요.");
        }

        CustomUserDetailsDTO user = (CustomUserDetailsDTO) customUserDetailsService.loadUserByUsername(loginDto.getUsername());

        if (!bCryptPasswordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            System.out.println("비밀번호가 틀립니다. 다시 확인해주세요.");
            throw new BadCredentialsException("잘못된 로그인 정보입니다.");
        }

//        if (user.isDeleted()) {
//            System.out.println("이미 탈퇴된 계정입니다. 새로운 계정을 만들어주세요.");
//            throw new DisabledException("계정이 비활성화되었습니다.");
//        }
    }
}
