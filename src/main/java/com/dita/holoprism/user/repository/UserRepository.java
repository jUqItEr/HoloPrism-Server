package com.dita.holoprism.user.repository;

import com.dita.holoprism.user.dto.ProviderDto;
import com.dita.holoprism.user.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByIdAndProvider(String id, String provider);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user_tbl SET visited_at = now() WHERE id = :userId", nativeQuery = true)
    void updateVisitedTime(@Param("userId") String userId);
}
