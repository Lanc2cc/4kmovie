package com.movie4k.controller;

import com.movie4k.service.TmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private TmdbService tmdbService;

    /**
     * 模拟同步：离线状态下向数据库注入测试数据
     * 访问地址: http://localhost:8080/api/admin/mock
     */
    @GetMapping("/mock")
    public String mockData() {
        try {
            tmdbService.addMockMovies();
            return "模拟同步成功！已注入 10 部精美电影数据，现在去首页看看吧。";
        } catch (Exception e) {
            return "模拟同步失败: " + e.getMessage();
        }
    }

    /**
     * 手动触发同步热门电影
     * 访问地址: http://localhost:8080/api/admin/sync?page=1
     */
    @GetMapping("/sync")
    public String syncMovies(@RequestParam(defaultValue = "1") int page) {
        try {
            tmdbService.syncPopularMovies(page);
            return "同步成功！已抓取第 " + page + " 页热门电影数据。";
        } catch (Exception e) {
            return "同步失败: " + e.getMessage();
        }
    }
}
