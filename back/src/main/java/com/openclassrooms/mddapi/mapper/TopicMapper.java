package com.openclassrooms.mddapi.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.openclassrooms.mddapi.domain.Topic;
import com.openclassrooms.mddapi.domain.User;
import com.openclassrooms.mddapi.dto.response.TopicDTO;
import com.openclassrooms.mddapi.dto.response.TopicWithSubscriptionDto;

@Mapper(componentModel = "spring")
public interface TopicMapper {

    TopicDTO toDto(Topic topic);

    List<TopicDTO> toDtoList(List<Topic> topics);

    // Explicit mapping for Topic -> TopicWithSubscriptionDto.
    // MapStruct cannot infer this method for list mappings because it has multiple
    // parameters.
    // We name it so that the list mapping can reference it explicitly.
    @Named("topicWithSubscription")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "topic.id")
    @Mapping(target = "name", source = "topic.name")
    @Mapping(target = "description", source = "topic.description")
    @Mapping(target = "subscribed", expression = "java(topic.getSubscribers().stream().anyMatch(u -> u.getId().equals(user.getId())))")
    TopicWithSubscriptionDto toTopicWithSubscriptionDto(Topic topic, @Context User user);

    // Force MapStruct to use the method above for each element.
    // Without this, MapStruct tries implicit mapping and fails on 'subscribed'.
    @IterableMapping(qualifiedByName = "topicWithSubscription")
    List<TopicWithSubscriptionDto> toTopicWithSubscriptionDtoList(List<Topic> topics, @Context User user);
}
