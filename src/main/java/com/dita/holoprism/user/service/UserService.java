package com.dita.holoprism.user.service;

import com.dita.holoprism.user.dto.RegisterDto;
import com.dita.holoprism.user.dto.ProviderDto;
import com.dita.holoprism.user.dto.UserIdDto;
import com.dita.holoprism.user.entity.UserEntity;
import com.dita.holoprism.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserEntity createUser(RegisterDto dto) {
        if (userRepository.existsById(dto.getId())) {
            System.out.println("이미 존재하는 ID입니다.");
            return null;
        }
        String password = dto.getPassword();
        dto.setPassword(passwordEncoder.encode(password));
        return userRepository.save(dto.toEntity());
    }

    public UserEntity findUserById(UserIdDto dto) {
        return userRepository.findById(dto.getId()).orElse(null);
    }

    public Optional<UserEntity> findUserBySocialType(ProviderDto providerDto) {
        String provider = providerDto.getProvider();
        String id = providerDto.getId();
        return userRepository.findByIdAndProvider(id,provider);
    }

    public void updateVisitedTime(String userId) {
        userRepository.updateVisitedTime(userId);
    }
}
