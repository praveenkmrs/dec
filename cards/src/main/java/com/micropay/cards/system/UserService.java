package com.micropay.cards.system;

import com.micropay.cards.domains.User;
import com.micropay.cards.exceptions.UserNotFoundException;
import com.micropay.cards.system.domain.meta.Meta;
import com.micropay.cards.system.domain.meta.ResponseWrapper;
import com.micropay.cards.system.translators.UserServiceTranslator;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    private static final String MAIN_SERVICE = "mainService";
    private UserServiceClient serviceClient;
    private UserServiceTranslator serviceTranslator;

    @Autowired
    public UserService(UserServiceClient serviceClient, UserServiceTranslator serviceTranslator) {
        this.serviceClient = serviceClient;
        this.serviceTranslator = serviceTranslator;
    }

    /**
     * Call user service to find if the user exists else throw user not found exception
     *
     * @return User
     */
    @Bulkhead(name = "userbulkhead", type = Bulkhead.Type.THREADPOOL)
    @TimeLimiter(name = "usertimelimiter", fallbackMethod = "fallback")
    @CircuitBreaker(name = MAIN_SERVICE, fallbackMethod = "fallback")
    public User getUser(Long id) {
        ResponseWrapper wrapper = serviceClient.getData(id);
        if (null != wrapper.getMeta() && Meta.Code.SUCCESS.equals(wrapper.getMeta().getCode())) {
            return serviceTranslator.translateFromResponse(wrapper.getUserResponse());
        }
        throw new UserNotFoundException("User not found.");
    }

    private User fallback(Long id) {
        log.error("Failed to connect to user service, id: {}", id);
        throw new RuntimeException("Failed to connect to user service");
    }
}
