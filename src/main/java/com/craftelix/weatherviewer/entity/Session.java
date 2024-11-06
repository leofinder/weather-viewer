package com.craftelix.weatherviewer.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("sessions")
public class Session implements Persistable<UUID> {

    @Id
    private UUID id;

    @Column("user_id")
    private Long userId;

    private LocalDateTime expiresAt;

    @Override
    public boolean isNew() {
        return true;
    }
}
