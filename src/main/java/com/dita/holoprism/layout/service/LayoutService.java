package com.dita.holoprism.layout.service;

import com.dita.holoprism.layout.dto.LayoutDto;
import com.dita.holoprism.layout.entity.LayoutEntity;
import com.dita.holoprism.layout.repository.LayoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LayoutService {
    private final LayoutRepository layoutRepository;

    public LayoutEntity updateLayout(LayoutDto dto) {
        return layoutRepository.save(dto.toEntity());
    }
}
