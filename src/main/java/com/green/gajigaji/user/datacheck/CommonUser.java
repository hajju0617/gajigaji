package com.green.gajigaji.user.datacheck;

import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUser {

    private static final String NICKNAME_REGEX = "^[0-9a-zA-Z가-힣]{4,10}$";

    public static boolean isValidNickname(String nickname) {
        return nickname.matches(NICKNAME_REGEX);
    }

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9]{6,15}@[a-z]{3,7}\\.(com|net){1}$";


    public static boolean isValidEmail(String email) {
        return email.matches(EMAIL_REGEX);
    }
//    private static final String BIRTHDATE_REGEX = "^(19|20)\\d{2}-(0[13578]|1[02])-31$|^(19|20)\\d{2}-(0[1,3-9]|1[0-2])-30$|^(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|1\\d|2[0-8])$";
//    public static boolean isValidBirthDate(String birth) {
//        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
//        String str = date.format(birth);
//        return birth.matches(BIRTHDATE_REGEX);
//    }

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    public static boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setLenient(false); // 유연하지 않은 분석을 설정하여 정확한 날짜를 요구
        try {
            // 문자열을 Date 객체로 변환
            Date date = sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
    public static Date convertToDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setLenient(false); // 유연하지 않은 분석을 설정하여 정확한 날짜를 요구
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + dateStr, e);
        }
    }

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@^*?";

    private static final SecureRandom RANDOM = new SecureRandom();
    public static String tempPassword(int length) {
        StringBuilder password = new StringBuilder(length);
        password.append(UPPER.charAt(RANDOM.nextInt(UPPER.length())));
        password.append(LOWER.charAt(RANDOM.nextInt(LOWER.length())));
        password.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
        password.append(SPECIAL.charAt(RANDOM.nextInt(SPECIAL.length())));
        String allChars = UPPER + LOWER + DIGITS;
        for (int i = 4; i < length; i++) {
            password.append(allChars.charAt(RANDOM.nextInt(allChars.length())));
        }
        return password.toString();
    }

    public static String maskEmail(String email) {
        String id = extractIdFromEmail(email);
        String maskedId = maskId(id);
        String domain = email.split("@")[1];
        return maskedId + "@" + domain;
    }
    public static String extractIdFromEmail(String email) {
        return email.split("@")[0];
    }
    public static String maskId(String id) {
        int length = id.length();
        StringBuilder maskedId = new StringBuilder(id);

        if (length == 6) {
            // 뒤에서 두 번째까지
            for (int i = length - 2; i < length; i++) {
                maskedId.setCharAt(i, '*');
            }
        } else if (length > 6 && length <= 9) {
            // 뒤에서 세 번째까지
            for (int i = length - 3; i < length; i++) {
                maskedId.setCharAt(i, '*');
            }
        } else if (length > 9) {
            // 뒤에서 다섯 번째까지
            for (int i = length - 5; i < length; i++) {
                maskedId.setCharAt(i, '*');
            }
        }
        return maskedId.toString();
    }

}
