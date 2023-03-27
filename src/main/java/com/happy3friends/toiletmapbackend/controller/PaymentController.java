package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.config.OpenApiConfig;
import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.request.PaymentRequest;
import com.happy3friends.toiletmapbackend.response.BaseResponse;
import com.happy3friends.toiletmapbackend.response.PaymentResponse;
import com.happy3friends.toiletmapbackend.service.PaymentService;
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

@Tag(name = "Payment", description = "Payment API")
@RestController
@RequestMapping(value = "/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Operation(summary = "Create payment", description = "Create payment with account ID")
    @Parameter(name = "account-id", description = "A specific account ID", in = ParameterIn.PATH, required = true, example = "4")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Payment Request", required = true, content = @Content(
            examples = {
                    @ExampleObject(name = "Payment Request with Cash Payment Method", value = "{\n" +
                            "  \"total\": 100000,\n" +
                            "  \"method\": \"Tiền mặt\"\n" +
                            "}"),
                    @ExampleObject(name = "Payment Request with VNPAY Payment Method", value = "{\n" +
                            "  \"total\": 100000,\n" +
                            "  \"method\": \"VNPAY\"\n" +
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
    @RolesAllowed({RoleConstant.STAFF, RoleConstant.USER})
    @PostMapping(value = "/{account-id}")
    public ResponseEntity<BaseResponse<PaymentResponse>> createPaymentByAccountId(@PathVariable("account-id") int accountId,
                                                                                @RequestBody @Valid PaymentRequest paymentRequest) {

        PaymentResponse response = paymentService.createPaymentByAccountId(accountId, paymentRequest);

        return ResponseBuilder.generateResponse(
                "Create payment with account-id successfully!",
                HttpStatus.CREATED,
                response
        );
    }

    @Operation(summary = "Payment histories by Account ID", description = "List of payment histories of a specific Account by Account ID")
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
    public ResponseEntity<BaseResponse<List<PaymentResponse>>> getPaymentHistoriesByAccountId(@PathVariable("account-id") int accountId) {

        List<PaymentResponse> responses = paymentService.getPaymentHistoriesByAccountId(accountId);

        if (responses.isEmpty())
            return ResponseBuilder.generateResponse(
                    "List of payment histories by Account ID is empty!",
                    HttpStatus.NO_CONTENT,
                    responses
            );

        return ResponseBuilder.generateResponse(
                "Get list of payment histories by Account ID successfully!",
                HttpStatus.OK,
                responses
        );
    }
}
