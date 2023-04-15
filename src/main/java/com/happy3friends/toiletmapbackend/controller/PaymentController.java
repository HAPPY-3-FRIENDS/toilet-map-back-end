package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.base.models.BaseResponse;
import com.happy3friends.toiletmapbackend.config.OpenApiConfig;
import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.request.PaymentRequest;
import com.happy3friends.toiletmapbackend.response.PaymentResponse;
import com.happy3friends.toiletmapbackend.service.PaymentService;
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

@Tag(name = "Payment", description = "Payment API")
@RestController
@RequestMapping(value = "/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Operation(summary = "Create payment", description = "[Staff, Toilet, User] Create payment with account ID")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Payment Request", required = true, content = @Content(
            examples = {
                    @ExampleObject(name = "Payment Request with Cash Payment Method", value = "{\n" +
                            "  \"accountId\": 6,\n" +
                            "  \"total\": 100000,\n" +
                            "  \"method\": \"Tiền mặt\"\n" +
                            "}"),
                    @ExampleObject(name = "Payment Request with VNPAY Payment Method", value = "{\n" +
                            "  \"accountId\": 6,\n" +
                            "  \"total\": 100000,\n" +
                            "  \"method\": \"VNPAY\"\n" +
                            "}"),
                    @ExampleObject(name = "Payment Request with Bank Transfer Payment Method", value = "{\n" +
                            "  \"accountId\": 6,\n" +
                            "  \"total\": 100000,\n" +
                            "  \"method\": \"Chuyển khoản\"\n" +
                            "}")}))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"accountId\": 4,\n" +
                            "    \"total\": 10000,\n" +
                            "    \"method\": \"VNPAY\",\n" +
                            "    \"createdDate\": \"20/03/2023 - 09:17:19\"\n" +
                            "  }")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.STAFF, RoleConstant.TOILET, RoleConstant.USER})
    @PostMapping
    public ResponseEntity<BaseResponse<PaymentResponse>> createPaymentByAccountId(@RequestBody @Valid PaymentRequest paymentRequest) {

        PaymentResponse response = paymentService.createPaymentByAccountId(paymentRequest);

        return ResponseBuilder.generateResponse(
                "Create payment with account-id successfully!",
                HttpStatus.CREATED,
                response
        );
    }

    @Operation(summary = "Get list of all payments", description = "[User] List of payment histories of a specific Account by Account ID")
    @Parameters(value = {
            @Parameter(name = "account-id", description = "A specific Account ID", in = ParameterIn.QUERY, required = true, example = "6"),
            @Parameter(name = "sort",
                    in = ParameterIn.QUERY,
                    description = "Sorting criteria in the format: property(,asc|desc). Default sort order is descending by createdDate. Multiple sort criteria are supported.",
                    example ="[\"total,asc\", \"method,desc\"]",
                    array = @ArraySchema(schema = @Schema(implementation = String.class), maxItems = 5),
                    allowReserved = true)
    })
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
    @GetMapping
    public ResponseEntity<BaseResponse<List<PaymentResponse>>> getPaymentHistoriesByAccountId(
            @RequestParam("account-id") int accountId,
            @ModelAttribute BasePaginationRequest paginationRequest) {

        List<PaymentResponse> responses = paymentService.getPaymentHistoriesByAccountId(accountId, paginationRequest);

        if (responses.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return ResponseBuilder.generateResponse(
                "Get list of payment histories by Account ID successfully!",
                HttpStatus.OK,
                responses
        );
    }

    @Operation(summary = "Count list of all payments", description = "[User] Count list of payment histories of a specific Account by Account ID")
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
    public ResponseEntity<BaseResponse<Integer>> count(
            @RequestParam(name = "account-id", required = false) Integer accountId) {

        int response = paymentService.count(accountId);

        if (accountId != null) {
            return ResponseBuilder.generateResponse(
                    "Count list of payment histories by Account ID successfully!",
                    HttpStatus.OK,
                    response
            );
        }

        return ResponseBuilder.generateResponse(
                "Count list of payment histories successfully!",
                HttpStatus.OK,
                response
        );
    }
}
