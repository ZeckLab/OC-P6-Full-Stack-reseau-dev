package com.openclassrooms.mddapi.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.openclassrooms.mddapi.domain.Comment;
import com.openclassrooms.mddapi.dto.request.CreateCommentDTO;
import com.openclassrooms.mddapi.dto.response.CommentDTO;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "article", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Comment toEntity(CreateCommentDTO dto);

    @Mapping(target = "authorUsername", source = "author.username")
    @Mapping(target = "createdAt", expression = "java(comment.getCreatedAt().toString())")
    CommentDTO toDto(Comment comment);

    List<CommentDTO> toDtoList(List<Comment> comments);
}

