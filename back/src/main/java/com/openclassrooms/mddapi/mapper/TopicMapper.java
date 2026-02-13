package com.openclassrooms.mddapi.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.openclassrooms.mddapi.domain.Topic;
import com.openclassrooms.mddapi.dto.response.TopicDTO;

@Mapper(componentModel = "spring")
public interface TopicMapper {
    TopicDTO toDto(Topic topic);

    List<TopicDTO> toDtoList(List<Topic> topics);
}

