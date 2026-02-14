package com.openclassrooms.mddapi.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

import com.openclassrooms.mddapi.domain.Article;
import com.openclassrooms.mddapi.domain.Topic;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findAllByOrderByCreatedAtDesc();

    List<Article> findByTopicIn(Set<Topic> subscriptions, Sort sortOrder);
}

