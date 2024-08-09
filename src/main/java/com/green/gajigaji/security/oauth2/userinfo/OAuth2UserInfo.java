package com.green.gajigaji.security.oauth2.userinfo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public abstract class OAuth2UserInfo {

    protected final Map<String, Object> attributes;


    public abstract String getId();
    public abstract String getName();
    public abstract String getEmail();
    public abstract String getProfilePicUrl();
}
