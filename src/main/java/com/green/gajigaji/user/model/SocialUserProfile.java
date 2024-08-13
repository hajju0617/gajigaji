package com.green.gajigaji.user.model;

import com.green.gajigaji.security.SignInProviderType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialUserProfile {
    private long userSeq;
    private String userEmail;
    private SignInProviderType providerType;
    private String userNickname;
    private String userPic;
    private String userRole;
}
