package com.openclassrooms.mddapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import com.openclassrooms.mddapi.domain.Article;
import com.openclassrooms.mddapi.dto.request.CreateArticleDTO;
import com.openclassrooms.mddapi.dto.response.ArticleDetailDTO;
import com.openclassrooms.mddapi.dto.response.CommentDTO;
import com.openclassrooms.mddapi.dto.response.ArticleDTO;
import com.openclassrooms.mddapi.mapper.ArticleMapper;
import com.openclassrooms.mddapi.mapper.CommentMapper;
import com.openclassrooms.mddapi.service.ArticleService;

import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleMapper articleMapper;
    private final CommentMapper commentMapper;

    @GetMapping("/feed")
    public ResponseEntity<List<ArticleDTO>> getFeed(@RequestParam(defaultValue = "desc") String sort) {
        log.info("GET /feed - Retrieving feed for current user");

        List<Article> articles = articleService.getFeedForCurrentUser(sort);
        log.info("GET /feed - Feed retrieved for current user, number of articles: {}", articles.size());

        return ResponseEntity.ok(articleMapper.toDtoList(articles));
    }

    @PostMapping("/articles")
    public ResponseEntity<ArticleDTO> createArticle(@Valid @RequestBody CreateArticleDTO createArticleDto) {
        log.info("POST /articles - Creating article for current user");

        Article article = articleService.createArticle(createArticleDto);

        log.info("POST /articles - Article created successfully with id={}", article.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(articleMapper.toDto(article));
    }

    @GetMapping("/articles/{id}")
    public ResponseEntity<ArticleDetailDTO> getArticle(@PathVariable Long id) {
        log.info("GET /articles/{} - Retrieving article", id);

        Article article = articleService.getArticleById(id);
        log.info("GET /articles/{} - Article retrieved successfully", id);

        List<CommentDTO> commentDTOs = commentMapper.toDtoList(article.getComments());

        return ResponseEntity.ok(articleMapper.toDetailDto(article, commentDTOs));
    }
}
