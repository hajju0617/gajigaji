package com.green.gajigaji.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import static com.green.gajigaji.user.usercommon.UserMessage.NICKNAME_REGEX_MESSAGE;
import static com.green.gajigaji.user.usercommon.UserMessage.PASSWORD_REGEX_MESSAGE;


@Setter
@Getter
public class UpdateUserInfoReq {
    @Schema(example = "닉네임123", description = "유저 닉네임")
    @Pattern(regexp = "^[0-9a-zA-Z가-힣]{4,10}$", message = NICKNAME_REGEX_MESSAGE)
    private String userNickname;

    @Schema(example = "대구 중구 중앙로", description = "유저 주소")
    private String userAddr;

    @Schema(example = "운동, 독서, 게임", description = "유저 관심모임")
    private String userFav;

    @Schema(example = "01012345678", description = "유저 전화번호")
    @Pattern(regexp = "^01[01](?:\\d{3}|\\d{4})\\d{4}$", message = PASSWORD_REGEX_MESSAGE)
    private String userPhone;

    @Schema(example = "안녕하세요 가지가지 나뭇가지", description = "유저 자기소개")
    private String userIntro;

    @JsonIgnore
    private long userSeq;
}
