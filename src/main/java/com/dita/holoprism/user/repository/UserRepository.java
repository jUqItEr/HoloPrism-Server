package com.dita.holoprism.user.repository;

import com.dita.holoprism.user.dto.ProviderDto;
import com.dita.holoprism.user.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByIdAndProvider(String id, String provider);

    @Query(value = "SELECT COUNT(*) FROM user_tbl WHERE id = :id AND refresh_token IS NOT NULL", nativeQuery = true)
    int findUserRefreshToken(@Param("id") String id);

    @Query(value = "SELECT refresh_token FROM user_tbl WHERE id = :id", nativeQuery = true)
    String getUserRefreshToken(@Param("id") String id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user_tbl SET visited_at = now() WHERE id = :userId", nativeQuery = true)
    void updateVisitedTime(@Param("userId") String userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user_tbl SET access_token = :accessToken, refresh_token = :refreshToken WHERE id = :id", nativeQuery = true)
    void updateToken(@Param("id") String id, @Param("accessToken") String accessToken, @Param("refreshToken") String refreshToken);


}