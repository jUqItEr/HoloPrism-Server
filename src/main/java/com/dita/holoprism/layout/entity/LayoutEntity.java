package com.dita.holoprism.layout.entity;

import com.dita.holoprism.user.entity.UserEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.util.Map;

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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_tbl_id")
    private UserEntity user;

    @JdbcTypeCode(SqlTypes.JSON)
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> layout;
}
