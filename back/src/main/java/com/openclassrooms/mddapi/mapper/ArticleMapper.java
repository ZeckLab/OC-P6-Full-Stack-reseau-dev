package com.openclassrooms.mddapi.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.openclassrooms.mddapi.domain.Article;
import com.openclassrooms.mddapi.dto.request.CreateArticleDTO;
import com.openclassrooms.mddapi.dto.response.ArticleDTO;
import com.openclassrooms.mddapi.dto.response.ArticleDetailDTO;
import com.openclassrooms.mddapi.dto.response.CommentDTO;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "topic", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "comments", ignore = true)
    Article toEntity(CreateArticleDTO dto);

    @Mapping(target = "topicName", source = "topic.name")
    @Mapping(target = "authorUsername", source = "author.username")
    ArticleDTO toDto(Article article);

    @Mapping(target = "topicName", source = "article.topic.name")
    @Mapping(target = "authorUsername", source = "article.author.username")
    @Mapping(target = "createdAt", expression = "java(article.getCreatedAt().toString())")
    @Mapping(target = "comments", source = "comments")
    ArticleDetailDTO toDetailDto(Article article, List<CommentDTO> comments);

    List<ArticleDTO> toDtoList(List<Article> articles);
}
