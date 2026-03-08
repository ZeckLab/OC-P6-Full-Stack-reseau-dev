package com.openclassrooms.mddapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.domain.Comment;
import com.openclassrooms.mddapi.dto.request.CreateCommentDTO;
import com.openclassrooms.mddapi.dto.response.CommentDTO;
import com.openclassrooms.mddapi.mapper.CommentMapper;
import com.openclassrooms.mddapi.service.CommentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * REST controller responsible for managing comments on articles.
 * Provides endpoints for creating comments and retrieving all comments for a given article.
 */
@Slf4j
@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    /**
     * Adds a new comment to the specified article.
     *
     * @param id  the ID of the article to which the comment will be added
     * @param dto the payload containing the comment's content
     * @return CommentDTO representing the newly created comment, with HTTP 201 status
     */
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDTO> addComment(@PathVariable Long id, @Valid @RequestBody CreateCommentDTO dto) {
        log.info("POST /articles/{}/comments - Adding comment", id);

        Comment comment = commentService.addComment(id, dto);
        log.info("POST /articles/{}/comments - Comment created successfully with id={}", id, comment.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(commentMapper.toDto(comment));
    }

    /**
     * Retrieves all comments associated with the specified article.
     *
     * @param id the ID of the article whose comments should be retrieved
     * @return a list of CommentDTO objects representing the article's comments
     */
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long id) {
        log.info("GET /articles/{}/comments - Retrieving comments", id);

        List<Comment> comments = commentService.getCommentsForArticle(id);
        log.info("GET /articles/{}/comments - {} comments retrieved", id, comments.size());

        return ResponseEntity.ok(commentMapper.toDtoList(comments));
    }
}
