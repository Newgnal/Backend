package com.tave.alarmissue.ai.dto.response;


public class SentimentResponse {
    private Float score;  // 필드명도 score로 변경

    // 기본 생성자
    public SentimentResponse() {}

    // Float 매개변수 생성자 추가
    public SentimentResponse(Float score) {
        this.score = score;
    }

    // Double을 받는 생성자도 추가 (편의를 위해)
    public SentimentResponse(Double score) {
        this.score = score != null ? score.floatValue() : null;
    }

    // getter, setter
    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }
}
