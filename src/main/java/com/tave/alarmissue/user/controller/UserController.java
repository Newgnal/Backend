package com.tave.alarmissue.user.controller;

import com.tave.alarmissue.auth.dto.request.PrincipalUserDetails;
import com.tave.alarmissue.user.dto.request.NicknameRequest;
import com.tave.alarmissue.user.dto.response.LogoutResponse;
import com.tave.alarmissue.user.dto.response.NicknameResponse;
import com.tave.alarmissue.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/v1")
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(
            @AuthenticationPrincipal PrincipalUserDetails principal

    ) {

        Long userId = principal.getUserId();

        LogoutResponse response = userService.logout(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/nickname")
    public ResponseEntity<NicknameResponse> changeNickname(
            @AuthenticationPrincipal PrincipalUserDetails principal,
            @RequestBody NicknameRequest dto
    ) {

        Long userId = principal.getUserId();
        NicknameResponse response = userService.changeNickname(userId,dto.getNickname());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @DeleteMapping()
    public ResponseEntity<Void> deleteUser(
            @AuthenticationPrincipal PrincipalUserDetails principal
    ) {

        log.debug(" delete user with ID: {}", principal.getUserId());

        Long userId = principal.getUserId();
        userService.softDeleteUser(userId);

        return ResponseEntity.noContent().build();
    }

}
