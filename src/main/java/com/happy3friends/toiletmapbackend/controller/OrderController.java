package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.config.OpenApiConfig;
import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.request.OrderRequest;
import com.happy3friends.toiletmapbackend.response.BaseResponse;
import com.happy3friends.toiletmapbackend.response.OrderResponse;
import com.happy3friends.toiletmapbackend.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@Tag(name = "Order", description = "Order API")
@RestController
@RequestMapping(value = "/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "Create order", description = "[User] Create order with account ID")
    @Parameter(name = "account-id", description = "A specific account ID", in = ParameterIn.PATH, required = true, example = "4")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Order Request", required = true, content = @Content(
            examples = {
                    @ExampleObject(name = "Order Request with AccountBalance Payment Method", value = "{\n" +
                            "  \"comboId\": 2,\n" +
                            "  \"paymentMethod\": \"Số dư\"\n" +
                            "}"),
                    @ExampleObject(name = "Order Request with VNPAY Payment Method", value = "{\n" +
                            "  \"comboId\": 2,\n" +
                            "  \"paymentMethod\": \"VNPAY\"\n" +
                            "}")}))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"accountId\": 4,\n" +
                            "    \"comboId\": 2,\n" +
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
    @RolesAllowed({RoleConstant.USER})
    @PostMapping(value = "/{account-id}")
    public ResponseEntity<BaseResponse<OrderResponse>> createOrderByAccountId(
            @PathVariable("account-id") int accountId,
            @RequestBody @Valid OrderRequest orderRequest) {

        OrderResponse response = orderService.createOrderByAccountId(accountId, orderRequest);

        return ResponseBuilder.generateResponse(
                "Create order by account ID successfully!",
                HttpStatus.CREATED,
                response
        );
    }

    @Operation(summary = "Order histories by Account ID", description = "[User] List of order histories of a specific Account by Account ID")
    @Parameter(name = "account-id", description = "A specific Account ID", in = ParameterIn.PATH, required = true, example = "4")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "[\n" +
                            "    {\n" +
                            "      \"total\": 70000,\n" +
                            "      \"method\": \"VNPAY\",\n" +
                            "      \"createdDate\": \"20/03/2023 - 09:17:19\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"total\": 70000,\n" +
                            "      \"method\": \"VNPAY\",\n" +
                            "      \"createdDate\": \"20/03/2023 - 09:15:36\"\n" +
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
    @GetMapping(value = "/{account-id}")
    public ResponseEntity<BaseResponse<List<OrderResponse>>> getOrderHistoriesByAccountId(@PathVariable("account-id") int accountId) {

        List<OrderResponse> responses = orderService.getOrderHistoriesByAccountId(accountId);

        if (responses.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return ResponseBuilder.generateResponse(
                "Get list of order histories by Account ID successfully!",
                HttpStatus.OK,
                responses
        );
    }
}
