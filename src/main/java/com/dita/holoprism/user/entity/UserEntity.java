package com.dita.holoprism.user.entity;

import com.dita.holoprism.layout.entity.LayoutEntity;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "role")
    private int permission;

    @OneToOne(mappedBy = "user")
    private LayoutEntity layout;
}
