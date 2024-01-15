package com.dita.holoprism.user.entity;

import com.dita.holoprism.layout.entity.LayoutEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "user_tbl")
public class UserEntity {
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
}
