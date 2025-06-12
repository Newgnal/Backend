package com.tave.alarmissue.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String email;
    private String name;        // 닉네임 또는 전체 이름
    private String givenName;   // 이름 (없으면 null)
    private String familyName;  // 성 (없으면 null)
    private String picture;     // 프로필 이미지 URL
}
