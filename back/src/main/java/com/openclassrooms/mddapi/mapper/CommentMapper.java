package com.openclassrooms.mddapi.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.openclassrooms.mddapi.domain.Comment;
import com.openclassrooms.mddapi.dto.request.CreateCommentDTO;
import com.openclassrooms.mddapi.dto.response.CommentDTO;

/**
 * Mapper responsible for converting between Comment entities and their DTO representations.
 * Uses MapStruct for automatic field mapping.
 */
@Mapper(componentModel = "spring")
public interface CommentMapper {

    /**
     * Converts a CreateCommentDTO into a Comment entity.
     * Some fields are intentionally ignored and set later (author, article, timestamps).
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "article", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Comment toEntity(CreateCommentDTO dto);

    /**
     * Converts a Comment entity into a CommentDTO.
     */
    @Mapping(target = "authorUsername", source = "author.username")
    @Mapping(target = "createdAt", expression = "java(comment.getCreatedAt().toString())")
    CommentDTO toDto(Comment comment);

    /**
     * Converts a list of Comment entities into a list of CommentDTOs.
     */
    List<CommentDTO> toDtoList(List<Comment> comments);
}

