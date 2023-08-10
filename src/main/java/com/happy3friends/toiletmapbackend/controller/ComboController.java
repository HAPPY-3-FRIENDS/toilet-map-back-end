package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.base.models.BaseResponse;
import com.happy3friends.toiletmapbackend.config.OpenApiConfig;
import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.request.ComboRequest;
import com.happy3friends.toiletmapbackend.response.ComboResponse;
import com.happy3friends.toiletmapbackend.service.ComboService;
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

@Tag(name = "Combo", description = "Combo API")
@RestController
@RequestMapping(value = "/api/combos")
public class ComboController {

    @Autowired
    private ComboService comboService;

    @Operation(summary = "Get all combos", description = "[Admin, Staff, User] Get the list of all combos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "[\n" +
                            "    {\n" +
                            "      \"id\": 1,\n" +
                            "      \"totalTurn\": 8,\n" +
                            "      \"price\": 10000\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": 2,\n" +
                            "      \"totalTurn\": 19,\n" +
                            "      \"price\": 20000\n" +
                            "    }\n" +
                            "  ]")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN, RoleConstant.STAFF, RoleConstant.USER})
    @GetMapping
    public ResponseEntity<BaseResponse<List<ComboResponse>>> getAllCombo() {
        List<ComboResponse> responses = comboService.getAllCombo();

        return ResponseBuilder.generateResponse(
                "Get list of all combos successfully!",
                HttpStatus.OK,
                responses
        );
    }

    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN})
    @GetMapping(value = "/{combo-id}")
    public ResponseEntity<BaseResponse<ComboResponse>> getComboByComboId(@PathVariable("combo-id") int comboId) {

        ComboResponse response = comboService.getComboIdByComboId(comboId);

        return ResponseBuilder.generateResponse(
                "Get combo by combo ID successfully!",
                HttpStatus.OK,
                response
        );
    }

    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN})
    @PutMapping(value = "/{combo-id}")
    public ResponseEntity<BaseResponse<ComboResponse>> updateComboByComboId(
            @PathVariable("combo-id") int comboId,
            @RequestBody ComboRequest comboRequest) {

        ComboResponse response = comboService.updateComboByComboId(comboId, comboRequest);

        return ResponseBuilder.generateResponse(
                "Update combo by comboId successfully!",
                HttpStatus.OK,
                response
        );
    }

    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN})
    @PostMapping
    public ResponseEntity<BaseResponse<ComboResponse>> createCombo(@RequestBody ComboRequest comboRequest) {

        ComboResponse response = comboService.createCombo(comboRequest);

        return ResponseBuilder.generateResponse(
                "Creat combo successfully!",
                HttpStatus.CREATED,
                response
        );
    }
}
