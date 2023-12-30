package com.dita.holoprism.user.controller;

import com.dita.holoprism.user.dto.RegisterDto;
import com.dita.holoprism.user.dto.ProviderDto;
import com.dita.holoprism.user.dto.UserIdDto;
import com.dita.holoprism.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Validated @RequestBody RegisterDto dto) {
        return ResponseEntity.ok(userService.createUser(dto));
    }

    @GetMapping("/find")
    public ResponseEntity<?> findUserById(UserIdDto dto) {
        return ResponseEntity.ok(userService.findUserById(dto));
    }

    @GetMapping("/find/socialType")
    public ResponseEntity<?> findUserBySocialType(ProviderDto providerDto) {
        return ResponseEntity.ok(userService.findUserBySocialType(providerDto));
    }
}
