package com.dita.holoprism.layout.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "layout_tbl")
public class LayoutEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_tbl_id", length = 64)
    private String userId;

    @Column(length = 64)
    private String layout;
}
