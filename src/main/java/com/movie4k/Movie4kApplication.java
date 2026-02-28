package com.movie4k;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class Movie4kApplication {
    public static void main(String[] args) {
        SpringApplication.run(Movie4kApplication.class, args);
    }
}
