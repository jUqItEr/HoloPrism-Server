package com.dita.holoprism.user.dto;

import com.dita.holoprism.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class RegisterDto {
    private String id;
    private String password;
    private String nickname;
    private String email;

    public UserEntity toEntity() {
        return UserEntity.builder()
                .id(id)
                .password(password)
                .nickname(nickname)
                .email(email)
                .build();
    }

    public RegisterDto toDto(UserEntity entity) {
        return RegisterDto.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .nickname(entity.getNickname())
                .build();
    }
}
