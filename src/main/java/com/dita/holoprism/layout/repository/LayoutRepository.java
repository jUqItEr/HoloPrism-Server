package com.dita.holoprism.layout.repository;

import com.dita.holoprism.layout.entity.LayoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LayoutRepository extends JpaRepository<LayoutEntity, Long> {
    List<LayoutEntity> findByUserId(String userId);
    Optional<LayoutEntity> findById(Long id);
}
