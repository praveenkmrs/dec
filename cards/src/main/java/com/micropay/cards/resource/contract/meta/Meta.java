package com.micropay.cards.resource.contract.meta;

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
        APPLICATION_FAILURE("CA500"),
        INVALID_REQUEST("CA501"),
        ERROR_USER_NOT_FOUND("CA400"),
        ERROR_FETCHING_CARD_DETAILS("CA401"),
        ERROR_NO_CARDS_REGISTERED("CA402"),
        ERROR_CARD_ALREADY_EXISTS("CA403")
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
