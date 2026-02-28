package com.movie4k.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.movie4k.entity.Movie;
import com.movie4k.entity.MovieResource;
import com.movie4k.repository.MovieRepository;
import com.movie4k.repository.MovieResourceRepository;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
public class TmdbService {

    private static final String API_KEY = "be6c4b6fbcfe9749a5100ba98fb559e5";
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieResourceRepository movieResourceRepository;

    private final OkHttpClient client = new OkHttpClient();

    /**
     * 同步热门电影到本地
     */
    @Transactional
    public void syncPopularMovies(int page) throws IOException {
        String url = String.format("%s/movie/popular?api_key=%s&language=zh-CN&page=%d", BASE_URL, API_KEY, page);
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            String responseData = response.body().string();
            JSONObject json = JSON.parseObject(responseData);
            JSONArray results = json.getJSONArray("results");

            for (int i = 0; i < results.size(); i++) {
                JSONObject item = results.getJSONObject(i);
                saveOrUpdateMovie(item);
            }
        }
    }

    private void saveOrUpdateMovie(JSONObject item) {
        Integer tmdbId = item.getInteger("id");
        
        // 1. 保存电影基础信息
        Movie movie = movieRepository.findAll().stream()
                .filter(m -> tmdbId.equals(m.getTmdbId()))
                .findFirst()
                .orElse(new Movie());

        movie.setTmdbId(tmdbId);
        movie.setTitle(item.getString("title"));
        movie.setDescription(item.getString("overview"));
        movie.setCoverUrl(IMAGE_BASE_URL + item.getString("poster_path"));
        movie.setReleaseYear(parseYear(item.getString("release_date")));
        movie.setLanguage(item.getString("original_language"));
        movie.setRating(item.getDouble("vote_average"));
        
        Movie savedMovie = movieRepository.save(movie);

        // 2. 自动生成 4K 播放资源 (基于公用解析接口)
        // 注意：这里使用了常见的第三方嵌入播放地址，仅供本地测试学习使用
        generateResources(savedMovie);
    }

    private void generateResources(Movie movie) {
        // 如果已经有资源了，就不再重复创建
        if (movieResourceRepository.findByMovieId(movie.getId()).isEmpty()) {
            MovieResource res1 = new MovieResource();
            res1.setMovieId(movie.getId());
            res1.setSourceName("4K 全球源 (线路1)");
            res1.setPlayUrl("https://vidsrc.to/embed/movie/" + movie.getTmdbId());
            res1.setQuality("4K");
            movieResourceRepository.save(res1);

            MovieResource res2 = new MovieResource();
            res2.setMovieId(movie.getId());
            res2.setSourceName("极速海外源 (线路2)");
            res2.setPlayUrl("https://vidsrc.me/embed/movie/" + movie.getTmdbId());
            res2.setQuality("1080P");
            movieResourceRepository.save(res2);
        }
    }

    private Integer parseYear(String date) {
        if (date != null && date.length() >= 4) {
            return Integer.parseInt(date.substring(0, 4));
        }
        return 0;
    }
}
