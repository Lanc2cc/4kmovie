package com.movie4k.repository;

import com.movie4k.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Integer>, JpaSpecificationExecutor<Movie> {
    List<Movie> findByCategoryId(Integer categoryId);
    List<Movie> findByCountry(String country);
    List<Movie> findByLanguage(String language);
}
