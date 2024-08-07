package com.example.ArtAuction_24.global;

public class ProfanityFilter { //비속어 필터링 기능
    private static final String[] PROFANITY_LIST = {"바보", "멍청이", "씨발", "지랄", "새끼", "병신", "쓰레기", "니애미", "느금"};

    public static boolean containsProfanity(String text) {
        for (String profanity : PROFANITY_LIST) {
            if (text.contains(profanity)) {
                return true; // 비속어가 포함되어 있다면 true 반환
            }
        }
        return false; // 비속어가 포함되어 있지 않다면 false 반환
    }
}
