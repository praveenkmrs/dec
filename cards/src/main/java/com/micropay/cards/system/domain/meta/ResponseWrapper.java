package com.micropay.cards.system.domain.meta;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.micropay.cards.system.domain.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseWrapper {
    private Meta meta;
    private UserResponse userResponse;
}
