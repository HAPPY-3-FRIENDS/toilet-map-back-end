package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.request.AccountRequest;
import com.happy3friends.toiletmapbackend.response.AccountResponse;
import com.happy3friends.toiletmapbackend.response.BaseResponse;
import com.happy3friends.toiletmapbackend.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Account", description = "Account API")
@RestController
@RequestMapping(value = "/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Operation(summary = "Register an account", description = "Register an account by username (phone), password (optional) and roleName (optional)")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Account Request", required = true, content = @Content(
            examples = {
                    @ExampleObject(name = "Employee Account Request", value = "{\n" +
                            "  \"username\": \"tienhuynh-tn\",\n" +
                            "  \"password\": \"123\",\n" +
                            "  \"roleName\": \"Staff\"" +
                            "}"),
                    @ExampleObject(name = "User Account Request", value = "{\n" +
                            "  \"username\": \"0849666957\",\n" +
                            "  \"password\": null,\n" +
                            "  \"roleName\": null" +
                            "}")
            }))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"username\": \"tienhuynh-tn\",\n" +
                            "    \"status\": \"Đang hoạt động\",\n" +
                            "    \"roleName\": \"Staff\"" +
                            "}")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirements
    @PostMapping
    public ResponseEntity<BaseResponse<AccountResponse>> userCheckIn(@RequestBody AccountRequest accountRequest) {

        AccountResponse response = accountService.createAccount(accountRequest);

        return ResponseBuilder.generateResponse(
                "Create account successfully!",
                HttpStatus.CREATED,
                response
        );
    }
}
