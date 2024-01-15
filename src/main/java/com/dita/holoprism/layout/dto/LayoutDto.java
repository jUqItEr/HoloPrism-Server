package com.dita.holoprism.layout.dto;

import com.dita.holoprism.layout.entity.LayoutEntity;
import com.dita.holoprism.user.entity.UserEntity;
import com.dita.holoprism.user.repository.UserRepository;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.ManyToAny;

import javax.sound.midi.Patch;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class LayoutDto {
    private long id;
    private Map<String, Object> layout;
    private String userId;

    public LayoutEntity toEntity(UserRepository userRepository) {
        UserEntity user = null;
        // 여기 부분 내일 access토큰 검증해서 수정해야함.
        // access토큰 검증과정을 해줘야함.
        if (this.userId != null) {
            user = userRepository.findById(this.userId).orElse(null);
        }

        return LayoutEntity.builder()
                .id(id)
                .layout(layout)
                .user(user)
                .build();
    }

    public LayoutDto toDto(LayoutEntity entity) {
        return LayoutDto.builder()
                .id(entity.getId())
                .layout(entity.getLayout())
                .userId(entity.getUser().getId())
                .build();
    }
}
