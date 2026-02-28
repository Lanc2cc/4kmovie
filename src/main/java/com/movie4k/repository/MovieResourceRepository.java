package com.movie4k.repository;

import com.movie4k.entity.MovieResource;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MovieResourceRepository extends JpaRepository<MovieResource, Integer> {
    List<MovieResource> findByMovieId(Integer movieId);
}
