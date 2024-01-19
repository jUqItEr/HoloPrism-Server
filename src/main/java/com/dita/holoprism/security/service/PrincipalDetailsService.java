package com.dita.holoprism.security.service;

import com.dita.holoprism.user.dto.UserIdDto;
import com.dita.holoprism.user.entity.UserEntity;
import com.dita.holoprism.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService  implements UserDetailsService {


    private final UserService userService;
    @Override
    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userService.findUserById(UserIdDto.builder().id(username).build());
        if (user == null) {
            return null;
        }else {
            return user;
        }
    }
}