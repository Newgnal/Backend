package com.tave.alarmissue.news.domain.enums;

public enum Thema {
    SEMICONDUCTOR_AI("반도체·AI"),
    IT_INTERNET("IT·인터넷 서비스"),
    FINANCE_INSURANCE("금융·보험"),
    MOBILITY("자동차·모빌리티"),
    DEFENSE_AEROSPACE("방산·항공우주"),
    SECOND_BATTERY_ENVIRONMENT("2차전지·친환경 에너지"),
    REAL_ESTATE_REIT("부동산·리츠"),
    BOND_INTEREST("채권·금리"),
    HEALTHCARE_BIO("헬스케어·바이오"),
    EXCHANGE_RATE("환율·외환"),
    RAW_MATERIAL_METALS("원자재·귀금속"),
    ETC("기타");



    private final String displayName;

    Thema(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }

    public static Thema fromString(String value) {
        if (value == null) return ETC;

        for (Thema thema : Thema.values()) {
            if (thema.getDisplayName().equalsIgnoreCase(value.trim())) {
                return thema;
            }
        }
        return ETC;
    }
}
