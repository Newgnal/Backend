package com.tave.alarmissue.news.domain.enums;

public enum Thema {
    SEMICONDUCTOR_AI,
    IT_INTERNET,
    FINANCE_INSURANCE,
    MOBILITY,
    DEFENSE_AEROSPACE,
    SECOND_BATTERY_ENVIRONMENT,
    REAL_ESTATE_REIT,
    BOND_INTEREST,
    HEALTHCARE_BIO,
    EXCHANGE_RATE,
    RAW_MATERIAL_METALS,
    ETC;

    // string -> enum 변환
    public static Thema fromStringOrNull(String thema) {
        try {
            return Thema.valueOf(thema.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }
}
