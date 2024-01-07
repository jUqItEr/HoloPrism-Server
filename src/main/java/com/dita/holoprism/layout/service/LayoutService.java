package com.dita.holoprism.layout.service;

import com.dita.holoprism.layout.dto.LayoutDto;
import com.dita.holoprism.layout.entity.LayoutEntity;
import com.dita.holoprism.layout.repository.LayoutRepository;
import com.dita.holoprism.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LayoutService {
    private final LayoutRepository layoutRepository;
    private final UserRepository userRepository;

    public LayoutEntity updateLayout(LayoutDto dto) {
        LayoutEntity entity = dto.toEntity(userRepository);
        return layoutRepository.save(entity);
    }
}
