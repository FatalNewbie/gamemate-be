package com.example.gamemate.domain.auth.controller;

import com.example.gamemate.domain.auth.dto.JoinDTO;
import com.example.gamemate.domain.auth.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    //백엔드에서 쿠키에서 JWT를 읽어 응답의 헤더로 반환
    @GetMapping("/token")
    public ResponseEntity<Void> getToken(@CookieValue(name = "jwt", required = false) String jwtToken) {

        if(jwtToken == null) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        }

        //헤더에 JWT 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtToken);

        return ResponseEntity.ok().headers(headers).build();

    }

}
