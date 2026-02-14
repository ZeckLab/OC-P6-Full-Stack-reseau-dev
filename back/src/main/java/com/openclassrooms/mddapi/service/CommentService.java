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

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final ArticleService articleService;
    private final UserService userService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    public Comment addComment(Long articleId, CreateCommentDTO createCommentDto) {
        User user = userService.getCurrentUser();
        Article article = articleService.getArticleById(articleId);
        log.info("addComment - Adding comment for user '{}' on article id={}", user.getUsername(), articleId);

        Comment comment = commentMapper.toEntity(createCommentDto);
        comment.setArticle(article);
        comment.setAuthor(user);

        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsForArticle(Long articleId) {
        log.info("getCommentsForArticle - Retrieving comments for article id={}", articleId);
        // Ensure article exists
        articleService.getArticleById(articleId);

        return commentRepository.findByArticleIdOrderByCreatedAtAsc(articleId);
    }
}
