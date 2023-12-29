package com.dita.holoprism.user.dto;

import com.dita.holoprism.user.entity.UserEntity;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UserIdDto {

    private String id;

    public UserEntity toEntity() {
        return UserEntity.builder()
                .id(id)
                .build();
    }

    public UserIdDto toDto(UserEntity entity) {
        return UserIdDto.builder()
                .id(entity.getId())
                .build();
    }
}
