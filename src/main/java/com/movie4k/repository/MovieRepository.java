package com.movie4k.repository;

import com.movie4k.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
    List<Movie> findByCategoryId(Integer categoryId);
    List<Movie> findByCountry(String country);
    List<Movie> findByLanguage(String language);
}
