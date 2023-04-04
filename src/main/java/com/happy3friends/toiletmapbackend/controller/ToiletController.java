package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.config.OpenApiConfig;
import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.response.BaseResponse;
import com.happy3friends.toiletmapbackend.response.ToiletDetailsInfoResponse;
import com.happy3friends.toiletmapbackend.service.ToiletService;
import io.swagger.v3.oas.annotations.Hidden;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Tag(name = "Toilet", description = "Toilet API")
@RestController
@RequestMapping(value = "/api/toilets")
public class ToiletController {

    @Autowired
    private ToiletService toiletService;

    @Operation(summary = "Get toilet by account ID", description = "Get a specific toilet by account ID")
    @Parameter(name = "account-id", description = "A specific account ID", in = ParameterIn.PATH, required = true, example = "3")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"id\": 1,\n" +
                            "    \"toiletName\": \"Nhà vệ sinh lưu động số 1\",\n" +
                            "    \"address\": \"44 Trần Đình Xu\",\n" +
                            "    \"ward\": \"Phường Cô Giang\",\n" +
                            "    \"district\": \"Quận 1\",\n" +
                            "    \"province\": \"Thành phố Hồ Chí Minh\",\n" +
                            "    \"longitude\": 10.759935271800982,\n" +
                            "    \"latitude\": 106.69202149316303,\n" +
                            "    \"nearBy\": \"Gần CircleK, gần Phúc Long\",\n" +
                            "    \"openTime\": \"09:00:00\",\n" +
                            "    \"closeTime\": \"23:00:00\",\n" +
                            "    \"minPrice\": 2000,\n" +
                            "    \"maxPrice\": 8000,\n" +
                            "    \"toiletFacilityDTOS\": [\n" +
                            "      {\n" +
                            "        \"facilityName\": \"Giấy vệ sinh\",\n" +
                            "        \"quantity\": 1,\n" +
                            "        \"description\": null\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"facilityName\": \"Máy sấy tay\",\n" +
                            "        \"quantity\": 0,\n" +
                            "        \"description\": null\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"facilityName\": \"Phòng tắm\",\n" +
                            "        \"quantity\": 0,\n" +
                            "        \"description\": null\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"facilityName\": \"Phòng vệ sinh\",\n" +
                            "        \"quantity\": 8,\n" +
                            "        \"description\": \"4 phòng vệ sinh cho nữ, 4 phòng vệ sinh cho nam\"\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"facilityName\": \"Phòng vệ sinh dành cho người khuyết tật\",\n" +
                            "        \"quantity\": 1,\n" +
                            "        \"description\": null\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"facilityName\": \"Vòi xịt\",\n" +
                            "        \"quantity\": 1,\n" +
                            "        \"description\": null\n" +
                            "      }\n" +
                            "    ],\n" +
                            "    \"toiletImageSources\": [\n" +
                            "      \"https://dichvucongichquan1.com/wp-content/uploads/2021/04/z2469130019572_1b2874d47ba76fa3b7089d0ffa4b72c7.jpg\",\n" +
                            "      \"https://dichvucongichquan1.com/wp-content/uploads/2021/04/z2469130681021_b9303b13544929365e1810b07c7e3dff.jpg\"\n" +
                            "    ],\n" +
                            "    \"ratingStar\": 3.8,\n" +
                            "    \"free\": false\n" +
                            "  }")
            })),
            @ApiResponse(responseCode = "204", description = "No-content!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.MANAGER, RoleConstant.STAFF})
    @GetMapping(value = "/accounts/{account-id}")
    public ResponseEntity<BaseResponse<ToiletDetailsInfoResponse>> getToiletByAccountId(@PathVariable("account-id") int accountId) {

        ToiletDetailsInfoResponse response = toiletService.getToiletByAccountId(accountId);
        if (response == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return ResponseBuilder.generateResponse(
                "Get toilet by account ID successfully!",
                HttpStatus.OK,
                response
        );
    }

    @Operation(summary = "Get toilet by toilet ID", description = "Get a specific toilet by toilet ID")
    @Parameter(name = "toilet-id", description = "A specific toilet ID", in = ParameterIn.PATH, required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"id\": 1,\n" +
                            "    \"toiletName\": \"Nhà vệ sinh lưu động số 1\",\n" +
                            "    \"address\": \"44 Trần Đình Xu\",\n" +
                            "    \"ward\": \"Phường Cô Giang\",\n" +
                            "    \"district\": \"Quận 1\",\n" +
                            "    \"province\": \"Thành phố Hồ Chí Minh\",\n" +
                            "    \"longitude\": 10.759935271800982,\n" +
                            "    \"latitude\": 106.69202149316303,\n" +
                            "    \"nearBy\": \"Gần CircleK, gần Phúc Long\",\n" +
                            "    \"openTime\": \"09:00:00\",\n" +
                            "    \"closeTime\": \"23:00:00\",\n" +
                            "    \"minPrice\": 2000,\n" +
                            "    \"maxPrice\": 8000,\n" +
                            "    \"toiletFacilityDTOS\": [\n" +
                            "      {\n" +
                            "        \"facilityName\": \"Giấy vệ sinh\",\n" +
                            "        \"quantity\": 1,\n" +
                            "        \"description\": null\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"facilityName\": \"Máy sấy tay\",\n" +
                            "        \"quantity\": 0,\n" +
                            "        \"description\": null\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"facilityName\": \"Phòng tắm\",\n" +
                            "        \"quantity\": 0,\n" +
                            "        \"description\": null\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"facilityName\": \"Phòng vệ sinh\",\n" +
                            "        \"quantity\": 8,\n" +
                            "        \"description\": \"4 phòng vệ sinh cho nữ, 4 phòng vệ sinh cho nam\"\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"facilityName\": \"Phòng vệ sinh dành cho người khuyết tật\",\n" +
                            "        \"quantity\": 1,\n" +
                            "        \"description\": null\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"facilityName\": \"Vòi xịt\",\n" +
                            "        \"quantity\": 1,\n" +
                            "        \"description\": null\n" +
                            "      }\n" +
                            "    ],\n" +
                            "    \"toiletImageSources\": [\n" +
                            "      \"https://dichvucongichquan1.com/wp-content/uploads/2021/04/z2469130019572_1b2874d47ba76fa3b7089d0ffa4b72c7.jpg\",\n" +
                            "      \"https://dichvucongichquan1.com/wp-content/uploads/2021/04/z2469130681021_b9303b13544929365e1810b07c7e3dff.jpg\"\n" +
                            "    ],\n" +
                            "    \"ratingStar\": 3.8,\n" +
                            "    \"free\": false\n" +
                            "  }")
            })),
            @ApiResponse(responseCode = "204", description = "No-content!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.MANAGER, RoleConstant.STAFF, RoleConstant.USER})
    @GetMapping(value = "/{toilet-id}")
    public ResponseEntity<BaseResponse<ToiletDetailsInfoResponse>> getToiletByToiletId(@PathVariable("toilet-id") int toiletId) {

        ToiletDetailsInfoResponse response = toiletService.getToiletByToiletId(toiletId);
        if (response == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return ResponseBuilder.generateResponse(
                "Get toilet by toilet ID successfully!",
                HttpStatus.OK,
                response
        );
    }

    @Hidden
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN, RoleConstant.USER})
    @GetMapping
    public ResponseEntity<BaseResponse<List<ToiletDetailsInfoResponse>>> getAllToilets() {

        List<ToiletDetailsInfoResponse> response = toiletService.getAllToilets();

        return ResponseBuilder.generateResponse(
                "Get list of toilets successfully!",
                HttpStatus.OK,
                response
        );
    }
}
