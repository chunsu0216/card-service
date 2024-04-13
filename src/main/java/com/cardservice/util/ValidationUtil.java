package com.cardservice.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {

    /**
     * 카드 유효기간 검증
     * 현재 날짜 기준으로 YYMM 비교 메소드
     * @param expireDate
     * @return
     */
    public static boolean validExpireDate(String expireDate) {
        String yyMM = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMM"));

        return Integer.parseInt(expireDate) >= Integer.parseInt(yyMM);
    }

    /**
     * 비밀번호 앞 2자리 검증
     * @param password
     * @return
     */
    public static boolean validPassword(String password) {
        String regexp = "\\d{2}";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }

    /**
     * 생년월일 6자리 검증
     * @param userInfo
     * @return
     */
    public static boolean validUserInfo(String userInfo) {
        String regexp = "^(\\d{2})(\\d{2})(\\d{2})$";
        //String regexp = "/([0-9]{2}(0[1-9]|1[0-2])(0[1-9]|[1,2][0-9]|3[0,1]))/g";

        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(userInfo);

        return matcher.matches();
    }
}
