package com.example.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.example.domain.Tweet;
import com.example.repository.TweetRepository;
import com.example.web.rest.errors.BadRequestAlertException;
import com.example.web.rest.util.HeaderUtil;
import com.example.web.rest.util.PaginationUtil;
import com.example.web.rest.validation.TweetValidator;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Tweet.
 */
@RestController
@RequestMapping("/api")
public class TweetResource {

    private final Logger log = LoggerFactory.getLogger(TweetResource.class);

    private static final String ENTITY_NAME = "tweet";

    private final TweetRepository tweetRepository;

    private final TweetValidator tweetValidator;

    public TweetResource(TweetRepository tweetRepository, TweetValidator tweetValidator) {
        this.tweetRepository = tweetRepository;
        this.tweetValidator = tweetValidator;
    }

    @InitBinder("tweet")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(tweetValidator);
    }

    /**
     * POST  /tweets : Create a new tweet.
     *
     * @param tweet the tweet to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tweet, or with status 400 (Bad Request) if the tweet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tweets")
    @Timed
    public ResponseEntity<Tweet> createTweet(@Valid @RequestBody Tweet tweet) throws URISyntaxException {
        log.debug("REST request to save Tweet : {}", tweet);
        if (tweet.getId() != null) {
            throw new BadRequestAlertException("A new tweet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tweet result = tweetRepository.save(tweet);
        return ResponseEntity.created(new URI("/api/tweets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tweets : Updates an existing tweet.
     *
     * @param tweet the tweet to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tweet,
     * or with status 400 (Bad Request) if the tweet is not valid,
     * or with status 500 (Internal Server Error) if the tweet couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tweets")
    @Timed
    public ResponseEntity<Tweet> updateTweet(@Valid @RequestBody Tweet tweet) throws URISyntaxException {
        log.debug("REST request to update Tweet : {}", tweet);
        if (tweet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Tweet result = tweetRepository.save(tweet);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tweet.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tweets : get all the tweets.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tweets in body
     */
    @GetMapping("/tweets")
    @Timed
    public ResponseEntity<List<Tweet>> getAllTweets(Pageable pageable) {
        log.debug("REST request to get a page of Tweets");
        Page<Tweet> page = tweetRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tweets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tweets/:id : get the "id" tweet.
     *
     * @param id the id of the tweet to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tweet, or with status 404 (Not Found)
     */
    @GetMapping("/tweets/{id}")
    @Timed
    public ResponseEntity<Tweet> getTweet(@PathVariable String id) {
        log.debug("REST request to get Tweet : {}", id);
        Optional<Tweet> tweet = tweetRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tweet);
    }

    /**
     * DELETE  /tweets/:id : delete the "id" tweet.
     *
     * @param id the id of the tweet to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tweets/{id}")
    @Timed
    public ResponseEntity<Void> deleteTweet(@PathVariable String id) {
        log.debug("REST request to delete Tweet : {}", id);

        tweetRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
