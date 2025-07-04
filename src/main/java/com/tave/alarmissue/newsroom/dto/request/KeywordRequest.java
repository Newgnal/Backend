package com.tave.alarmissue.newsroom.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeywordRequest {
    @NotBlank(message = "키워드는 필수입니다.")
    @Size(min = 2, max = 10, message = "키워드는 2자 이상 10자 이하로 입력해주세요.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s]+$", message = "키워드는 한글, 영문, 숫자, 띄어쓰기만 사용 가능합니다.")
    private String keyword;
}
