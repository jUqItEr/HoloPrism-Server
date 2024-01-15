package com.dita.holoprism.layout.controller;

import com.dita.holoprism.layout.dto.LayoutDto;
import com.dita.holoprism.layout.entity.LayoutEntity;
import com.dita.holoprism.layout.service.LayoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/layout")
public class LayoutController {
    private final LayoutService layoutService;


    //추가
    @PostMapping(value = "/update")
    public ResponseEntity<?> updateLayout(@Validated @RequestBody LayoutDto dto) {
        return ResponseEntity.ok(layoutService.updateLayout(dto));
    }

    // 삭제
    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deleteLayout2(@RequestParam("id") Long id) {
        boolean isDeleted = layoutService.deleteLayout(id);

        if (isDeleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 삭제2 위에랑 기능 같음.
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteLayout(@PathVariable Long id) {
        boolean isDeleted = layoutService.deleteLayout(id);

        if (isDeleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 유저의 레이아웃 전체 검색
    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<List<LayoutEntity>> getLayoutsByUserId(@PathVariable String userId) {
        List<LayoutEntity> layouts = layoutService.getLayoutsByUserId(userId);
        return ResponseEntity.ok(layouts);
    }

    // 레이아웃 id를 통한 단일검색
    @GetMapping(value = "/{layoutId}")
    public ResponseEntity<LayoutEntity> getLayoutById(@PathVariable Long layoutId) {
        LayoutEntity layout = layoutService.getLayoutById(layoutId);
        return layout != null ? ResponseEntity.ok(layout) : ResponseEntity.notFound().build();
    }
}