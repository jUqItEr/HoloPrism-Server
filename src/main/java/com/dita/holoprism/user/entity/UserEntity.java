package com.dita.holoprism.user.entity;

import com.dita.holoprism.layout.entity.LayoutEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "user_tbl")
public class UserEntity implements UserDetails, OAuth2User {

    @Transient
    private final Map<String, Object> attributes;

    @Id
    @Column(length = 64)
    private String id;

    @Column(length = 160)
    private String password;

    @Column(length = 64)
    private String nickname;

    private String email;

    @Column(name = "social_type", length = 64)
    private String provider;

    @Column(name = "profile_image")
    private String image;

    @Column(name = "created_at", length = 64)
    private String createdTime;

    @Column(name = "visited_at", length = 64)
    private String visitedTime;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "role")
    private int permission;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<LayoutEntity> layout;

    public UserEntity() {
        this.attributes = new HashMap<>();
    }

    // UserDetails and OAuth2User methods
    @Override
    public String getName() {
        return nickname;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        switch (this.permission) {
            case 0:
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                break;
                // DB role : 0
            case 1:
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                break;
                // DB role : 1
        }

        return authorities;
    }

    @Override
    public String getUsername() {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
