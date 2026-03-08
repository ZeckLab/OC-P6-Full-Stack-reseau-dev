package com.openclassrooms.mddapi.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.domain.Article;
import com.openclassrooms.mddapi.domain.Comment;
import com.openclassrooms.mddapi.domain.User;
import com.openclassrooms.mddapi.dto.request.CreateCommentDTO;
import com.openclassrooms.mddapi.mapper.CommentMapper;
import com.openclassrooms.mddapi.repository.CommentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service responsible for handling business logic related to comments,
 * including creation of new comments and retrieval of comments for a given article.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final ArticleService articleService;
    private final UserService userService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    /**
     * Adds a new comment to the specified article on behalf of the current user.
     *
     * @param articleId the ID of the article to comment on
     * @param createCommentDto the payload containing the comment content
     * @return the newly created Comment entity
     */
    public Comment addComment(Long articleId, CreateCommentDTO createCommentDto) {
        User user = userService.getCurrentUser();
        Article article = articleService.getArticleById(articleId);
        log.info("addComment - Adding comment for user '{}' on article id={}", user.getUsername(), articleId);

        Comment comment = commentMapper.toEntity(createCommentDto);
        comment.setArticle(article);
        comment.setAuthor(user);

        return commentRepository.save(comment);
    }

    /**
     * Retrieves all comments associated with the specified article,
     * ordered by creation date in ascending order.
     *
     * @param articleId the ID of the article whose comments should be retrieved
     * @return a list of Comment entities for the given article
     */
    public List<Comment> getCommentsForArticle(Long articleId) {
        log.info("getCommentsForArticle - Retrieving comments for article id={}", articleId);
        // Ensure article exists
        articleService.getArticleById(articleId);

        return commentRepository.findByArticleIdOrderByCreatedAtAsc(articleId);
    }
}
