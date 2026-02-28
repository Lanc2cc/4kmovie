package com.movie4k;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Movie4kApplication {
    public static void main(String[] args) {
        SpringApplication.run(Movie4kApplication.class, args);
    }
}
