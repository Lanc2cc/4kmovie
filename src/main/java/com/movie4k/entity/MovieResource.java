package com.movie4k.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;

@Data
@Entity
@Table(name = "movie_resource")
public class MovieResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer movieId;
    private String sourceName; // 如：4K线路1
    
    @Column(columnDefinition = "TEXT")
    private String playUrl;
    
    private String quality; // 4K, HD等

    @CreatedDate
    private LocalDateTime createdAt;
}
