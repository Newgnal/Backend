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
public class AuthController {

    private final AuthService authService;
    private final KakaoApiClient kakaoApiClient;


    @PostMapping("/login/kakao")
    public ResponseEntity<JwtLoginResponse> kakaoLogin (
            @Valid @RequestBody TokenRequest tokenRequest) {

        SocialLoginResponse socialLoginResponse = authService.loginOrRegister(tokenRequest);
        String accessWithBearer = authService.createAccessTokenWhenLogin(socialLoginResponse.getUserId());

        JwtLoginResponse jwtResponse = AuthConverter.toJwtLoginResponse(socialLoginResponse, accessWithBearer);


        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, accessWithBearer)
                .body(jwtResponse);

    }

    @GetMapping("/auth/login/kakao")
    public ResponseEntity<SocialLoginResponse> kakaoLoginToken(@RequestParam String code) {

        String accessToken = kakaoApiClient.requestAccessToken(code);

        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setAccessToken(accessToken);

        SocialLoginResponse loginResponse = authService.loginOrRegister(tokenRequest);

        return ResponseEntity.ok(loginResponse);
    }


}
