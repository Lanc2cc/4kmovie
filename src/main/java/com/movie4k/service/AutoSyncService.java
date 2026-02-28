package com.movie4k.service;

import com.movie4k.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AutoSyncService implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AutoSyncService.class);

    @Autowired
    private TmdbService tmdbService;

    @Autowired
    private MovieRepository movieRepository;

    /**
     * 应用启动后执行
     * 如果数据库为空，则进行初始同步
     */
    @Override
    public void run(String... args) {
        if (movieRepository.count() == 0) {
            logger.info("检测到影库为空，开始初始化同步数据...");
            performSync(3); // 初始同步前 3 页数据
        } else {
            logger.info("影库已有数据，无需初始化同步。");
        }
    }

    /**
     * 每 12 小时执行一次定时任务 (43200000 毫秒)
     */
    @Scheduled(fixedRate = 43200000, initialDelay = 3600000)
    public void scheduledSync() {
        logger.info("开始执行定时同步任务...");
        performSync(1); // 每次定时同步第 1 页
    }

    private void performSync(int pages) {
        for (int i = 1; i <= pages; i++) {
            try {
                logger.info("正在同步第 {} 页电影数据...", i);
                tmdbService.syncPopularMovies(i);
            } catch (IOException e) {
                logger.error("同步第 {} 页数据失败: {}", i, e.getMessage());
            }
        }
        logger.info("数据同步完成。");
    }
}
