package com.dita.holoprism.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "user_tbl")
public class UserEntity {
    @Id
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
}
