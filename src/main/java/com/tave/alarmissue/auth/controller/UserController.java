package com.tave.alarmissue.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user/v1")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<String> getAuthenticatedUserInfo(@AuthenticationPrincipal User currentUser) {

        String userId = currentUser.getUsername();

        return ResponseEntity.ok("인증된 사용자: " + userId);
    }
}
