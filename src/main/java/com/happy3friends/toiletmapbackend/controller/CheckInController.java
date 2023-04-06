package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.config.OpenApiConfig;
import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.request.CheckInRequest;
import com.happy3friends.toiletmapbackend.response.BaseResponse;
import com.happy3friends.toiletmapbackend.response.CheckInResponse;
import com.happy3friends.toiletmapbackend.service.CheckInService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@Tag(name = "Check-in", description = "Check-in API")
@RestController
@RequestMapping(value = "/api/check-in")
public class CheckInController {

    @Autowired
    private CheckInService checkInService;

    @Operation(summary = "Check-in histories by Toilet ID", description = "[Admin, Manager] Get the list of check-in histories of a specific toilet by toilet-id")
    @Parameter(name = "toilet-id", description = "A specific toilet ID", in = ParameterIn.PATH, required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"fullName\": \"Huỳnh Lê Thủy Tiên\",\n" +
                            "    \"dateTime\": \"29/10/2001 - 10:30:00\",\n" +
                            "    \"serviceName\": \"Đi vệ sinh (Đại tiện)\",\n" +
                            "    \"paymentMethod\": \"Số dư\",\n" +
                            "    \"balance\": 4000,\n" +
                            "    \"turn\": null\n" +
                            "}")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN, RoleConstant.MANAGER})
    @GetMapping(value = "/toilets/{toilet-id}")
    public ResponseEntity<BaseResponse<List<CheckInResponse>>> toiletCheckInHistoriesByToiletId(@PathVariable("toilet-id") int toiletId) {

        List<CheckInResponse> response = checkInService.getCheckInHistoriesByToiletId(toiletId);

        return ResponseBuilder.generateResponse(
                "Get list of check-in histories by toilet-id successfully!",
                HttpStatus.OK,
                response
        );
    }

    @Operation(summary = "Check-in histories by Account ID", description = "[User] List of check-in histories by Account ID")
    @Parameters(value = {
            @Parameter(name = "account-id", description = "A specific account ID", in = ParameterIn.PATH, required = true, example = "4"),
            @Parameter(name = "payment-method", description = "A specific payment method", in = ParameterIn.QUERY, examples = {
                    @ExampleObject(name = "Payment method is BALANCE", value = "Số dư"),
                    @ExampleObject(name = "Payment method is TURN", value = "Số lượt"),
                    @ExampleObject(name = "Payment method is BALANCE & TURN", description = "No need to add to query param")
            })
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "[\n" +
                            "    {\n" +
                            "      \"dateTime\": \"24/03/2023 - 09:30:05\",\n" +
                            "      \"serviceName\": \"Đi vệ sinh (tiểu tiện)\",\n" +
                            "      \"balance\": 2000,\n" +
                            "      \"turn\": null,\n" +
                            "      \"toiletName\": \"Nhà vệ sinh lưu động số 1\",\n" +
                            "      \"toiletId\": 1\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"dateTime\": \"24/03/2023 - 09:30:05\",\n" +
                            "      \"serviceName\": \"Đi vệ sinh (tiểu tiện)\",\n" +
                            "      \"balance\": 2000,\n" +
                            "      \"turn\": null,\n" +
                            "      \"toiletName\": \"Nhà vệ sinh lưu động số 1\",\n" +
                            "      \"toiletId\": 1\n" +
                            "    }\n" +
                            "]")})),
            @ApiResponse(responseCode = "204", description = "No Content!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.USER})
    @GetMapping(value = "/accounts/{account-id}")
    public ResponseEntity<BaseResponse<List<CheckInResponse>>> getCheckInHistoriesByAccountId(
            @PathVariable(value = "account-id") int accountId,
            @RequestParam(name = "payment-method", required = false) String paymentMethod) {

        List<CheckInResponse> responses = checkInService.getCheckInHistoriesByAccountId(accountId, paymentMethod);

        if (responses.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return ResponseBuilder.generateResponse(
                "Get list check-in histories by account ID successfully!",
                HttpStatus.OK,
                responses
        );
    }

    @Operation(summary = "User check-in", description = "[Staff] User check in a specific toilet")
    @Parameter(name = "toilet-id", description = "A specific toilet ID", in = ParameterIn.PATH, required = true, example = "1")
    @Parameter(name = "account-id", description = "A specific account ID (User)", in = ParameterIn.PATH, required = true, example = "4")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Check-in Request", required = true, content = @Content(
            examples = {
                    @ExampleObject(value = "{\n" +
                            "  \"serviceName\": \"Đi vệ sinh (tiểu tiện)\",\n" +
                            "  \"datetime\": \"2023-10-29 10:30:00.123456\"\n" +
                            "}")}))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"fullName\": \"Huỳnh Lê Thủy Tiên\",\n" +
                            "    \"dateTime\": \"29/10/2001 - 10:30:00\",\n" +
                            "    \"serviceName\": \"Đi vệ sinh (Đại tiện)\",\n" +
                            "    \"paymentMethod\": \"Số dư\",\n" +
                            "    \"balance\": 4000,\n" +
                            "    \"turn\": null\n" +
                            "}")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.STAFF})
    @PostMapping(value = "/toilets/{toilet-id}/accounts/{account-id}/user")
    public ResponseEntity<BaseResponse<CheckInResponse>> userCheckIn(
            @PathVariable("toilet-id") int toiletId,
            @PathVariable("account-id") int accountId,
            @RequestBody @Valid CheckInRequest checkInRequest) {

        CheckInResponse response = checkInService.userCheckIn(toiletId, accountId, checkInRequest);

        return ResponseBuilder.generateResponse(
                "User check-in toilet successfully!",
                HttpStatus.CREATED,
                response
        );
    }

    @Operation(summary = "Walk-in-guest check-in", description = "[Staff] Walk-in-guest check in a specific toilet")
    @Parameter(name = "toilet-id", description = "A specific toilet ID", in = ParameterIn.PATH, required = true, example = "1")
    @Parameter(name = "account-id", description = "A specific account ID (Staff)", in = ParameterIn.PATH, required = true, example = "3")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Check-in Request", required = true, content = @Content(
            examples = {
                    @ExampleObject(value = "[\n" +
                            "  {\n" +
                            "    \"serviceName\": \"Đi vệ sinh (tiểu tiện)\",\n" +
                            "    \"quantity\": 1\n" +
                            "  },\n" +
                            "  {\n" +
                            "    \"serviceName\": \"Đi vệ sinh (đại tiện)\",\n" +
                            "    \"quantity\": 2\n" +
                            "  }\n" +
                            "]")}))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "[\n" +
                            "    {\n" +
                            "      \"fullName\": \"Khách vãng lai\",\n" +
                            "      \"dateTime\": \"06/04/2023 - 16:25:32\",\n" +
                            "      \"serviceName\": \"Đi vệ sinh (tiểu tiện)\",\n" +
                            "      \"paymentMethod\": \"Tiền mặt\",\n" +
                            "      \"balance\": 2000,\n" +
                            "      \"turn\": null\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"fullName\": \"Khách vãng lai\",\n" +
                            "      \"dateTime\": \"06/04/2023 - 16:25:32\",\n" +
                            "      \"serviceName\": \"Đi vệ sinh (đại tiện)\",\n" +
                            "      \"paymentMethod\": \"Tiền mặt\",\n" +
                            "      \"balance\": 4000,\n" +
                            "      \"turn\": null\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"fullName\": \"Khách vãng lai\",\n" +
                            "      \"dateTime\": \"06/04/2023 - 16:25:32\",\n" +
                            "      \"serviceName\": \"Đi vệ sinh (đại tiện)\",\n" +
                            "      \"paymentMethod\": \"Tiền mặt\",\n" +
                            "      \"balance\": 4000,\n" +
                            "      \"turn\": null\n" +
                            "    }\n" +
                            "  ]")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.STAFF})
    @PostMapping(value = "/toilets/{toilet-id}/accounts/{account-id}/walk-in-guest")
    public ResponseEntity<BaseResponse<List<CheckInResponse>>> walkInGuestCheckIn(
            @PathVariable("toilet-id") int toiletId,
            @PathVariable("account-id") int accountId,
            @RequestBody @Valid List<CheckInRequest> checkInRequests) {

        List<CheckInResponse> response = checkInService.walkInGuestCheckIn(toiletId, accountId, checkInRequests);

        return ResponseBuilder.generateResponse(
                "Walk-in-guest check-in toilet successfully!",
                HttpStatus.CREATED,
                response
        );
    }
}
