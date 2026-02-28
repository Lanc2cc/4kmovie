package com.movie4k.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@Table(name = "watch_history")
@EntityListeners(AuditingEntityListener.class)
public class WatchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private Integer movieId;
    
    private Integer progressSeconds;

    @LastModifiedDate
    private LocalDateTime lastWatchedAt;
}
