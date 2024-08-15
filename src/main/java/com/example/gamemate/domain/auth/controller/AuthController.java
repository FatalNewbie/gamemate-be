package com.example.gamemate.domain.auth.controller;

import com.example.gamemate.domain.auth.dto.JoinDTO;
import com.example.gamemate.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "게임메이트 인증 API")
@Slf4j
@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {

        this.authService = authService;

    }

    @PostMapping("/join")
    public String joinProcess(@RequestBody JoinDTO joinDTO) {

        log.info(joinDTO.getUsername());
        authService.joinProcess(joinDTO);

        return "ok";

    }

    @GetMapping("/check-availability")
    public ResponseEntity<Map<String, String>> checkAvailability(
        @RequestParam("username") String username,
        @RequestParam("nickname") String nickname) {

        Map<String, String> response = new HashMap<>();

        boolean isUsernameExist = authService.isEmailDuplicated(username);
        boolean isNicknameExist = authService.isNicknameDuplicated(nickname);

        if (isUsernameExist) {
            response.put("emailError", "이미 존재하는 이메일입니다.");
        }

        if (isNicknameExist) {
            response.put("nicknameError", "이미 존재하는 닉네임입니다.");
        }

        if (response.isEmpty()) {
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body(response);
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
