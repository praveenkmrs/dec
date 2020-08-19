package com.micropay.payments.resource.handlers;


import com.micropay.payments.resource.contracts.meta.Meta;
import com.micropay.payments.resource.contracts.meta.ResponseWrapper;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ValidationException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * Handles all uncaught runtime exceptions
     *
     * @param ex
     * @return ResponseEntity <Map>
     */
    @ExceptionHandler(ValidationException.class)
    @ApiResponse(code = 400, message = "Missing information in request", response = ResponseWrapper.class)
    public final ResponseEntity<ResponseWrapper> handleAllValidationExceptions(ValidationException ex) {
        log.error("Missing information in request", ex);
        return new ResponseEntity<>(
                new ResponseWrapper(
                        new Meta(Meta.Code.INVALID_REQUEST, "Missing information in request. "+ex.getMessage()),
                        null), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all uncaught runtime exceptions
     *
     * @param ex
     * @return ResponseEntity <Map>
     */
    @ExceptionHandler(Exception.class)
    @ApiResponse(code = 500, message = "Error occurred while processing the request", response = ResponseWrapper.class)
    public final ResponseEntity<ResponseWrapper> handleAllExceptions(RuntimeException ex) {
        log.error("Error caught while processing request.", ex);
        return new ResponseEntity<>(
                new ResponseWrapper(
                        new Meta(Meta.Code.APPLICATION_FAILURE, "Error occurred while processing the request"),
                        null), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
