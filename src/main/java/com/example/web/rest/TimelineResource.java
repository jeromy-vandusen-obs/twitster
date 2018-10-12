package com.example.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.example.domain.Tweet;
import com.example.repository.TweetRepository;
import com.example.security.SecurityUtils;
import com.example.web.rest.errors.InternalServerErrorException;
import com.example.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TimelineResource {
    private final Logger log = LoggerFactory.getLogger(TimelineResource.class);

    private final TweetRepository tweetRepository;

    public TimelineResource(TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    @GetMapping("/timeline")
    @Timed
    public ResponseEntity<List<Tweet>> view(Pageable pageable) {
        log.debug("REST request to get a page of the timeline for ");
        final String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
        Page<Tweet> timeline = tweetRepository.findByTweeterOrderByCreateTimeDesc(userLogin, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(timeline, "/api/tweets");
        return new ResponseEntity<>(timeline.getContent(), headers, HttpStatus.OK);
    }
}
