package com.movie4k.repository;

import com.movie4k.entity.WatchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WatchHistoryRepository extends JpaRepository<WatchHistory, Integer> {
    Optional<WatchHistory> findByMovieId(Integer movieId);
}
