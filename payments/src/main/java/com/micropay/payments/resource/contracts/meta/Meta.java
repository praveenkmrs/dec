package com.micropay.payments.resource.contracts.meta;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Meta {

    private Code code;
    private String description;

    @Getter
    @ToString
    public enum Code implements Serializable {
        SUCCESS("SUCCESS"),
        APPLICATION_FAILURE("PY500"),
        INVALID_REQUEST("PY501"),
        ERROR_EXISTING_OPEN_TRANSACTION("PY400"),
        ERROR_USER_DOES_NOT_OWN_CARD("PY401"),
        ERROR_NO_OPEN_PAYMENTS("PY402"),
        ;

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
