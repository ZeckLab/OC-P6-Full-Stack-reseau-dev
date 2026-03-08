package com.openclassrooms.mddapi.service;

import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.domain.User;
import com.openclassrooms.mddapi.exception.NotFoundException;
import com.openclassrooms.mddapi.constants.ErrorMessages;
import com.openclassrooms.mddapi.domain.Topic;
import com.openclassrooms.mddapi.repository.TopicRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Service responsible for managing topics and user subscriptions.
 * Provides operations for retrieving topics, subscribing, and unsubscribing users.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final UserService userService;

    /**
     * Retrieves all available topics.
     *
     * @return a list of Topic entities
     */
    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    /**
     * Retrieves a topic by its ID.
     *
     * @param id the ID of the topic to retrieve
     * @return the Topic entity
     * @throws NotFoundException if no topic exists with the given ID
     */
    public Topic getTopicById(Long id) {
        log.info("getTopicById - Retrieving topic with ID: {}", id);
        return topicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.TOPIC_NOT_FOUND + " " + id));
    }

    /**
     * Subscribes the current user to the specified topic.
     *
     * @param id the ID of the topic to subscribe to
     * @return true if the subscription was newly created, false if the user was already subscribed
     */
    public boolean subscribe(Long id) {
        User user = userService.getCurrentUser();
        Topic topic = getTopicById(id);
        log.info("subscribe - User '{}' subscribing to topic '{}'", user.getUsername(), topic.getName());

        if (topic.getSubscribers().contains(user)) {
            log.info("subscribe - User '{}' is already subscribed to topic '{}'", user.getUsername(), topic.getName());
            return false; // User is already subscribed, nothing to do
        }

        boolean added = topic.getSubscribers().add(user); // Set - no need to check for duplicates
        topicRepository.save(topic);
        log.info("subscribe - User '{}' subscribed to topic '{}'", user.getUsername(), topic.getName());
        return added;

    }

    /**
     * Unsubscribes the current user from the specified topic.
     *
     * @param id the ID of the topic to unsubscribe from
     * @return true if the user was unsubscribed, false if they were not subscribed
     */
    public boolean unsubscribe(Long id) {
        User user = userService.getCurrentUser();
        Topic topic = getTopicById(id);
        log.info("unsubscribe - User '{}' unsubscribing from topic '{}'", user.getUsername(), topic.getName());

        if (!topic.getSubscribers().contains(user)) {
            log.info("unsubscribe - User '{}' is not subscribed to topic '{}'", user.getUsername(), topic.getName());
            return false; // User is not subscribed, nothing to do
        }

        boolean removed = topic.getSubscribers().remove(user);
        topicRepository.save(topic);
        log.info("unsubscribe - User '{}' unsubscribed from topic '{}'", user.getUsername(), topic.getName());
        return removed;
    }
}
