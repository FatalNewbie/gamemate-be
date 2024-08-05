package com.example.gamemate.domain.auth.controller;

import com.example.gamemate.domain.auth.dto.JoinDTO;
import com.example.gamemate.domain.auth.service.JoinService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final JoinService joinService;

    public AuthController(JoinService joinService) {

        this.joinService = joinService;

    }

    @PostMapping("/join")
    public String joinProcess(@RequestBody JoinDTO joinDTO) {

        System.out.println(joinDTO.getUsername());
        joinService.joinProcess(joinDTO);

        return "ok";

    }

}
