package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.base.models.BaseResponse;
import com.happy3friends.toiletmapbackend.config.OpenApiConfig;
import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.request.OrderRequest;
import com.happy3friends.toiletmapbackend.response.OrderResponse;
import com.happy3friends.toiletmapbackend.service.OrderService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@Tag(name = "Order", description = "Order API")
@RestController
@RequestMapping(value = "/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "Create order", description = "[Staff, User] Create order with a specific Account by Account ID")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Order Request", required = true, content = @Content(
            examples = {
                    @ExampleObject(name = "Order Request with AccountBalance Payment Method", value = "{\n" +
                            "  \"accountId\": 6,\n" +
                            "  \"comboId\": 2,\n" +
                            "  \"paymentMethod\": \"Số dư\"\n" +
                            "}"),
                    @ExampleObject(name = "Order Request with VNPAY Payment Method", value = "{\n" +
                            "  \"accountId\": 6,\n" +
                            "  \"comboId\": 2,\n" +
                            "  \"paymentMethod\": \"VNPAY\"\n" +
                            "}"),
                    @ExampleObject(name = "Order Request with Bank Transfer Payment Method", value = "{\n" +
                            "  \"accountId\": 6,\n" +
                            "  \"comboId\": 2,\n" +
                            "  \"paymentMethod\": \"Chuyển khoản\"\n" +
                            "}")}))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"accountId\": 4,\n" +
                            "    \"totalTurn\": 19,\n" +
                            "    \"totalPrice\": 20000,\n" +
                            "    \"paymentMethod\": \"VNPAY\",\n" +
                            "    \"dateTime\": \"20/03/2023 - 13:00:13\"\n" +
                            "  }")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.STAFF, RoleConstant.USER})
    @PostMapping
    public ResponseEntity<BaseResponse<OrderResponse>> createOrderByAccountId(
            @RequestBody @Valid OrderRequest orderRequest) {

        OrderResponse response = orderService.createOrderByAccountId(orderRequest);

        return ResponseBuilder.generateResponse(
                "Create order by account ID successfully!",
                HttpStatus.CREATED,
                response
        );
    }

    @Operation(summary = "Get list of all orders", description = "[User] List of order histories of a specific Account by Account ID")
    @Parameters(value = {
            @Parameter(name = "account-id", description = "A specific Account ID", in = ParameterIn.QUERY, required = true, example = "6"),
            @Parameter(name = "sort",
                    in = ParameterIn.QUERY,
                    description = "Sorting criteria in the format: property(,asc|desc). Default sort order is descending by datetime. Multiple sort criteria are supported.",
                    example ="[\"totalTurn,asc\", \"totalPrice,desc\"]",
                    array = @ArraySchema(schema = @Schema(implementation = String.class), maxItems = 5),
                    allowReserved = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "[\n" +
                            "    {\n" +
                            "      \"totalTurn\": 8,\n" +
                            "      \"totalPrice\": 10000,\n" +
                            "      \"paymentMethod\": \"Số dư\",\n" +
                            "      \"dateTime\": \"20/03/2023 - 12:50:59\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"totalTurn\": 8,\n" +
                            "      \"totalPrice\": 10000,\n" +
                            "      \"paymentMethod\": \"VNPAY\",\n" +
                            "      \"dateTime\": \"10/03/2023 - 02:44:29\"\n" +
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
    public ResponseEntity<BaseResponse<List<OrderResponse>>> getOrderHistoriesByAccountId(
            @RequestParam(value = "account-id") int accountId,
            @ModelAttribute BasePaginationRequest paginationRequest) {

        List<OrderResponse> responses = orderService.getOrderHistoriesByAccountId(accountId, paginationRequest);

        if (responses.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return ResponseBuilder.generateResponse(
                "Get list of order histories by Account ID successfully!",
                HttpStatus.OK,
                responses
        );
    }

    @Operation(summary = "Count list of all orders", description = "[User] Count list of order histories of a specific Account by Account ID")
    @Parameters(value = {
            @Parameter(name = "account-id", description = "A specific Account ID", in = ParameterIn.QUERY, example = "6")
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
    public ResponseEntity<BaseResponse<Integer>> count(@RequestParam(value = "account-id", required = false) Integer accountId) {

        int response = orderService.count(accountId);

        if (accountId != null) {
            return ResponseBuilder.generateResponse(
                    "Count list of order histories by Account ID successfully!",
                    HttpStatus.OK,
                    response
            );
        }

        return ResponseBuilder.generateResponse(
                "Count list of order histories successfully!",
                HttpStatus.OK,
                response
        );
    }
}
