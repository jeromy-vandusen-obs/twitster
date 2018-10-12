package com.example.repository;

import com.example.domain.Tweet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Tweet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TweetRepository extends MongoRepository<Tweet, String> {
    Page<Tweet> findByTweeterOrderByCreateTimeDesc(String tweeter, Pageable pageable);
}
