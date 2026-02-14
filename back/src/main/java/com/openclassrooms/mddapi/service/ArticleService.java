package com.openclassrooms.mddapi.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.constants.ErrorMessages;
import com.openclassrooms.mddapi.domain.Article;
import com.openclassrooms.mddapi.domain.Topic;
import com.openclassrooms.mddapi.domain.User;
import com.openclassrooms.mddapi.dto.request.CreateArticleDTO;
import com.openclassrooms.mddapi.exception.NotFoundException;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.mapper.ArticleMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserService userService;
    private final TopicService topicService;
    private final ArticleMapper articleMapper;

    public List<Article> getFeedForCurrentUser(String sort) {
        User user = userService.getCurrentUser();
        log.info("getFeedForCurrentUser - Retrieving articles for user '{}'", user.getUsername());

        Sort sortOrder = "asc".equalsIgnoreCase(sort) ? Sort.by("createdAt").ascending()
                : Sort.by("createdAt").descending();

        return articleRepository.findByTopicIn(user.getSubscriptions(), sortOrder);
    }

    public Article createArticle(CreateArticleDTO createArticleDto) {
        User user = userService.getCurrentUser();
        log.info("createArticle - Creating article for user '{}'", user.getUsername());

        Topic topic = topicService.getTopicById(createArticleDto.getTopicId());

        Article article = articleMapper.toEntity(createArticleDto);
        article.setAuthor(user);
        article.setTopic(topic);

        return articleRepository.save(article);
    }

    public Article getArticleById(Long id) {
        log.info("getArticleById - Retrieving article with id={}", id);
        return articleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.ARTICLE_NOT_FOUND + id));
    }
}
