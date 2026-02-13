package com.openclassrooms.mddapi.service;

import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.domain.User;
import com.openclassrooms.mddapi.dto.response.TopicWithSubscriptionDto;
import com.openclassrooms.mddapi.exception.NotFoundException;
import com.openclassrooms.mddapi.constants.ErrorMessages;
import com.openclassrooms.mddapi.domain.Topic;
import com.openclassrooms.mddapi.repository.TopicRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final UserService userService;

    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    public Topic getTopicById(Long id) {
        log.info("getTopicById - Retrieving topic with ID: {}", id);
        return topicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.TOPIC_NOT_FOUND + " " + id));
    }

    public boolean subscribe(Long id, String username) {
        User user = userService.getCurrentUser(username);
        Topic topic = getTopicById(id);

        if (topic.getSubscribers().contains(user)) {
            log.info("subscribe - User '{}' is already subscribed to topic '{}'", username, topic.getName());
            return false; // User is already subscribed, nothing to do
        }

        boolean added = topic.getSubscribers().add(user); // Set - no need to check for duplicates
        topicRepository.save(topic);
        log.info("subscribe - User '{}' subscribed to topic '{}'", username, topic.getName());
        return added;

    }

    public boolean unsubscribe(Long id, String name) {
        User user = userService.getCurrentUser(name);
        Topic topic = getTopicById(id);

        if (!topic.getSubscribers().contains(user)) {
            log.info("unsubscribe - User '{}' is not subscribed to topic '{}'", name, topic.getName());
            return false; // User is not subscribed, nothing to do
        }

        boolean removed = topic.getSubscribers().remove(user);
        topicRepository.save(topic);
        log.info("unsubscribe - User '{}' unsubscribed from topic '{}'", name, topic.getName());
        return removed;
    }

    /**
     * Returns all topics enriched with the subscription status for the current
     * user.
     * Never returns null. Throws UserNotFoundException if the user does not exist.
     */
    public List<TopicWithSubscriptionDto> getAllTopicsWithCurrentUserSubscriptionStatus(String username) {
        User user = userService.getCurrentUser(username);
        return topicRepository.findAll().stream()
                .map(topic -> new TopicWithSubscriptionDto(
                        topic.getId(),
                        topic.getName(),
                        topic.getDescription(),
                        topic.getSubscribers().contains(user)))
                .toList();
    }
}
