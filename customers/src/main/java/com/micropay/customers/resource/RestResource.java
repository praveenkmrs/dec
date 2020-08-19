package com.micropay.customers.resource;

import com.micropay.customers.exceptions.UserExists;
import com.micropay.customers.exceptions.UserNotFound;
import com.micropay.customers.resource.contracts.UserRequest;
import com.micropay.customers.resource.contracts.meta.Meta;
import com.micropay.customers.resource.contracts.meta.ResponseWrapper;
import com.micropay.customers.resource.translators.RestResourceTranslator;
import com.micropay.customers.service.CustomerService;
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
@RequestMapping("/v1/customers")
@Api(tags = {"Customer Service"})
@Slf4j
public class RestResource {
    private CustomerService service;
    private RestResourceTranslator restResourceTranslator;

    public RestResource(CustomerService service, RestResourceTranslator restResourceTranslator) {
        this.service = service;
        this.restResourceTranslator = restResourceTranslator;
    }

    /**
     * Get the details of the user
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(notes = "Gets the details of the customer", produces = MediaType.APPLICATION_JSON_VALUE,
            value = "Gets the details of the customer")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved customer details", response = ResponseWrapper.class),
            @ApiResponse(code = 404, message = "Customer not found", response = ResponseWrapper.class),
            @ApiResponse(code = 500, message = "Unable to find details of the Customer", response = ResponseWrapper.class),
    })
    @ResponseBody
    public ResponseEntity<ResponseWrapper> getUser(@PathVariable String id) {
        log.info("Getting details of the user with id {}", id);
        try {
            return ResponseEntity.ok(ResponseWrapper.builder()
                    .meta(Meta.builder()
                            .code(Meta.Code.SUCCESS)
                            .description("Successfully found the customer")
                            .build())
                    .userResponse(restResourceTranslator.translateToContract(service.getCustomerById(Long.valueOf(id))))
                    .build());
        } catch (UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.builder()
                    .meta(Meta.builder()
                            .code(Meta.Code.ERROR_USER_NOT_FOUND)
                            .description("Customer not found")
                            .build())
                    .userResponse(null)
                    .build());
        }
    }

    @PostMapping
    @ApiOperation(notes = "Creates new customer with the details provided", produces = MediaType.APPLICATION_JSON_VALUE,
            value = "Creates new customer with the details provided")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created the customer", response = ResponseWrapper.class),
            @ApiResponse(code = 400, message = "Customer already exists", response = ResponseWrapper.class),
            @ApiResponse(code = 500, message = "Unable to update details of the Customer", response = ResponseWrapper.class),
    })
    @ResponseBody
    public ResponseEntity<ResponseWrapper> createUser(@RequestBody UserRequest user) {
        try {
            return ResponseEntity.ok(ResponseWrapper.builder()
                    .meta(Meta.builder()
                            .code(Meta.Code.SUCCESS)
                            .description("Successfully created the customer")
                            .build())
                    .userResponse(restResourceTranslator.translateToContract(
                            service.createCustomer(restResourceTranslator.translateToDomain(user))))
                    .build());
        } catch (UserExists e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.builder()
                    .meta(Meta.builder()
                            .code(Meta.Code.ERROR_USER_ALREADY_EXISITS)
                            .description("Customer already exists")
                            .build())
                    .userResponse(null)
                    .build());
        }
    }

    @PutMapping("/{id}")
    @ApiOperation(notes = "Update details of the customer", produces = MediaType.APPLICATION_JSON_VALUE,
            value = "Update details of the customer")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated customer details", response = ResponseWrapper.class),
            @ApiResponse(code = 404, message = "Customer not found", response = ResponseWrapper.class),
            @ApiResponse(code = 500, message = "Unable to update details of the Customer", response = ResponseWrapper.class),
    })
    @ResponseBody
    public ResponseEntity<ResponseWrapper> updateUser(@PathVariable String id, @RequestBody UserRequest user) {
        try {
            return ResponseEntity.ok(ResponseWrapper.builder()
                    .meta(Meta.builder()
                            .code(Meta.Code.SUCCESS)
                            .description("Successfully updated the customer")
                            .build())
                    .userResponse(restResourceTranslator.translateToContract(
                            service.updateCustomer(new CustomerService.UpdateUserCommand(Long.valueOf(id), user.getName(),
                                    user.getDateOfBirth(), user.getEmail(), user.getAddress(), user.getPhone()))))
                    .build());
        } catch (UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.builder()
                    .meta(Meta.builder()
                            .code(Meta.Code.ERROR_USER_NOT_FOUND)
                            .description("Customer not found")
                            .build())
                    .userResponse(null)
                    .build());
        }
    }
}

