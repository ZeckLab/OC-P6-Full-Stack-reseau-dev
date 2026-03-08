package com.openclassrooms.mddapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import com.openclassrooms.mddapi.constants.SuccessMessages;
import com.openclassrooms.mddapi.domain.Topic;
import com.openclassrooms.mddapi.dto.response.ApiResponseDTO;
import com.openclassrooms.mddapi.dto.response.TopicDTO;
import com.openclassrooms.mddapi.dto.response.TopicWithSubscriptionDto;
import com.openclassrooms.mddapi.mapper.TopicMapper;
import com.openclassrooms.mddapi.service.TopicService;
import com.openclassrooms.mddapi.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;

/**
 * REST controller responsible for managing topics and user subscriptions.
 * Provides endpoints for retrieving topics, checking subscription status,
 * and subscribing or unsubscribing the current user from a topic.
 */
@Slf4j
@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;
    private final TopicMapper topicMapper;
    private final UserService userService;

    /**
     * Retrieves all available topics.
     *
     * @return a list of TopicDTO objects representing all topics
     */
    @GetMapping
    public ResponseEntity<List<TopicDTO>> getTopics() {
        log.info("GET /topics - Fetching all topics");

        List<TopicDTO> topics = topicMapper.toDtoList(topicService.getAllTopics());

        log.info("GET /topics - {} topics found", topics.size());
        return ResponseEntity.ok(topics);
    }

    /**
     * Retrieves all topics along with the subscription status
     * for the currently authenticated user.
     *
     * @return a list of TopicWithSubscriptionDto objects including subscription information
     */
    @GetMapping("/me")
    public ResponseEntity<List<TopicWithSubscriptionDto>> getAllTopicsWithCurrentUserSubscriptionStatus() {
        log.info("GET /topics/me - Fetching all topics with subscription status for current user");
        List<Topic> topics = this.topicService.getAllTopics();

        log.info("GET /topics/me - {} topics found with subscription status for current user", topics.size());

        return ResponseEntity.ok(topicMapper.toTopicWithSubscriptionDtoList(topics, userService.getCurrentUser()));
    }

    /**
     * Subscribes the current user to the specified topic.
     *
     * @param id the ID of the topic to subscribe to
     * @return ApiResponseDTO indicating whether the subscription was successful
     */
    @PostMapping("/{id}/subscribe")
    public ResponseEntity<ApiResponseDTO> subscribe(@PathVariable Long id) {
        log.info("POST /topics/{}/subscribe - Subscription request receive", id);
        try {
            boolean isSubscribed = this.topicService.subscribe(id);
            log.info("POST /topics/{}/subscribe - Subscription result: {}", id, isSubscribed);

            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDTO(
                    isSubscribed ? SuccessMessages.SUBSCRIBED_TO_TOPIC_SUCCESSFULLY
                            : SuccessMessages.ALREADY_SUBSCRIBED_TO_TOPIC,
                    HttpStatus.OK.value()));
        } catch (Exception e) {
            log.warn("POST /topics/{}/subscribe - Error subscribing user to topic: {}", id, e.getMessage());
            throw e; // Let the global exception handler deal with it
        }
    }

    /**
     * Unsubscribes the current user from the specified topic.
     *
     * @param id the ID of the topic to unsubscribe from
     * @return ApiResponseDTO indicating whether the unsubscription was successful
     */
    @DeleteMapping("/{id}/subscribe")
    public ResponseEntity<ApiResponseDTO> unsubscribe(@PathVariable Long id) {
        log.info("DELETE /topics/{}/unsubscribe - Unsubscription request receive", id);
        try {
            boolean isUnsubscribed = this.topicService.unsubscribe(id);
            log.info("DELETE /topics/{}/unsubscribe - Unsubscription result: {}", id, isUnsubscribed);

            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDTO(
                    isUnsubscribed ? SuccessMessages.UNSUBSCRIBED_FROM_TOPIC_SUCCESSFULLY
                            : SuccessMessages.ALREADY_UNSUBSCRIBED_FROM_TOPIC,
                    HttpStatus.OK.value()));
        } catch (Exception e) {
            log.warn("DELETE /topics/{}/unsubscribe - Error unsubscribing user from topic: {}", id, e.getMessage());
            throw e; // Let the global exception handler deal with it
        }
    }
}
