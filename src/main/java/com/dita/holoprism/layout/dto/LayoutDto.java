package com.dita.holoprism.layout.dto;

import com.dita.holoprism.layout.entity.LayoutEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class LayoutDto {
    private long id;
    private String layout;

    public LayoutEntity toEntity() {
        return LayoutEntity.builder()
                .id(id)
                .layout(layout)
                .build();
    }

    public LayoutDto toDto(LayoutEntity entity) {
        return LayoutDto.builder()
                .id(entity.getId())
                .layout(entity.getLayout())
                .build();
    }
}
