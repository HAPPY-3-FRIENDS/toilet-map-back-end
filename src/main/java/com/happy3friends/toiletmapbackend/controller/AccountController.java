package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.base.models.BaseResponse;
import com.happy3friends.toiletmapbackend.config.OpenApiConfig;
import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.dto.TokenDTO;
import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.request.AccountRequest;
import com.happy3friends.toiletmapbackend.request.UpdatePasswordRequest;
import com.happy3friends.toiletmapbackend.response.AccountResponse;
import com.happy3friends.toiletmapbackend.response.UpdateAccountResponse;
import com.happy3friends.toiletmapbackend.response.UserInfoResponse;
import com.happy3friends.toiletmapbackend.service.AccountService;
import com.happy3friends.toiletmapbackend.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.Map;

@Tag(name = "Account", description = "Account API")
@RestController
@RequestMapping(value = "/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserInfoService userInfoService;

    @Operation(summary = "Register an account for an employee", description = "[Admin, Manager] Register an account by username (phone), password, roleName and companyId")
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
    @PostMapping(value = "/employee")
    public ResponseEntity<BaseResponse<AccountResponse>> registerEmployee(@RequestBody @Valid AccountRequest accountRequest) {

        AccountResponse response = accountService.registerEmployee(accountRequest);

        return ResponseBuilder.generateResponse(
                "Create account for employee successfully!",
                HttpStatus.CREATED,
                response
        );
    }

    @Operation(summary = "Register an account for a user", description = "[All] Register an account by username (phone) and fullName")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Account Request", required = true, content = @Content(
            examples = {
                    @ExampleObject(value = "{\n" +
                            "  \"username\": \"0849666957\",\n" +
                            "  \"fullName\": \"Huỳnh Lê Thủy Tiên\"" +
                            "}")}))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"accessToken\": \"token\",\n" +
                            "    \"tokenType\": \"Bearer\"" +
                            "}")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirements
    @PostMapping(value = "/user")
    public ResponseEntity<BaseResponse<TokenDTO>> registerUser(@RequestBody @Valid AccountRequest accountRequest) {

        TokenDTO response = accountService.registerUser(accountRequest);

        return ResponseBuilder.generateResponse(
                "Create account for user successfully!",
                HttpStatus.CREATED,
                response
        );
    }

    @Operation(summary = "Update user info of a user", description = "[User] Update one or many fields in user info of a user by Account ID")
    @Parameter(name = "account-id", description = "A specific account ID", in = ParameterIn.PATH, required = true, example = "4")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Fields Request", required = true, content = @Content(
            examples = {
                    @ExampleObject(name = "One field", value = "{\n" +
                            "  \"fullName\": \"Huỳnh Lê Thủy Tiên\"\n" +
                            "}"),
                    @ExampleObject(name = "Many fields", value = "{\n" +
                            "  \"fullName\": \"Huỳnh Lê Thủy Tiên\",\n" +
                            "  \"defaultPayment\": null\n" +
                            "}")}))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"accountId\": 4,\n" +
                            "    \"fullName\": \"Huỳnh Lê Thủy Tiên\",\n" +
                            "    \"gmail\": \"tien.huynhlt.tn@gmail.com\",\n" +
                            "    \"avatar\": \"https://scontent.fsgn19-1.fna.fbcdn.net/v/t39.30808-6/272908202_3227262997503338_854943145488623253_n.jpg?_nc_cat=105&ccb=1-7&_nc_sid=174925&_nc_ohc=DsyBnrzVM54AX_UwxS-&_nc_ht=scontent.fsgn19-1.fna&oh=00_AfBWPT-ZIevQvgZ9zUBRhFcVeKZxBWFbyvYSh7QBDP36uQ&oe=641EEE84\",\n" +
                            "    \"defaultPayment\": \"Số lượt\",\n" +
                            "    \"accountBalance\": 25000,\n" +
                            "    \"accountTurn\": 24\n" +
                            "  }")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.USER})
    @PatchMapping(value = "/{account-id}/user-info")
    public ResponseEntity<BaseResponse<UserInfoResponse>> updateUserInfoByAccountId(
            @PathVariable("account-id") int accountId,
            @RequestBody Map<String, Object> fields) {

        UserInfoResponse response = userInfoService.updateUserInfoByFieldsAndAccountId(accountId, fields);

        return ResponseBuilder.generateResponse(
                "Update user info by Account ID successfully!",
                HttpStatus.OK,
                response
        );
    }

    @Operation(summary = "Get user infos of a user", description = "[User] Get user infos of a user by Account ID")
    @Parameter(name = "account-id", description = "A specific account ID", in = ParameterIn.PATH, required = true, example = "4")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"accountId\": 4,\n" +
                            "    \"fullName\": \"Huỳnh Lê Thủy Tiên\",\n" +
                            "    \"gmail\": \"tien.huynhlt.tn@gmail.com\",\n" +
                            "    \"avatar\": \"https://scontent.fsgn19-1.fna.fbcdn.net/v/t39.30808-6/272908202_3227262997503338_854943145488623253_n.jpg?_nc_cat=105&ccb=1-7&_nc_sid=174925&_nc_ohc=DsyBnrzVM54AX_UwxS-&_nc_ht=scontent.fsgn19-1.fna&oh=00_AfBWPT-ZIevQvgZ9zUBRhFcVeKZxBWFbyvYSh7QBDP36uQ&oe=641EEE84\",\n" +
                            "    \"defaultPayment\": \"Số lượt\",\n" +
                            "    \"accountBalance\": 25000,\n" +
                            "    \"accountTurn\": 24\n" +
                            "  }")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.USER})
    @GetMapping(value = "/{account-id}/user-infos")
    public ResponseEntity<BaseResponse<UserInfoResponse>> getUserInfoByAccountId(@PathVariable("account-id") int accountId) {

        UserInfoResponse response = userInfoService.getUserInfoAccountId(accountId);

        return ResponseBuilder.generateResponse(
                "Get user info by Account ID successfully!",
                HttpStatus.OK,
                response
        );
    }

    @Operation(summary = "Get user infos of a user", description = "[Manager, Staff, Toilet, User] Get user infos of a user by Account username")
    @Parameter(name = "account-username", description = "A specific account username", in = ParameterIn.QUERY, required = true, example = "0849666957")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"accountId\": 4,\n" +
                            "    \"fullName\": \"Huỳnh Lê Thủy Tiên\",\n" +
                            "    \"gmail\": \"tien.huynhlt.tn@gmail.com\",\n" +
                            "    \"avatar\": \"https://scontent.fsgn19-1.fna.fbcdn.net/v/t39.30808-6/272908202_3227262997503338_854943145488623253_n.jpg?_nc_cat=105&ccb=1-7&_nc_sid=174925&_nc_ohc=DsyBnrzVM54AX_UwxS-&_nc_ht=scontent.fsgn19-1.fna&oh=00_AfBWPT-ZIevQvgZ9zUBRhFcVeKZxBWFbyvYSh7QBDP36uQ&oe=641EEE84\",\n" +
                            "    \"defaultPayment\": \"Số lượt\",\n" +
                            "    \"accountBalance\": 25000,\n" +
                            "    \"accountTurn\": 24\n" +
                            "  }")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.MANAGER, RoleConstant.STAFF, RoleConstant.TOILET, RoleConstant.USER})
    @GetMapping(value = "/user-infos")
    public ResponseEntity<BaseResponse<UserInfoResponse>> getUserInfoByAccountUsername(@RequestParam(value = "account-username") String accountUsername) {

        UserInfoResponse response = userInfoService.getUserInfoByAccountUsername(accountUsername);

        return ResponseBuilder.generateResponse(
                "Get user info by Account username successfully!",
                HttpStatus.OK,
                response
        );
    }

    @Operation(summary = "Update an account", description = "[User] Update an account and its information")
    @Parameter(name = "id", description = "A specific account ID", in = ParameterIn.PATH, required = true, example = "6")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Fields Request", required = true, content = @Content(
            examples = {
                    @ExampleObject(name = "Update username", value = "{\n" +
                            "  \"username\": \"0849666957\" \n" +
                            "}"),
                    @ExampleObject(name = "Update status", value = "{\n" +
                            "  \"status\": \"Đang hoạt động\" \n" +
                            "}")}))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"username\": \"0849666957\",\n" +
                            "    \"password\": null,\n" +
                            "    \"status\": \"Đang hoạt động\"\n" +
                            "  }")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.USER})
    @PatchMapping(value = "/{id}")
    public ResponseEntity<BaseResponse<UpdateAccountResponse>> updateAccountById(
            @PathVariable("id") int id,
            @RequestBody Map<String, Object> fields) {

        UpdateAccountResponse response = accountService.updateAccount(id, fields);

        return ResponseBuilder.generateResponse(
                "Update account by ID successfully!",
                HttpStatus.OK,
                response
        );
    }

    @Operation(summary = "Update password", description = "[Manager, Staff, Toilet] Update password")
    @Parameter(name = "id", description = "A specific account ID", in = ParameterIn.PATH, required = true, example = "39")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Update password request", required = true, content = @Content(
            examples = {
                    @ExampleObject(name = "Update password", value = "{\n" +
                            "  \"oldPassword\": \"$2a$10$/dH1LFY1VeSe9aQoibV8puAthiOjM/7Cb0NwnDfSA40wUxnagEkRG\",\n" +
                            "  \"newPassword\": \"12345\"\n" +
                            "}")}))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"username\": \"manager-hieu\",\n" +
                            "    \"password\": $2a$10$/dH1LFY1VeSe9aQoibV8puAthiOjM/7Cb0NwnDfSA40wUxnagEkRG,\n" +
                            "    \"status\": \"Đang hoạt động\"\n" +
                            "  }")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.MANAGER, RoleConstant.STAFF, RoleConstant.TOILET})
    @PutMapping(value = "/password/{id}")
    public ResponseEntity<BaseResponse<UpdateAccountResponse>> updatePassword(
            @PathVariable("id") int id,
            @RequestBody UpdatePasswordRequest request) {

        UpdateAccountResponse response = accountService.updatePassword(id, request);

        return ResponseBuilder.generateResponse(
                "Update password successfully!",
                HttpStatus.OK,
                response
        );
    }
}
