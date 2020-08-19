package com.micropay.cards.system;

import com.micropay.cards.system.domain.meta.ResponseWrapper;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service", path = "/v1/customers")
@LoadBalancerClient(name = "customer-service")
public interface UserServiceClient {

    @GetMapping("/{dataId}")
    ResponseWrapper getData(@PathVariable Long dataId);

}
