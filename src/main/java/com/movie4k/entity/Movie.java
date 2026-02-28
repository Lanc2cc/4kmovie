package com.movie4k.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@Table(name = "movie")
@EntityListeners(AuditingEntityListener.class)
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private Integer tmdbId; // TMDB 的唯一标识

    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private String coverUrl;
    private Integer releaseYear;
    private String language;
    private String country;
    private Double rating;
    private String tags;
    private Integer categoryId;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
