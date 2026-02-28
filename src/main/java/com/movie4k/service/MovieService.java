package com.movie4k.service;

import com.movie4k.entity.Movie;
import com.movie4k.entity.MovieResource;
import com.movie4k.repository.MovieRepository;
import com.movie4k.repository.MovieResourceRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieResourceRepository movieResourceRepository;

    /**
     * 影库高级筛选
     */
    public Page<Movie> findMovies(Integer categoryId, String country, String language, String sort, int page, int size) {
        Specification<Movie> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (categoryId != null) predicates.add(cb.equal(root.get("categoryId"), categoryId));
            if (country != null && !country.isEmpty()) predicates.add(cb.equal(root.get("country"), country));
            if (language != null && !language.isEmpty()) predicates.add(cb.equal(root.get("language"), language));
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Sort sortObj = Sort.by(Sort.Direction.DESC, "createdAt");
        if ("rating".equals(sort)) sortObj = Sort.by(Sort.Direction.DESC, "rating");
        if ("year".equals(sort)) sortObj = Sort.by(Sort.Direction.DESC, "releaseYear");

        return movieRepository.findAll(spec, PageRequest.of(page, size, sortObj));
    }

    /**
     * 获取电影详情及资源
     */
    public Map<String, Object> getMovieDetails(Integer movieId) {
        Map<String, Object> details = new HashMap<>();
        Movie movie = movieRepository.findById(movieId).orElse(null);
        if (movie != null) {
            details.put("info", movie);
            details.put("resources", movieResourceRepository.findByMovieId(movieId));
        }
        return details;
    }
    
    /**
     * 首页推荐数据
     */
    public List<Movie> getRecommended() {
        return movieRepository.findAll(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "rating"))).getContent();
    }
}
