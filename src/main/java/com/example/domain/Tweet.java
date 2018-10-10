package com.example.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A Tweet.
 */
@Document(collection = "tweet")
public class Tweet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("tweeter")
    private String tweeter;

    @NotNull
    @Field("content")
    private String content;

    @NotNull
    @Field("createTime")
    private Instant createTime = Instant.now();

    @Field("refs")
    private List<String> refs = new ArrayList<>();

    @Field("tags")
    private List<String> tags = new ArrayList<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTweeter() {
        return tweeter;
    }

    public Tweet tweeter(String tweeter) {
        this.setTweeter(tweeter);
        return this;
    }

    public void setTweeter(String tweeter) {
        this.tweeter = tweeter;
    }

    public String getContent() {
        return content;
    }

    public Tweet content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
        extractRefsFromContent();
        extractTagsFromContent();
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    public Instant getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public List<String> getRefs() {
        return refs;
    }

    public List<String> getTags() {
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tweet tweet = (Tweet) o;
        if (tweet.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tweet.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Tweet{" +
            "id=" + getId() +
            ", tweeter='" + getTweeter() + "'" +
            ", content='" + getContent() + "'" +
            "}";
    }

    private void extractRefsFromContent() {
        refs = extractTokensByPrefix(content, "@");
    }

    private void extractTagsFromContent() {
        tags = extractTokensByPrefix(content, "#");
    }

    private List<String> extractTokensByPrefix(String content, String prefix) {
        // extremely naive implementation here...
        List<String> tokens = new ArrayList<>();
        if (content == null) {
            return tokens;
        }
        int startIndex = content.indexOf(prefix);
        if (startIndex < 0) {
            return tokens;
        }
        int endIndex = content.indexOf(" ", startIndex + 1);
        tokens.add(content.substring(startIndex + 1, endIndex));
        tokens.addAll(extractTokensByPrefix(content.substring(endIndex), prefix));
        return tokens;
    }
}
