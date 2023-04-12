package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.base.models.BaseResponse;
import com.happy3friends.toiletmapbackend.config.OpenApiConfig;
import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.response.FacilityResponse;
import com.happy3friends.toiletmapbackend.service.FacilityService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Tag(name = "Facility", description = "Facility API")
@RestController
@RequestMapping(value = "/api/facilities")
public class FacilityController {

    @Autowired
    private FacilityService facilityService;

    @Operation(summary = "Get list of all facilities", description = "[Manager] Get list of all facilities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "[\n" +
                            "    {\n" +
                            "      \"id\": 1,\n" +
                            "      \"name\": \"Phòng vệ sinh\",\n" +
                            "      \"type\": \"Phòng\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": 2,\n" +
                            "      \"name\": \"Phòng tắm\",\n" +
                            "      \"type\": \"Phòng\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": 3,\n" +
                            "      \"name\": \"Phòng vệ sinh dành cho người khuyết tật\",\n" +
                            "      \"type\": \"Phòng\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": 4,\n" +
                            "      \"name\": \"Vòi xịt\",\n" +
                            "      \"type\": \"Trang thiết bị\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": 5,\n" +
                            "      \"name\": \"Máy sấy tay\",\n" +
                            "      \"type\": \"Trang thiết bị\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": 6,\n" +
                            "      \"name\": \"Giấy vệ sinh\",\n" +
                            "      \"type\": \"Trang thiết bị\"\n" +
                            "    }\n" +
                            "  ]")})),
            @ApiResponse(responseCode = "204", description = "No Content!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.MANAGER})
    @GetMapping
    public ResponseEntity<BaseResponse<List<FacilityResponse>>> getAllFacilities() {

        List<FacilityResponse> responses = facilityService.getAllFacilities();

        if (responses.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return ResponseBuilder.generateResponse(
                "Get list of all facilities successfully!",
                HttpStatus.OK,
                responses
        );
    }
}
