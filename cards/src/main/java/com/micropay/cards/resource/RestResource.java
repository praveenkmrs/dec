package com.micropay.cards.resource;

import com.micropay.cards.exceptions.CardAlreadyExistsException;
import com.micropay.cards.exceptions.CardNotFoundException;
import com.micropay.cards.exceptions.UserNotFoundException;
import com.micropay.cards.resource.contract.CardRequest;
import com.micropay.cards.resource.contract.meta.Meta;
import com.micropay.cards.resource.contract.meta.ResponseWrapper;
import com.micropay.cards.resource.translators.RestResourceTranslator;
import com.micropay.cards.service.CardService;
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
@RequestMapping("/v1/cards")
@Api(tags = {"Cards Service"})
@Slf4j
public class RestResource {

    private CardService service;
    private RestResourceTranslator restResourceTranslator;

    public RestResource(CardService cardService, RestResourceTranslator restResourceTranslator) {
        this.service = cardService;
        this.restResourceTranslator = restResourceTranslator;
    }

    /**
     * Get the card details of the user
     *
     * @param id
     * @return
     */
    @GetMapping("/user/{id}")
    @ApiOperation(notes = "Gets the card details of the customer", produces = MediaType.APPLICATION_JSON_VALUE,
            value = "Gets the card details of the customer")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved card details of the customer", response = ResponseWrapper.class),
            @ApiResponse(code = 404, message = "Customer not found", response = ResponseWrapper.class),
            @ApiResponse(code = 404, message = "No cards found", response = ResponseWrapper.class),
            @ApiResponse(code = 500, message = "Unable to find card details of the Customer", response = ResponseWrapper.class),
    })
    @ResponseBody
    public ResponseEntity<ResponseWrapper> getAllCardsOfUser(@PathVariable String id) {
        log.info("Getting card details of the user with id {}", id);
        try {
            return ResponseEntity.ok(ResponseWrapper.builder()
                    .meta(Meta.builder()
                            .code(Meta.Code.SUCCESS)
                            .description("Successfully found the cards of the customer")
                            .build())
                    .response(restResourceTranslator.translateToContract(service.getCardsOfUser(Long.valueOf(id))))
                    .build());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.builder()
                    .meta(Meta.builder()
                            .code(Meta.Code.ERROR_USER_NOT_FOUND)
                            .description("Customer not found")
                            .build())
                    .response(null)
                    .build());
        } catch (CardNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.builder()
                    .meta(Meta.builder()
                            .code(Meta.Code.ERROR_NO_CARDS_REGISTERED)
                            .description("Card not found")
                            .build())
                    .response(null)
                    .build());
        }
    }

    /**
     * Get details of the card
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(notes = "Gets the card details", produces = MediaType.APPLICATION_JSON_VALUE,
            value = "Gets the card details")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved card details", response = ResponseWrapper.class),
            @ApiResponse(code = 404, message = "No cards found", response = ResponseWrapper.class),
            @ApiResponse(code = 500, message = "Unable to find card details", response = ResponseWrapper.class),
    })
    @ResponseBody
    public ResponseEntity<ResponseWrapper> getCardDetails(@PathVariable String id) {
        log.info("Getting details of the card with id {}", id);
        try {
            return ResponseEntity.ok(ResponseWrapper.builder()
                    .meta(Meta.builder()
                            .code(Meta.Code.SUCCESS)
                            .description("Successfully found the card")
                            .build())
                    .response(restResourceTranslator.translateToContract(service.getCardDetails(Long.valueOf(id))))
                    .build());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.builder()
                    .meta(Meta.builder()
                            .code(Meta.Code.ERROR_USER_NOT_FOUND)
                            .description("Customer not found")
                            .build())
                    .response(null)
                    .build());
        } catch (CardNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.builder()
                    .meta(Meta.builder()
                            .code(Meta.Code.ERROR_NO_CARDS_REGISTERED)
                            .description("Card not found")
                            .build())
                    .response(null)
                    .build());
        }
    }

    @PostMapping("/{id}")
    @ApiOperation(notes = "Add new card to the customer", produces = MediaType.APPLICATION_JSON_VALUE,
            value = "Add new card to the customer")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added card to the customer", response = ResponseWrapper.class),
            @ApiResponse(code = 400, message = "Card already exists", response = ResponseWrapper.class),
            @ApiResponse(code = 500, message = "Unable to add card to the Customer", response = ResponseWrapper.class),
    })
    @ResponseBody
    public ResponseEntity<ResponseWrapper> addCardToUser(@RequestBody CardRequest card, @PathVariable String id) {
        try {
            return ResponseEntity.ok(ResponseWrapper.builder()
                    .meta(Meta.builder()
                            .code(Meta.Code.SUCCESS)
                            .description("Successfully added new card to the customer")
                            .build())
                    .response(restResourceTranslator.translateToContract(
                            service.addCard(restResourceTranslator.translateToDomain(card), Long.valueOf(id))))
                    .build());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.builder()
                    .meta(Meta.builder()
                            .code(Meta.Code.ERROR_USER_NOT_FOUND)
                            .description("Customer does not exists")
                            .build())
                    .response(null)
                    .build());
        } catch (CardAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.builder()
                    .meta(Meta.builder()
                            .code(Meta.Code.ERROR_CARD_ALREADY_EXISTS)
                            .description("Card already exists")
                            .build())
                    .response(null)
                    .build());
        }
    }

    @DeleteMapping("/{cardId}/{userId}")
    @ApiOperation(notes = "Remove card from the customer", produces = MediaType.APPLICATION_JSON_VALUE,
            value = "Remove card from the customer")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully removed card from the customer", response = ResponseWrapper.class),
            @ApiResponse(code = 404, message = "Customer not found", response = ResponseWrapper.class),
            @ApiResponse(code = 404, message = "Card not found", response = ResponseWrapper.class),
            @ApiResponse(code = 500, message = "Unable to remove card from the Customer", response = ResponseWrapper.class),
    })
    @ResponseBody
    public ResponseEntity<ResponseWrapper> deleteCard(@PathVariable String cardId, @PathVariable String userId) {
        try {
            service.removeCard(new CardService.RemoveUserCommand(cardId, userId));
            return ResponseEntity.ok(ResponseWrapper.builder()
                    .meta(Meta.builder()
                            .code(Meta.Code.SUCCESS)
                            .description("Successfully removed the card from customer")
                            .build())
                    .response(null)
                    .build());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.builder()
                    .meta(Meta.builder()
                            .code(Meta.Code.ERROR_USER_NOT_FOUND)
                            .description("Customer not found")
                            .build())
                    .response(null)
                    .build());
        } catch (CardNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.builder()
                    .meta(Meta.builder()
                            .code(Meta.Code.ERROR_NO_CARDS_REGISTERED)
                            .description("Card not found")
                            .build())
                    .response(null)
                    .build());
        }
    }
}