package com.tave.alarmissue.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NicknameResponse {
    private Long userId;
    private String nickname;
}
