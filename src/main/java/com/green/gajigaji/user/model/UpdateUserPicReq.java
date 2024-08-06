package com.green.gajigaji.user.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class UpdateUserPicReq {
    @JsonIgnore
    @Schema(example = "1", description = "유저 PK 값")
    private long userSeq;

    @Schema(example = "asdfqwer123.jpg", description = "업데이트 할 유저 프로필 사진")
    private MultipartFile pic;

    @JsonIgnore
    private String picName;
}
