package com.micropay.customers.resource.contracts.meta;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.micropay.customers.resource.contracts.UserResponse;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseWrapper {
    private Meta meta;
    private UserResponse userResponse;
}
