package com.dita.holoprism.security.auth;

import com.dita.holoprism.user.entity.UserEntity;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private UserEntity user;
    private Map<String, Object> attributes;

    //일반 로그인
    public PrincipalDetails(UserEntity user) {
        this.user = user;
    }

    //OAuth 로그인
    public PrincipalDetails(UserEntity user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

//    ----------------------------------------
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(() -> {return String.valueOf(user.getPermission());});
        return authorities;
    }
//    ------------------------------------------------
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }
}
