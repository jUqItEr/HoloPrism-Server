package com.dita.holoprism.user.dto;

import com.dita.holoprism.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderDto {

    private String id;
    private String provider;

    public UserEntity toEntity() {
        return UserEntity.builder()
                .id(id)
                .provider(provider)
                .build();
    }

    public ProviderDto toDto(UserEntity entity) {
        return ProviderDto.builder()
                .id(entity.getId())
                .provider(entity.getProvider())
                .build();
    }
}
