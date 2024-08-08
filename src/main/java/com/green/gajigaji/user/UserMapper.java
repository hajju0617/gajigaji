package com.green.gajigaji.user;


import com.green.gajigaji.user.model.*;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    int postSignUp(SignUpReq p);

    SimpleInfo getSimpleUserInfo(String userEmail);

    UserDetails getDetailUserInfo(long userSeq);
    String getUserPw(long userSeq);

    int patchPassword(UpdatePasswordReq p);

    int deleteUser(long userSeq);
    int userExists(long userSeq);

    int duplicatedCheckNickname(String userNickname);
    int duplicatedCheckNumber(String userPhone);
    int checkEmail(String userEmail);

    void updateUserPic(UpdateUserPicReq p);

    int updateUserInfo(UpdateUserInfoReq p);

    String findUserId(FindUserReq p);

    void checkAuthNum(String email);
    int setPassword(FindPasswordReq p);
    int emailExists(String userEmail);

}

