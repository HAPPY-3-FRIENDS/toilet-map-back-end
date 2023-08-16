package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.base.models.BaseResponse;
import com.happy3friends.toiletmapbackend.config.OpenApiConfig;
import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.dto.ServiceDTO;
import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.response.ServiceResponse;
import com.happy3friends.toiletmapbackend.service.ServiceService;
import io.swagger.v3.oas.annotations.Operation;
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
import java.util.List;

@Tag(name = "Service", description = "Service API")
@RestController
@RequestMapping(value = "/api/services")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    @Operation(summary = "Get all services", description = "[Admin, User] Get the list of all services")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "[\n" +
                            "    {\n" +
                            "      \"id\": 1,\n" +
                            "      \"name\": \"Đi vệ sinh (tiểu tiện)\",\n" +
                            "      \"price\": 5000,\n" +
                            "      \"turn\": 1,\n" +
                            "      \"turnPrice\": 3000\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": 2,\n" +
                            "      \"name\": \"Đi vệ sinh (đại tiện)\",\n" +
                            "      \"price\": 10000,\n" +
                            "      \"turn\": 2,\n" +
                            "      \"turnPrice\": 6000\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": 3,\n" +
                            "      \"name\": \"Đi tắm\",\n" +
                            "      \"price\": 15000,\n" +
                            "      \"turn\": 3,\n" +
                            "      \"turnPrice\": 9000\n" +
                            "    }\n" +
                            "  ]")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN, RoleConstant.USER})
    @GetMapping
    public ResponseEntity<BaseResponse<List<ServiceResponse>>> getAllCombo() {
        List<ServiceResponse> responses = serviceService.getAllService();

        return ResponseBuilder.generateResponse(
                "Get list of all services successfully!",
                HttpStatus.OK,
                responses
        );
    }

    @Operation(summary = "Update batch services", description = "[Admin] Update batch services")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Batch Services Request", required = true, content = @Content(
            examples = {
                    @ExampleObject(value = "[\n" +
                            "  {\n" +
                            "    \"id\": 1,\n" +
                            "    \"price\": 7000\n" +
                            "  },\n" +
                            "  {\n" +
                            "    \"id\": 2,\n" +
                            "    \"price\": 14000\n" +
                            "  }\n" +
                            "]")}))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN})
    @PostMapping(value = "/batch-update")
    public ResponseEntity<BaseResponse<List<ServiceResponse>>> updateBatchServices(@RequestBody List<ServiceDTO> serviceDTOS) {
        serviceService.updateBatchServices(serviceDTOS);

        return ResponseBuilder.generateResponse(
                "Update batch services successfully!",
                HttpStatus.OK,
                null
        );
    }

    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN})
    @GetMapping("/turn-price")
    public ResponseEntity<BaseResponse<Integer>> getTurnPrice() {

        int turnPrice = serviceService.getTurnPrice();

        return ResponseBuilder.generateResponse(
                "Get turn price successfully!",
                HttpStatus.OK,
                turnPrice
        );
    }

    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN})
    @PutMapping
    public ResponseEntity<BaseResponse<Integer>> updateTurnPrice(@RequestParam int turnPrice) {

        serviceService.updateTurnPrice(turnPrice);

        return ResponseBuilder.generateResponse(
                "Update turn price successfully!",
                HttpStatus.OK,
                null
        );
    }
}
