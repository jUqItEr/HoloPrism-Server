package com.dita.holoprism.user.service;

import com.dita.holoprism.user.dto.RegisterDto;
import com.dita.holoprism.user.entity.UserEntity;
import com.dita.holoprism.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserEntity createUser(RegisterDto dto) {
        return userRepository.save(dto.toEntity());
    }
}
