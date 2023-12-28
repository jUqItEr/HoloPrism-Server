package com.dita.holoprism.user.controller;

import com.dita.holoprism.user.dto.RegisterDto;
import com.dita.holoprism.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Validated @RequestBody RegisterDto dto) {
        return ResponseEntity.ok(userService.createUser(dto));
    }
}
