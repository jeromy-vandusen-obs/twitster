package com.example.domain;

import org.junit.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

public class TweetTest {
    @Test
    public void constructor_always_initializesCreateTimeToCurrentTime() {
        Instant now = Instant.now();

        Tweet tweet = new Tweet();

        assertThat(tweet.getCreateTime()).isNotNull().isAfterOrEqualTo(now);
    }

    @Test
    public void setContent_whenArgumentIsEmpty_resultsInEmptyRefsList() {
        Tweet tweet = new Tweet();

        tweet.setContent("");

        assertThat(tweet.getRefs()).isNotNull().isEmpty();
    }

    @Test
    public void content_whenArgumentIsEmpty_resultsInEmptyRefsList() {
        Tweet tweet = new Tweet().content("");

        assertThat(tweet.getRefs()).isNotNull().isEmpty();
    }

    @Test
    public void setContent_whenArgumentIsNotEmptyAndContainsNoRefs_resultsInEmptyRefsList() {
        Tweet tweet = new Tweet();

        tweet.setContent("This is a test with no refs. It will generate an empty refs list.");

        assertThat(tweet.getRefs()).isNotNull().isEmpty();
    }

    @Test
    public void content_whenArgumentIsNotEmptyAndContainsNoRefs_resultsInEmptyRefsList() {
        Tweet tweet = new Tweet().content("This is a test with no refs. It will generate an empty refs list.");

        assertThat(tweet.getRefs()).isNotNull().isEmpty();
    }

    @Test
    public void setContent_whenArgumentIsNotEmptyAndContainsOneRef_resultsInRefsListWithCorrectEntry() {
        Tweet tweet = new Tweet();

        tweet.setContent("This is a test with one ref. @SampleRef is the ref. It will generate a refs list with one ref.");

        assertThat(tweet.getRefs()).isNotNull().hasSize(1).containsExactly("SampleRef");
    }

    @Test
    public void content_whenArgumentIsNotEmptyAndContainsOneRef_resultsInRefsListWithCorrectEntry() {
        Tweet tweet = new Tweet().content("This is a test with one ref. @SampleRef is the ref. It will generate a refs list with one ref.");

        assertThat(tweet.getRefs()).isNotNull().hasSize(1).containsExactly("SampleRef");
    }

    @Test
    public void setContent_whenArgumentIsNotEmptyAndContainsMultipleRefs_resultsInRefsListWithCorrectEntries() {
        Tweet tweet = new Tweet();

        tweet.setContent("This is a test with @MultipleRefs in it. @SampleRef is one of the refs. It will @Generate a refs list with three refs.");

        assertThat(tweet.getRefs()).isNotNull().hasSize(3).containsExactlyInAnyOrder("MultipleRefs", "SampleRef", "Generate");
    }

    @Test
    public void content_whenArgumentIsNotEmptyAndContainsMultipleRefs_resultsInRefsListWithCorrectEntries() {
        Tweet tweet = new Tweet().content("This is a test with @MultipleRefs in it. @SampleRef is one of the refs. It will @Generate a refs list with three refs.");

        assertThat(tweet.getRefs()).isNotNull().hasSize(3).containsExactlyInAnyOrder("MultipleRefs", "SampleRef", "Generate");
    }

    @Test
    public void setContent_whenArgumentIsEmpty_resultsInEmptyTagsList() {
        Tweet tweet = new Tweet();

        tweet.setContent("");

        assertThat(tweet.getTags()).isNotNull().isEmpty();
    }

    @Test
    public void content_whenArgumentIsEmpty_resultsInEmptyTagsList() {
        Tweet tweet = new Tweet().content("");

        assertThat(tweet.getTags()).isNotNull().isEmpty();
    }

    @Test
    public void setContent_whenArgumentIsNotEmptyAndContainsNoTags_resultsInEmptyTagsList() {
        Tweet tweet = new Tweet();

        tweet.setContent("This is a test with no tags. It will generate an empty tags list.");

        assertThat(tweet.getTags()).isNotNull().isEmpty();
    }

    @Test
    public void content_whenArgumentIsNotEmptyAndContainsNoTags_resultsInEmptyTagsList() {
        Tweet tweet = new Tweet().content("This is a test with no tags. It will generate an empty tags list.");

        assertThat(tweet.getTags()).isNotNull().isEmpty();
    }

    @Test
    public void setContent_whenArgumentIsNotEmptyAndContainsOneTag_resultsInTagsListWithCorrectEntry() {
        Tweet tweet = new Tweet();

        tweet.setContent("This is a test with one tag. #SampleTag is the tag. It will generate a tags list with one tag.");

        assertThat(tweet.getTags()).isNotNull().hasSize(1).containsExactly("SampleTag");
    }

    @Test
    public void content_whenArgumentIsNotEmptyAndContainsOneTag_resultsInTagsListWithCorrectEntry() {
        Tweet tweet = new Tweet().content("This is a test with one tag. #SampleTag is the tag. It will generate a tags list with one tag.");

        assertThat(tweet.getTags()).isNotNull().hasSize(1).containsExactly("SampleTag");
    }

    @Test
    public void setContent_whenArgumentIsNotEmptyAndContainsMultipleTags_resultsInTagsListWithCorrectEntries() {
        Tweet tweet = new Tweet();

        tweet.setContent("This is a test with #MultipleTags in it. #SampleTag is one of the tags. It will #Generate a tags list with three tags.");

        assertThat(tweet.getTags()).isNotNull().hasSize(3).containsExactlyInAnyOrder("MultipleTags", "SampleTag", "Generate");
    }

    @Test
    public void content_whenArgumentIsNotEmptyAndContainsMultipleTags_resultsInTagsListWithCorrectEntries() {
        Tweet tweet = new Tweet().content("This is a test with #MultipleTags in it. #SampleTag is one of the tags. It will #Generate a tags list with three tags.");

        assertThat(tweet.getTags()).isNotNull().hasSize(3).containsExactlyInAnyOrder("MultipleTags", "SampleTag", "Generate");
    }
}
