package com.micropay.payments.resource;

import com.micropay.payments.exceptions.CardNotFoundException;
import com.micropay.payments.exceptions.ExistingOpenTransactionException;
import com.micropay.payments.exceptions.NoOpenTransactionException;
import com.micropay.payments.resource.contracts.PaymentRequest;
import com.micropay.payments.resource.contracts.meta.Meta;
import com.micropay.payments.resource.contracts.meta.ResponseWrapper;
import com.micropay.payments.resource.translators.RestResourceTranslator;
import com.micropay.payments.service.PaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/payment")
@Api(tags = {"Payments Service"})
@Slf4j
public class RestResource {

    private PaymentService service;
    private RestResourceTranslator restResourceTranslator;

    public RestResource(PaymentService paymentService, RestResourceTranslator resourceTranslator) {
        this.service = paymentService;
        this.restResourceTranslator = resourceTranslator;
    }

    @PostMapping("/open/{userId}")
    @ApiOperation(notes = "Open new payment transaction on the card", produces = MediaType.APPLICATION_JSON_VALUE,
            value = "Open new payment transaction on the card")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully opened a payment transaction in the card", response = ResponseWrapper.class),
            @ApiResponse(code = 400, message = "Customer does not own the card", response = ResponseWrapper.class),
            @ApiResponse(code = 406, message = "An open transaction exists on the card", response = ResponseWrapper.class),
            @ApiResponse(code = 500, message = "Unable to add card to the Customer", response = ResponseWrapper.class),
    })
    @ResponseBody
    public ResponseEntity<ResponseWrapper> openPaymentTransaction(@RequestBody PaymentRequest card, @PathVariable String userId) {
        log.trace("Opening payment transaction for the card {} of user with id {}", card.getCardNumber(), userId);
        try {
            return ResponseEntity.ok(ResponseWrapper.builder()
                    .meta(Meta.builder()
                            .code(Meta.Code.SUCCESS)
                            .description("Successfully added payment transaction to the card.")
                            .build())
                    .response(restResourceTranslator.translateFromDomain(
                            service.openPaymentTransaction(
                                    new PaymentService.OpenPaymentCommand(
                                            card.getCardNumber(), Long.valueOf(userId), card.getAmount().floatValue()))))
                    .build());
        } catch (ExistingOpenTransactionException e) {
            log.error("An open transaction exists on the card.", e);
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ResponseWrapper.builder()
                    .meta(Meta.builder()
                            .code(Meta.Code.ERROR_EXISTING_OPEN_TRANSACTION)
                            .description("An open transaction exists on the card. Cannot make another transaction.")
                            .build())
                    .response(null)
                    .build());
        } catch (CardNotFoundException e) {
            log.error("Customer does not own the card", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.builder()
                    .meta(Meta.builder()
                            .code(Meta.Code.ERROR_USER_DOES_NOT_OWN_CARD)
                            .description("Customer does not own the card.")
                            .build())
                    .response(null)
                    .build());
        }
    }

    @PutMapping("/close/{cardNumber}/{transactionId}")
    @ApiOperation(notes = "Open new payment transaction on the card", produces = MediaType.APPLICATION_JSON_VALUE,
            value = "Open new payment transaction on the card")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully opened a payment transaction in the card", response = ResponseWrapper.class),
            @ApiResponse(code = 400, message = "Customer does not own the card", response = ResponseWrapper.class),
            @ApiResponse(code = 406, message = "An open transaction exists on the card", response = ResponseWrapper.class),
            @ApiResponse(code = 500, message = "Unable to add card to the Customer", response = ResponseWrapper.class),
    })
    @ResponseBody
    public ResponseEntity<ResponseWrapper> closePaymentTransaction(@PathVariable String cardNumber, @PathVariable String transactionId) {
        log.trace("closing payment transaction for the card {} for transaction with id {}", cardNumber, transactionId);
        try {
            return ResponseEntity.ok(ResponseWrapper.builder()
                    .meta(Meta.builder()
                            .code(Meta.Code.SUCCESS)
                            .description("Successfully closed payment transaction to the card.")
                            .build())
                    .response(restResourceTranslator.translateFromDomain(
                            service.closeTransaction(
                                    new PaymentService.ClosePaymentCommand(cardNumber, transactionId))))
                    .build());
        } catch (NoOpenTransactionException e) {
            log.error("No open transaction found for the card {}", cardNumber);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.builder()
                    .meta(Meta.builder()
                            .code(Meta.Code.ERROR_NO_OPEN_PAYMENTS)
                            .description("No open payment requests to close.")
                            .build())
                    .response(null)
                    .build());
        }
    }
}
