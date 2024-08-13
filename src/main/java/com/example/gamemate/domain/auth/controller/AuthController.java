package com.example.gamemate.domain.auth.controller;

import com.example.gamemate.domain.auth.dto.JoinDTO;
import com.example.gamemate.domain.auth.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {

        this.authService = authService;

    }

    @PostMapping("/join")
    public String joinProcess(@RequestBody JoinDTO joinDTO) {

        System.out.println(joinDTO.getUsername());
        authService.joinProcess(joinDTO);

        return "ok";

    }

//    @GetMapping("/naver-login")
//    public void naverLogin(HttpServletRequest request, HttpServletResponse response)
//        throws MalformedURLException, UnsupportedEncodingException, URISyntaxException {
//
//        String url = authService.getNaverAuthorizeUrl("authorize");
//
//        try {
//            response.sendRedirect(url);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @GetMapping("/login/oauth2/code/naver")
//    public void callBack(
//        HttpServletRequest request,
//        HttpServletResponse response,
//        NaverCallbackDTO callback) throws
//        IOException,
//        URISyntaxException {
//
//        if(callback.getError() != null) {
//            log.info(callback.getError_description());
//        }
//
//        String responseToken = authService.getNaverTokenUrl("token", callback);
//
//        ObjectMapper mapper = new ObjectMapper();
//        NaverTokenDTO token = mapper.readValue(responseToken, NaverTokenDTO.class);
//        String responseUser = authService.getNaverUserByToken(token);
//        NaverResponse naverUser = mapper.readValue(responseUser, NaverResponse.class);
//
//        response.sendRedirect("/");
//
//    }

//    //백엔드에서 쿠키에서 JWT를 읽어 응답의 헤더로 반환
//    @GetMapping("/token")
//    public ResponseEntity<Void> getToken(@CookieValue(name = "jwt", required = false) String jwtToken) {
//
//        if(jwtToken == null) {
//
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//
//        }
//
//        //헤더에 JWT 추가
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + jwtToken);
//
//        return ResponseEntity.ok().headers(headers).build();
//
//    }

}
