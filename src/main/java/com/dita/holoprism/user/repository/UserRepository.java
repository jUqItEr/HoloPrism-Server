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

    @Query(value = "select count(*) from user_tbl where id = :id and refresh_token is not null", nativeQuery = true)
    int findUserRefreshToken(@Param("id") String id);

    @Query(value = "select refresh_token from user_tbl where id = :id", nativeQuery = true)
    String getUserRefreshToken(@Param("id") String id);

    @Modifying
    @Transactional
    @Query(value = "update user_tbl set visited_at = now() where id = :userId", nativeQuery = true)
    void updateVisitedTime(@Param("userId") String userId);

    @Modifying
    @Transactional
    @Query(value = "update user_tbl set access_token = :accessToken, refresh_token = :refreshToken where id = :id", nativeQuery = true)
    void updateToken(@Param("id") String id, @Param("accessToken") String accessToken, @Param("refreshToken") String refreshToken);

    @Modifying
    @Transactional
    @Query(value = "update user_tbl set access_token = :accessToken where id = :id", nativeQuery = true)
    void updateAccessToken(@Param("id") String id, @Param("accessToken") String accessToken);
}