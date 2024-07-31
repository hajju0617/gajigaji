package com.green.gajigaji.user.usercommon;

public class UserMessage {
    public static final String IS_NOT_DUPLICATE = "중복검사를 통과하셨습니다";
    public static final String PW_INPUT_MESSAGE = "비밀번호는 필수값";
    public static final String ID_INPUT_MESSAGE = "아이디는 필수값";
    public static final String NAME_INPUT_MESSAGE = "이름은 필수값";
    public static final String NICKNAME_INPUT_MESSAGE = "닉네임은 필수값";
    public static final String ADDRESS_INPUT_MESSAGE = "주소는 필수값";
    public static final String BIRTH_INPUT_MESSAGE = "생년월일은 필수값";
    public static final String GENDER_INPUT_MESSAGE = "성별은 필수값";
    public static final String PHONE_INPUT_MESSAGE = "전화번호는 필수값";
    public static final String EMAIL_INPUT_MESSAGE = "이메일은 필수값";

    public static final String EMAIL_REGEX_MESSAGE = "이메일 형식에 안 맞음 ( 아이디 (6~15) @ 도메인 (3~7) . com or net )";
    public static final String NICKNAME_REGEX_MESSAGE = "닉네임 형식에 안 맞음 ( 한글, 숫자, 영문 대소문자 ( 4~10 )";
    public static final String NUMBER_REGEX_MESSAGE = "전화번호 형식에 안 맞음 ( 010 or 011 ( 3~4자리 ) ( 4자리 ) [- 는 필요 없음] )";
    public static final String NAME_REGEX_MESSAGE = "이름 형식이 안 맞음 (한글 2~6 자리)";
    public static final String PASSWORD_REGEX_MESSAGE = "비밀번호 조건 확인 ( 영문 대소문자, 숫자, 특수기호(!@#$%^&*만 가능) 1개 이상 (10~20자리)";

}
