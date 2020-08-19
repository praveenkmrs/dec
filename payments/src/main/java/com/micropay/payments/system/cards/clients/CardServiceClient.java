package com.micropay.payments.system.cards.clients;

import com.micropay.payments.system.cards.domains.meta.ResponseWrapper;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "card-service", path = "/v1/cards")
@LoadBalancerClient(name = "card-service")
public interface CardServiceClient {

    @GetMapping("/{dataId}")
    ResponseWrapper getCard(@PathVariable Long dataId);

    @GetMapping("/user/{userId}")
    ResponseWrapper getCards(@PathVariable Long userId);

}