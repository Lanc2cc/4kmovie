package com.movie4k.controller;

import com.movie4k.entity.Category;
import com.movie4k.entity.Movie;
import com.movie4k.repository.CategoryRepository;
import com.movie4k.service.HistoryService;
import com.movie4k.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin // 支持跨域调用
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * 1. 首页：获取推荐/轮播 (按高分排序)
     */
    @GetMapping("/recommended")
    public List<Movie> getRecommended() {
        return movieService.getRecommended();
    }

    /**
     * 2. 首页/影库：获取所有分类
     */
    @GetMapping("/categories")
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    /**
     * 3. 影库：多条件筛选
     */
    @GetMapping("/search")
    public Page<Movie> searchMovies(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String language,
            @RequestParam(defaultValue = "new") String sort,
            @RequestParam(defaultValue = "0") int page) {
        return movieService.findMovies(categoryId, country, language, sort, page, 20);
    }

    /**
     * 4. 详情页：获取基本信息 + 播放资源
     */
    @GetMapping("/{id}")
    public Map<String, Object> getDetails(@PathVariable Integer id) {
        return movieService.getMovieDetails(id);
    }

    /**
     * 5. 历史记录：保存进度
     */
    @PostMapping("/history")
    public String saveHistory(@RequestParam Integer movieId, @RequestParam Integer seconds) {
        historyService.saveProgress(movieId, seconds);
        return "success";
    }

    /**
     * 6. 历史记录：获取列表
     */
    @GetMapping("/history")
    public List<Map<String, Object>> getHistory() {
        return historyService.getHistoryList();
    }
}
