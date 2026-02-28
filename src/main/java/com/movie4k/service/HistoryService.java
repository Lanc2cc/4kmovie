package com.movie4k.service;

import com.movie4k.entity.Movie;
import com.movie4k.entity.WatchHistory;
import com.movie4k.repository.MovieRepository;
import com.movie4k.repository.WatchHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HistoryService {

    @Autowired
    private WatchHistoryRepository historyRepository;

    @Autowired
    private MovieRepository movieRepository;

    /**
     * 保存或更新进度
     */
    public void saveProgress(Integer movieId, Integer seconds) {
        WatchHistory history = historyRepository.findByMovieId(movieId)
                .orElse(new WatchHistory());
        history.setMovieId(movieId);
        history.setProgressSeconds(seconds);
        historyRepository.save(history);
    }

    /**
     * 获取历史记录列表 (附带电影信息)
     */
    public List<Map<String, Object>> getHistoryList() {
        List<WatchHistory> histories = historyRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (WatchHistory h : histories) {
            movieRepository.findById(h.getMovieId()).ifPresent(movie -> {
                Map<String, Object> map = new HashMap<>();
                map.put("movie", movie);
                map.put("progress", h.getProgressSeconds());
                map.put("lastWatchedAt", h.getLastWatchedAt());
                result.add(map);
            });
        }
        // 按时间倒序
        result.sort((a, b) -> ((java.time.LocalDateTime)b.get("lastWatchedAt"))
                .compareTo((java.time.LocalDateTime)a.get("lastWatchedAt")));
        return result;
    }
}
