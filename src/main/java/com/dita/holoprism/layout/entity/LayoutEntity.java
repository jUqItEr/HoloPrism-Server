package com.dita.holoprism.layout.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class LayoutEntity {
    @Id
    private long id;

    @Column(name = "user_tbl_id", length = 64)
    private String userId;

    @Column(length = 64)
    private String layout;
}
