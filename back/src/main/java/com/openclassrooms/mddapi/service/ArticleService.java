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

/**
 * Service handling business logic related to articles,
 * including feed retrieval, article creation, and article lookup.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserService userService;
    private final TopicService topicService;
    private final ArticleMapper articleMapper;

    /**
     * Retrieves a personalized feed for the currently authenticated user.
     * Articles are filtered by the user's subscribed topics and sorted by creation date.
     *
     * @param sort the sorting direction ("asc" or "desc")
     * @return a list of Article entities matching the user's feed
     */
    public List<Article> getFeedForCurrentUser(String sort) {
        User user = userService.getCurrentUser();
        log.info("getFeedForCurrentUser - Retrieving articles for user '{}'", user.getUsername());

        Sort sortOrder = "asc".equalsIgnoreCase(sort) ? Sort.by("createdAt").ascending()
                : Sort.by("createdAt").descending();

        return articleRepository.findByTopicIn(user.getSubscriptions(), sortOrder);
    }

    /**
     * Creates a new article authored by the currently authenticated user.
     *
     * @param createArticleDto the payload containing article title, content, and topic ID
     * @return the newly created Article entity
     */
    public Article createArticle(CreateArticleDTO createArticleDto) {
        User user = userService.getCurrentUser();
        log.info("createArticle - Creating article for user '{}'", user.getUsername());

        Topic topic = topicService.getTopicById(createArticleDto.getTopicId());

        Article article = articleMapper.toEntity(createArticleDto);
        article.setAuthor(user);
        article.setTopic(topic);

        return articleRepository.save(article);
    }

    /**
     * Retrieves an article by its ID.
     *
     * @param id the ID of the article to retrieve
     * @return the Article entity
     * @throws NotFoundException if no article is found with the given ID
     */
    public Article getArticleById(Long id) {
        log.info("getArticleById - Retrieving article with id={}", id);
        return articleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.ARTICLE_NOT_FOUND + id));
    }
}
