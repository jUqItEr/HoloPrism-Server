package com.dita.holoprism.user.service;

import com.dita.holoprism.user.dto.RegisterDto;
import com.dita.holoprism.user.entity.UserEntity;
import com.dita.holoprism.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserEntity createUser(RegisterDto dto) {
        String password = dto.getPassword();
        dto.setPassword(passwordEncoder.encode(password));
        return userRepository.save(dto.toEntity());
    }
}
