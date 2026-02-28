package com.movie4k.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    
    /**
     * 0: 题材分类, 1: 地区分类, 2: 语言分类
     */
    private Integer type;

    private LocalDateTime createdAt;
}
