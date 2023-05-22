package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.base.models.BaseResponse;
import com.happy3friends.toiletmapbackend.config.OpenApiConfig;
import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.request.FacilityRequest;
import com.happy3friends.toiletmapbackend.response.FacilityResponse;
import com.happy3friends.toiletmapbackend.service.FacilityService;
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
import java.util.List;

@Tag(name = "Facility", description = "Facility API")
@RestController
@RequestMapping(value = "/api/facilities")
public class FacilityController {

    @Autowired
    private FacilityService facilityService;

    @Operation(summary = "Get list of all facilities", description = "[Admin, Manager] Get list of all facilities")
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
    @RolesAllowed({RoleConstant.ADMIN, RoleConstant.MANAGER})
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

    @Operation(summary = "Get list of facilities by type", description = "[Admin, Manager] Get list of facilities by type")
    @Parameters(value = {
            @Parameter(name = "type", description = "Type of facility", in = ParameterIn.QUERY, required = true, example = "Phòng")
    })
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
    @RolesAllowed({RoleConstant.ADMIN, RoleConstant.MANAGER})
    @GetMapping("/type")
    public ResponseEntity<BaseResponse<List<FacilityResponse>>> getFacilitiesByType(
            @RequestParam(name = "type") String type
    ) {

        List<FacilityResponse> responses = facilityService.getFacilitiesByType(type);

        if (responses.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return ResponseBuilder.generateResponse(
                "Get list of facilities by type successfully!",
                HttpStatus.OK,
                responses
        );
    }

    @Operation(summary = "Create facility", description = "[Admin] create facility")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Facility Request", required = true, content = @Content(
            examples = {
                    @ExampleObject(value = "{\n" +
                            "  \"name\": \"Nước rửa tay\",\n" +
                            "  \"type\": \"Trang thiết bị\"\n" +
                            "}")}))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"id\": 7,\n" +
                            "    \"name\": \"Nước rửa tay\",\n" +
                            "    \"type\": \"Trang thiết bị\"\n" +
                            "  }")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN})
    @PostMapping
    public ResponseEntity<BaseResponse<FacilityResponse>> createFacility(
            @RequestBody FacilityRequest request
    ) {

        FacilityResponse responses = facilityService.createFacility(request);

        return ResponseBuilder.generateResponse(
                "Create facility successfully!",
                HttpStatus.OK,
                responses
        );
    }

    @Operation(summary = "Delete a facility", description = "[Admin] Delete a facility")
    @Parameter(name = "facility-id", description = "A specific facility ID", in = ParameterIn.PATH, required = true, example = "4")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "1")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN})
    @DeleteMapping("/{facility-id}")
    public ResponseEntity<BaseResponse<Integer>> deleteFacility(
            @PathVariable("facility-id") Integer id) {

        facilityService.deleteFacility(id);

        return ResponseBuilder.generateResponse(
                "Delete facility successfully!",
                HttpStatus.OK,
                id
        );
    }
}
