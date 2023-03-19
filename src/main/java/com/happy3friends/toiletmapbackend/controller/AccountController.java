package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.config.OpenApiConfig;
import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.dto.TokenDTO;
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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@Tag(name = "Account", description = "Account API")
@RestController
@RequestMapping(value = "/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Operation(summary = "Register an account for an employee", description = "Register an account by username (phone), password, roleName and companyId")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Account Request", required = true, content = @Content(
            examples = {
                    @ExampleObject(name = "Manager Account Request", value = "{\n" +
                            "  \"username\": \"tienhuynh-tn\",\n" +
                            "  \"password\": \"123\",\n" +
                            "  \"roleName\": \"Manager\"," +
                            "  \"companyId\": 1" +
                            "}"),
                    @ExampleObject(name = "Staff Account Request", value = "{\n" +
                            "  \"username\": \"tienhuynh-tn\",\n" +
                            "  \"password\": \"123\",\n" +
                            "  \"roleName\": \"Staff\"," +
                            "  \"companyId\": 1" +
                            "}")}))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"username\": \"tienhuynh-tn\",\n" +
                            "    \"status\": \"Đang hoạt động\",\n" +
                            "    \"roleName\": \"Staff\"" +
                            "}")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN, RoleConstant.MANAGER})
    @PostMapping("/employee")
    public ResponseEntity<BaseResponse<AccountResponse>> registerEmployee(@RequestBody AccountRequest accountRequest) {

        AccountResponse response = accountService.registerEmployee(accountRequest);

        return ResponseBuilder.generateResponse(
                "Create account for employee successfully!",
                HttpStatus.CREATED,
                response
        );
    }

    @Operation(summary = "Register an account for a user", description = "Register an account by username (phone) and fullName")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Account Request", required = true, content = @Content(
            examples = {
                    @ExampleObject(value = "{\n" +
                            "  \"username\": \"0849666957\",\n" +
                            "  \"fullName\": \"Huỳnh Lê Thủy Tiên\"" +
                            "}")}))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"username\": \"0849666957\",\n" +
                            "    \"fullName\": \"Huỳnh Lê Thủy Tiên\",\n" +
                            "    \"status\": \"Đang hoạt động\",\n" +
                            "    \"defaultPayment\": \"Số dư\",\n" +
                            "    \"roleName\": \"User\"" +
                            "}")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirements
    @PostMapping("/user")
    public ResponseEntity<BaseResponse<TokenDTO>> registerUser(@RequestBody AccountRequest accountRequest) {

        TokenDTO response = accountService.registerUser(accountRequest);

        return ResponseBuilder.generateResponse(
                "Create account for user successfully!",
                HttpStatus.CREATED,
                response
        );
    }
}
