package com.micropay.customers.resource.contracts.meta;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Meta {

    private Code code;
    private String description;

    @Getter
    public enum Code implements Serializable {
        SUCCESS("SUCCESS"),
        APPLICATION_FAILURE("US500"),
        INVALID_REQUEST("US501"),
        ERROR_USER_NOT_FOUND("US400"),
        ERROR_FETCHING_USER_DETAILS("US401"),
        ERROR_USER_ALREADY_EXISITS("US402");

        private final String errCode;

        Code(String code) {
            this.errCode = code;
        }

        @JsonValue
        public String getErrorCode() {
            return errCode;
        }
    }
}
