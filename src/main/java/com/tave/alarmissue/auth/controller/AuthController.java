package com.tave.alarmissue.auth.controller;

import com.tave.alarmissue.auth.client.KakaoApiClient;
import com.tave.alarmissue.auth.converter.AuthConverter;
import com.tave.alarmissue.auth.dto.response.JwtLoginResponse;
import com.tave.alarmissue.auth.dto.response.SocialLoginResponse;
import com.tave.alarmissue.auth.dto.request.TokenRequest;
import com.tave.alarmissue.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.desktop.UserSessionEvent;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/v1")
public class AuthController {

    private final AuthService authService;
    private final KakaoApiClient kakaoApiClient;


//    @GetMapping("/login/kakao")
//    public ResponseEntity<JwtLoginResponse> kakaoLogin (
//            @Valid @RequestParam String code) {
//
//        JwtLoginResponse response = authService.loginOrRegister(code);
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.AUTHORIZATION, response.getAccessToken())
//                .body(response);
//
//    }

    @GetMapping("/auth/login/kakao")
    public ResponseEntity<JwtLoginResponse> kakaoLoginToken(@RequestParam String code) {
        JwtLoginResponse loginResponse = authService.loginOrRegister(code);
        return ResponseEntity.ok(loginResponse);
    }


}
