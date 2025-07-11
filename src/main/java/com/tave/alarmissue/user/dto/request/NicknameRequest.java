package com.tave.alarmissue.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NicknameRequest {
    @NotBlank
    private String nickname;

}
