package com.example.gamemate.domain.auth.controller;

import com.example.gamemate.domain.auth.dto.JoinDTO;
import com.example.gamemate.domain.auth.service.AuthService;
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

}
