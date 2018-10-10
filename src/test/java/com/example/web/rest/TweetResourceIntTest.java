package com.example.web.rest;

import com.example.TwitsterApp;

import com.example.domain.Tweet;
import com.example.domain.User;
import com.example.repository.TweetRepository;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import com.example.service.dto.UserDTO;
import com.example.web.rest.errors.ExceptionTranslator;

import com.example.web.rest.validation.TweetValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;


import static com.example.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TweetResource REST controller.
 *
 * @see TweetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TwitsterApp.class)
public class TweetResourceIntTest {

    private static final String DEFAULT_TWEETER = "AAAAAAAAAA";
    private static final String UPDATED_TWEETER = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private TweetValidator tweetValidator;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restTweetMockMvc;

    private Tweet tweet;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TweetResource tweetResource = new TweetResource(tweetRepository, tweetValidator);
        this.restTweetMockMvc = MockMvcBuilders.standaloneSetup(tweetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tweet createEntity() {
        Tweet tweet = new Tweet()
            .tweeter(DEFAULT_TWEETER)
            .content(DEFAULT_CONTENT);
        return tweet;
    }

    @Before
    public void initTest() {
        tweetRepository.deleteAll();
        userRepository.deleteAll();
        createUser("AAAAAAAAAA");
        createUser("BBBBBBBBBB");
        tweet = createEntity();
    }

    @Test
    public void createTweet() throws Exception {
        int databaseSizeBeforeCreate = tweetRepository.findAll().size();

        // Create the Tweet
        restTweetMockMvc.perform(post("/api/tweets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tweet)))
            .andExpect(status().isCreated());

        // Validate the Tweet in the database
        List<Tweet> tweetList = tweetRepository.findAll();
        assertThat(tweetList).hasSize(databaseSizeBeforeCreate + 1);
        Tweet testTweet = tweetList.get(tweetList.size() - 1);
        assertThat(testTweet.getTweeter()).isEqualTo(DEFAULT_TWEETER);
        assertThat(testTweet.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    public void createTweetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tweetRepository.findAll().size();

        // Create the Tweet with an existing ID
        tweet.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restTweetMockMvc.perform(post("/api/tweets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tweet)))
            .andExpect(status().isBadRequest());

        // Validate the Tweet in the database
        List<Tweet> tweetList = tweetRepository.findAll();
        assertThat(tweetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkTweeterIsRequired() throws Exception {
        int databaseSizeBeforeTest = tweetRepository.findAll().size();
        // set the field null
        tweet.setTweeter(null);

        // Create the Tweet, which fails.

        restTweetMockMvc.perform(post("/api/tweets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tweet)))
            .andExpect(status().isBadRequest());

        List<Tweet> tweetList = tweetRepository.findAll();
        assertThat(tweetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = tweetRepository.findAll().size();
        // set the field null
        tweet.setContent(null);

        // Create the Tweet, which fails.

        restTweetMockMvc.perform(post("/api/tweets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tweet)))
            .andExpect(status().isBadRequest());

        List<Tweet> tweetList = tweetRepository.findAll();
        assertThat(tweetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllTweets() throws Exception {
        // Initialize the database
        tweetRepository.save(tweet);

        // Get all the tweetList
        restTweetMockMvc.perform(get("/api/tweets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tweet.getId())))
            .andExpect(jsonPath("$.[*].tweeter").value(hasItem(DEFAULT_TWEETER.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @Test
    public void getTweet() throws Exception {
        // Initialize the database
        tweetRepository.save(tweet);

        // Get the tweet
        restTweetMockMvc.perform(get("/api/tweets/{id}", tweet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tweet.getId()))
            .andExpect(jsonPath("$.tweeter").value(DEFAULT_TWEETER.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
    }

    @Test
    public void getNonExistingTweet() throws Exception {
        // Get the tweet
        restTweetMockMvc.perform(get("/api/tweets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateTweet() throws Exception {
        // Initialize the database
        tweetRepository.save(tweet);

        int databaseSizeBeforeUpdate = tweetRepository.findAll().size();

        // Update the tweet
        Tweet updatedTweet = tweetRepository.findById(tweet.getId()).get();
        updatedTweet
            .tweeter(UPDATED_TWEETER)
            .content(UPDATED_CONTENT);

        restTweetMockMvc.perform(put("/api/tweets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTweet)))
            .andExpect(status().isOk());

        // Validate the Tweet in the database
        List<Tweet> tweetList = tweetRepository.findAll();
        assertThat(tweetList).hasSize(databaseSizeBeforeUpdate);
        Tweet testTweet = tweetList.get(tweetList.size() - 1);
        assertThat(testTweet.getTweeter()).isEqualTo(UPDATED_TWEETER);
        assertThat(testTweet.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    public void updateNonExistingTweet() throws Exception {
        int databaseSizeBeforeUpdate = tweetRepository.findAll().size();

        // Create the Tweet

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTweetMockMvc.perform(put("/api/tweets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tweet)))
            .andExpect(status().isBadRequest());

        // Validate the Tweet in the database
        List<Tweet> tweetList = tweetRepository.findAll();
        assertThat(tweetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteTweet() throws Exception {
        // Initialize the database
        tweetRepository.save(tweet);

        int databaseSizeBeforeDelete = tweetRepository.findAll().size();

        // Get the tweet
        restTweetMockMvc.perform(delete("/api/tweets/{id}", tweet.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Tweet> tweetList = tweetRepository.findAll();
        assertThat(tweetList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tweet.class);
        Tweet tweet1 = new Tweet();
        tweet1.setId("id1");
        Tweet tweet2 = new Tweet();
        tweet2.setId(tweet1.getId());
        assertThat(tweet1).isEqualTo(tweet2);
        tweet2.setId("id2");
        assertThat(tweet1).isNotEqualTo(tweet2);
        tweet1.setId(null);
        assertThat(tweet1).isNotEqualTo(tweet2);
    }

    private void createUser(String login) {
        UserDTO user = new UserDTO();
        user.setLogin(login);
        user.setEmail(String.format("%s@example.com", login));
        userService.createUser(user);
    }
}
