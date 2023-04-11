package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.base.models.BaseResponse;
import com.happy3friends.toiletmapbackend.config.OpenApiConfig;
import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.response.ToiletServiceResponse;
import com.happy3friends.toiletmapbackend.service.ToiletServiceService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Tag(name = "Toilet Services", description = "Toilet Services API")
@RestController
@RequestMapping(value = "/api/toilet-services")
public class ToiletServiceController {

    @Autowired
    private ToiletServiceService toiletServiceService;

    @Operation(summary = "Get list of toilet's services",
            description = "- [Toilet] Get list of all toilet's services by Toilet ID")
    @Parameters(value = {
            @Parameter(name = "toilet-id", description = "A specific toilet ID", in = ParameterIn.QUERY, required = true, example = "4")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "[\n" +
                            "    {\n" +
                            "      \"id\": 1,\n" +
                            "      \"toiletId\": 4,\n" +
                            "      \"service\": {\n" +
                            "        \"id\": 1,\n" +
                            "        \"name\": \"Đi vệ sinh (tiểu tiện)\",\n" +
                            "        \"price\": 2000,\n" +
                            "        \"turn\": 1\n" +
                            "      }\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": 2,\n" +
                            "      \"toiletId\": 4,\n" +
                            "      \"service\": {\n" +
                            "        \"id\": 2,\n" +
                            "        \"name\": \"Đi vệ sinh (đại tiện)\",\n" +
                            "        \"price\": 4000,\n" +
                            "        \"turn\": 2\n" +
                            "      }\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": 3,\n" +
                            "      \"toiletId\": 4,\n" +
                            "      \"service\": {\n" +
                            "        \"id\": 3,\n" +
                            "        \"name\": \"Đi tắm\",\n" +
                            "        \"price\": 8000,\n" +
                            "        \"turn\": 3\n" +
                            "      }\n" +
                            "    }\n" +
                            "  ]")
            })),
            @ApiResponse(responseCode = "204", description = "No-content!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.TOILET})
    @GetMapping
    public ResponseEntity<BaseResponse<List<ToiletServiceResponse>>> getToiletServicesByToiletId(
            @RequestParam(name = "toilet-id") int toiletId) {

        List<ToiletServiceResponse> responses = toiletServiceService.getToiletServicesByToiletId(toiletId);

        if (responses.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return ResponseBuilder.generateResponse(
                "Get list of toilet's services by Toilet ID successfully!",
                HttpStatus.OK,
                responses
        );
    }
}
