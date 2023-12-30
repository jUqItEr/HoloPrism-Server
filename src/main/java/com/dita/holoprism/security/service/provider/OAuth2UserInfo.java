package com.dita.holoprism.security.service.provider;

public interface OAuth2UserInfo {
    String getSocialId();
    String getSocialType();
    String getEmail();
    String getName();

    String getImage();
}
