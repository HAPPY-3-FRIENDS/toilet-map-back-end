package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.base.models.BaseResponse;
import com.happy3friends.toiletmapbackend.config.OpenApiConfig;
import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.request.CheckInRequest;
import com.happy3friends.toiletmapbackend.request.WalkInGuestCheckInRequest;
import com.happy3friends.toiletmapbackend.response.CheckInResponse;
import com.happy3friends.toiletmapbackend.service.CheckInService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    @Autowired
    SimpMessagingTemplate template;

    @Hidden
    @ConditionalOnExpression("${my.controller.enabled:false}")
    @Operation(summary = "Check-in histories by Toilet ID", description = "[Manager] Get the list of check-in histories of a specific toilet by toilet-id")
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
    @RolesAllowed({RoleConstant.MANAGER})
    @GetMapping(value = "/toilets/{toilet-id}")
    public ResponseEntity<BaseResponse<List<CheckInResponse>>> toiletCheckInHistoriesByToiletId(@PathVariable("toilet-id") int toiletId) {

        List<CheckInResponse> response = checkInService.getCheckInHistoriesByToiletId(toiletId);

        return ResponseBuilder.generateResponse(
                "Get list of check-in histories by toilet-id successfully!",
                HttpStatus.OK,
                response
        );
    }

    @Operation(summary = "Get list of all check-in histories", description = "[Admin, User] List of check-in histories")
    @Parameters(value = {
            @Parameter(name = "account-id", description = "A specific account ID", in = ParameterIn.QUERY, required = false, example = "6"),
            @Parameter(name = "payment-method", description = "A specific payment method", in = ParameterIn.QUERY, examples = {
                    @ExampleObject(name = "Payment method is BALANCE", value = "Số dư"),
                    @ExampleObject(name = "Payment method is TURN", value = "Số lượt"),
                    @ExampleObject(name = "Payment method is BALANCE & TURN", description = "No need to add to query param")
            }),
            @Parameter(name = "sort",
                    in = ParameterIn.QUERY,
                    description = "Sorting criteria in the format: property(,asc|desc). Default sort order is descending by datetime. Multiple sort criteria are supported.",
                    example ="[\"balance,asc\", \"turn,desc\"]",
                    array = @ArraySchema(schema = @Schema(implementation = String.class), maxItems = 5),
                    allowReserved = true)
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
    @GetMapping
    public ResponseEntity<BaseResponse<List<CheckInResponse>>> getCheckInHistoriesByAccountId(
            @RequestParam(name = "account-id", required = false) Integer accountId,
            @RequestParam(name = "payment-method", required = false) String paymentMethod,
            @ModelAttribute BasePaginationRequest paginationRequest) {

        List<CheckInResponse> responses = checkInService.getCheckInHistories(accountId, paymentMethod, paginationRequest);

        if (responses.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return ResponseBuilder.generateResponse(
                "Get list check-in histories by account ID successfully!",
                HttpStatus.OK,
                responses
        );
    }

    @Operation(summary = "Count list of all check-in histories", description = "[User] Count list of check-in histories by Account ID")
    @Parameters(value = {
            @Parameter(name = "account-id", description = "A specific account ID", in = ParameterIn.QUERY, example = "6"),
            @Parameter(name = "payment-method", description = "A specific payment method", in = ParameterIn.QUERY, examples = {
                    @ExampleObject(name = "Payment method is BALANCE", value = "Số dư"),
                    @ExampleObject(name = "Payment method is TURN", value = "Số lượt"),
                    @ExampleObject(name = "Payment method is BALANCE & TURN", description = "No need to add to query param")
            })
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {@ExampleObject(value = "10")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.USER})
    @GetMapping(value = "/count")
    public ResponseEntity<BaseResponse<Integer>> count(
            @RequestParam(name = "account-id", required = false) Integer accountId,
            @RequestParam(name = "payment-method", required = false) String paymentMethod) {

        int response = checkInService.count(accountId, paymentMethod);

        if (accountId != null ) {
            return ResponseBuilder.generateResponse(
                    "Count list check-in histories by account ID successfully!",
                    HttpStatus.OK,
                    response
            );
        }

        return ResponseBuilder.generateResponse(
                "Count list check-in histories successfully!",
                HttpStatus.OK,
                response
        );
    }

    @Operation(summary = "User check-in", description = "[Toilet] User check in a specific toilet")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Check-in Request", required = true, content = @Content(
            examples = {
                    @ExampleObject(name = "Dynamic QR Code", value = "{\n" +
                            "  \"toiletId\": 4,\n" +
                            "  \"accountId\": 6,\n" +
                            "  \"serviceName\": \"Đi vệ sinh (tiểu tiện)\",\n" +
                            "  \"datetime\": \"2023-10-29 10:30:00.123456\"\n" +
                            "}"),
                    @ExampleObject(name = "Static QR Code", value = "{\n" +
                            "  \"toiletId\": 4,\n" +
                            "  \"accountId\": 6\n" +
                            "}")}))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"fullName\": \"Huỳnh Lê Thủy Tiên\",\n" +
                            "    \"defaultPayment\": \"Số lượt\",\n" +
                            "    \"accountBalance\": 50000,\n" +
                            "    \"accountTurn\": 19,\n" +
                            "    \"username\": \"0849666957\"\n" +
                            "}")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.TOILET})
    @PostMapping(value = "/toilets/user")
    public ResponseEntity<BaseResponse<CheckInResponse>> userCheckIn(@RequestBody @Valid CheckInRequest checkInRequest) {

        CheckInResponse response = checkInService.userCheckIn(checkInRequest);

        template.convertAndSend("/topic/check-in", response);

        return ResponseBuilder.generateResponse(
                "User check-in toilet successfully!",
                HttpStatus.CREATED,
                response
        );
    }

    @Operation(summary = "Walk-in-guest check-in", description = "[Toilet] Walk-in-guest check in a specific toilet")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Check-in Request", required = true, content = @Content(
            examples = {
                    @ExampleObject(value = "{\n" +
                            "  \"toiletId\": 4,\n" +
                            "  \"checkInRequests\": [\n" +
                            "    {\n" +
                            "      \"serviceName\": \"Đi vệ sinh (tiểu tiện)\",\n" +
                            "      \"quantity\": 1\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"serviceName\": \"Đi vệ sinh (đại tiện)\",\n" +
                            "      \"quantity\": 2\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}")}))
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
    @RolesAllowed({RoleConstant.TOILET})
    @PostMapping(value = "/toilets/walk-in-guest")
    public ResponseEntity<BaseResponse<List<CheckInResponse>>> walkInGuestCheckIn(
            @RequestBody @Valid WalkInGuestCheckInRequest walkInGuestCheckInRequest) {

        List<CheckInResponse> response = checkInService.walkInGuestCheckIn(walkInGuestCheckInRequest);

        template.convertAndSend("/topic/check-in-for-guest", response);

        return ResponseBuilder.generateResponse(
                "Walk-in-guest check-in toilet successfully!",
                HttpStatus.CREATED,
                response
        );
    }

    @MessageMapping("/check-in")
    @SendTo("/topic/rating")
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.TOILET})
    public ResponseEntity<BaseResponse<CheckInResponse>> userCheckInUsingWebSocket(@RequestBody @Valid CheckInRequest checkInRequest) {

        CheckInResponse response = checkInService.userCheckIn(checkInRequest);

        return ResponseBuilder.generateResponse(
                "User check-in toilet successfully!",
                HttpStatus.CREATED,
                response
        );
    }

    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.USER})
    @GetMapping(value = "/count/check-in-not-rating-yet")
    public ResponseEntity<BaseResponse<Integer>> countCheckInNotRatingYet(
            @RequestParam(name = "account-id", required = false) Integer accountId,
            @RequestParam(name = "payment-method", required = false) String paymentMethod) {
        int response = checkInService.countCheckInNotRatingYet(accountId, paymentMethod);

        return ResponseBuilder.generateResponse(
                "Count list check-in not rating yet histories successfully!",
                HttpStatus.OK,
                response
        );
    }
}
