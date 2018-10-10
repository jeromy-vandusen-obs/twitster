package com.example.web.rest.validation;

import com.example.domain.Tweet;
import com.example.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class TweetValidator implements Validator {
    private final UserRepository userRepository;

    public TweetValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Tweet.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Tweet tweet = (Tweet) o;
        if (! userRepository.findOneByLogin(tweet.getTweeter()).isPresent()) {
            errors.rejectValue("tweeter", "tweeter.unknown");
        }
    }
}
