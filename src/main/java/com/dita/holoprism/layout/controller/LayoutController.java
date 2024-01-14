package com.dita.holoprism.layout.controller;

import com.dita.holoprism.layout.dto.LayoutDto;
import com.dita.holoprism.layout.service.LayoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/layout")
public class LayoutController {
    private final LayoutService layoutService;

    @PostMapping(value = "/update")
    public ResponseEntity<?> updateLayout(@Validated @RequestBody LayoutDto dto) {
        return ResponseEntity.ok(layoutService.updateLayout(dto));
    }
}