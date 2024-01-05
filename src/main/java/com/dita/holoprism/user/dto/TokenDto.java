package com.dita.holoprism.user.dto;

import com.dita.holoprism.user.entity.UserEntity;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class TokenDto {

    private String accessToken;
    private String refreshToken;

    public TokenDto toDto(String accessToken, String refreshToken) {
        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
