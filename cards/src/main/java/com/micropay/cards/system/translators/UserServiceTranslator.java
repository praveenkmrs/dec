package com.micropay.cards.system.translators;

import com.micropay.cards.domains.User;
import com.micropay.cards.system.domain.UserResponse;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class UserServiceTranslator {
    public User translateFromResponse(UserResponse response) {
        return new User(response.getId(), response.getName(), Collections.emptyList());
    }
}
