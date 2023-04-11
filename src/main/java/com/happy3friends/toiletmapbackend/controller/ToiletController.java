package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.base.models.BaseResponse;
import com.happy3friends.toiletmapbackend.config.OpenApiConfig;
import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.response.ToiletDetailsInfoResponse;
import com.happy3friends.toiletmapbackend.service.ToiletService;
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

@Tag(name = "Toilet", description = "Toilet API")
@RestController
@RequestMapping(value = "/api/toilets")
public class ToiletController {

    @Autowired
    private ToiletService toiletService;

    @Operation(summary = "Get toilet by toilet ID", description = "[Manager, Toilet, User] Get a specific toilet by toilet ID")
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
                            "    \"latitude\": 10.759935271800982,\n" +
                            "    \"longitude\": 106.69202149316303,\n" +
                            "    \"nearBy\": \"Gần CircleK, gần Phúc Long\",\n" +
                            "    \"openTime\": \"09:00:00\",\n" +
                            "    \"closeTime\": \"23:00:00\",\n" +
                            "    \"minPrice\": 2000,\n" +
                            "    \"maxPrice\": 8000,\n" +
                            "    \"toiletFacilityDTOS\": [\n" +
                            "      {\n" +
                            "        \"facilityName\": \"Giấy vệ sinh\",\n" +
                            "        \"facilityType\": \"Trang thiết bị\",\n" +
                            "        \"quantity\": 1,\n" +
                            "        \"description\": null\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"facilityName\": \"Máy sấy tay\",\n" +
                            "        \"facilityType\": \"Trang thiết bị\",\n" +
                            "        \"quantity\": 0,\n" +
                            "        \"description\": null\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"facilityName\": \"Phòng tắm\",\n" +
                            "        \"facilityType\": \"Phòng\",\n" +
                            "        \"quantity\": 0,\n" +
                            "        \"description\": null\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"facilityName\": \"Phòng vệ sinh\",\n" +
                            "        \"facilityType\": \"Phòng\",\n" +
                            "        \"quantity\": 8,\n" +
                            "        \"description\": \"4 phòng vệ sinh cho nữ, 4 phòng vệ sinh cho nam\"\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"facilityName\": \"Phòng vệ sinh dành cho người khuyết tật\",\n" +
                            "        \"facilityType\": \"Phòng\",\n" +
                            "        \"quantity\": 1,\n" +
                            "        \"description\": null\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"facilityName\": \"Vòi xịt\",\n" +
                            "        \"facilityType\": \"Trang thiết bị\",\n" +
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
    @RolesAllowed({RoleConstant.MANAGER, RoleConstant.TOILET, RoleConstant.USER})
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

    @Operation(summary = "Get list of all toilets",
            description = "- [User] Get list of all toilets (only find id, latitude and longitude value)\n" +
                    "- [User] Get list of top 10 toilets near by current location")
    @Parameters(value = {
            @Parameter(name = "current-latitude", description = "Current latitude", in = ParameterIn.QUERY, required = false),
            @Parameter(name = "current-longitude", description = "Current longitude", in = ParameterIn.QUERY, required = false)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(name = "Get list of all toilets (only find id, latitude and longitude value)", value = "[\n" +
                            "    {\n" +
                            "      \"id\": 1,\n" +
                            "      \"latitude\": 10.759935271800982,\n" +
                            "      \"longitude\": 106.69202149316303\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": 2,\n" +
                            "      \"latitude\": 10.773159027085631,\n" +
                            "      \"longitude\": 106.70411367332397\n" +
                            "    }\n" +
                            "  ]"),
                    @ExampleObject(name = "Get list of top 10 toilets near by current location", value = "[\n" +
                            "    {\n" +
                            "      \"id\": 2,\n" +
                            "      \"toiletName\": \"Nhà vệ sinh lưu động số 2\",\n" +
                            "      \"address\": \"79 Nguyễn Huệ\",\n" +
                            "      \"ward\": \"Bến Nghé\",\n" +
                            "      \"district\": \"Quận 1\",\n" +
                            "      \"province\": \"Thành phố Hồ Chí Minh\",\n" +
                            "      \"latitude\": 10.845946930378068,\n" +
                            "      \"longitude\": 106.797017719663,\n" +
                            "      \"nearBy\": null,\n" +
                            "      \"openTime\": \"09:00:00\",\n" +
                            "      \"closeTime\": \"23:00:00\",\n" +
                            "      \"minPrice\": 2000,\n" +
                            "      \"maxPrice\": 8000,\n" +
                            "      \"toiletFacilities\": [\n" +
                            "        {\n" +
                            "          \"facilityName\": \"Phòng tắm\",\n" +
                            "          \"facilityType\": \"Phòng\",\n" +
                            "          \"quantity\": 0,\n" +
                            "          \"description\": null\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"facilityName\": \"Phòng vệ sinh\",\n" +
                            "          \"facilityType\": \"Phòng\",\n" +
                            "          \"quantity\": 0,\n" +
                            "          \"description\": null\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"facilityName\": \"Phòng vệ sinh dành cho người khuyết tật\",\n" +
                            "          \"facilityType\": \"Phòng\",\n" +
                            "          \"quantity\": 0,\n" +
                            "          \"description\": null\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"facilityName\": \"Giấy vệ sinh\",\n" +
                            "          \"facilityType\": \"Trang thiết bị\",\n" +
                            "          \"quantity\": 0,\n" +
                            "          \"description\": null\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"facilityName\": \"Máy sấy tay\",\n" +
                            "          \"facilityType\": \"Trang thiết bị\",\n" +
                            "          \"quantity\": 0,\n" +
                            "          \"description\": null\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"facilityName\": \"Vòi xịt\",\n" +
                            "          \"facilityType\": \"Trang thiết bị\",\n" +
                            "          \"quantity\": 0,\n" +
                            "          \"description\": null\n" +
                            "        }\n" +
                            "      ],\n" +
                            "      \"toiletImageSources\": [\n" +
                            "        \"https://anh.eva.vn/upload/2-2015/images/2015-05-13/1431482470-ava.jpg\"\n" +
                            "      ],\n" +
                            "      \"ratingStar\": 0,\n" +
                            "      \"free\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": 1,\n" +
                            "      \"toiletName\": \"Nhà vệ sinh lưu động số 1\",\n" +
                            "      \"address\": \"44 Trần Đình Xu\",\n" +
                            "      \"ward\": \"Cô Giang\",\n" +
                            "      \"district\": \"Quận 1\",\n" +
                            "      \"province\": \"Thành phố Hồ Chí Minh\",\n" +
                            "      \"latitude\": 10.845254597727745,\n" +
                            "      \"longitude\": 106.79238946200942,\n" +
                            "      \"nearBy\": \"Gần CircleK, gần Phúc Long\",\n" +
                            "      \"openTime\": \"09:00:00\",\n" +
                            "      \"closeTime\": \"23:00:00\",\n" +
                            "      \"minPrice\": 2000,\n" +
                            "      \"maxPrice\": 8000,\n" +
                            "      \"toiletFacilities\": [\n" +
                            "        {\n" +
                            "          \"facilityName\": \"Phòng tắm\",\n" +
                            "          \"facilityType\": \"Phòng\",\n" +
                            "          \"quantity\": 0,\n" +
                            "          \"description\": null\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"facilityName\": \"Phòng vệ sinh\",\n" +
                            "          \"facilityType\": \"Phòng\",\n" +
                            "          \"quantity\": 8,\n" +
                            "          \"description\": \"4 phòng vệ sinh cho nữ, 4 phòng vệ sinh cho nam\"\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"facilityName\": \"Phòng vệ sinh dành cho người khuyết tật\",\n" +
                            "          \"facilityType\": \"Phòng\",\n" +
                            "          \"quantity\": 1,\n" +
                            "          \"description\": null\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"facilityName\": \"Giấy vệ sinh\",\n" +
                            "          \"facilityType\": \"Trang thiết bị\",\n" +
                            "          \"quantity\": 1,\n" +
                            "          \"description\": null\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"facilityName\": \"Máy sấy tay\",\n" +
                            "          \"facilityType\": \"Trang thiết bị\",\n" +
                            "          \"quantity\": 0,\n" +
                            "          \"description\": null\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"facilityName\": \"Vòi xịt\",\n" +
                            "          \"facilityType\": \"Trang thiết bị\",\n" +
                            "          \"quantity\": 1,\n" +
                            "          \"description\": null\n" +
                            "        }\n" +
                            "      ],\n" +
                            "      \"toiletImageSources\": [\n" +
                            "        \"https://dichvucongichquan1.com/wp-content/uploads/2021/04/z2469130019572_1b2874d47ba76fa3b7089d0ffa4b72c7.jpg\"\n" +
                            "      ],\n" +
                            "      \"ratingStar\": 3.8,\n" +
                            "      \"free\": false\n" +
                            "    }\n" +
                            "  ]")
            })),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.USER})
    @GetMapping
    public ResponseEntity<BaseResponse<List<ToiletDetailsInfoResponse>>> getAllToilets(
            @RequestParam(name = "current-latitude", required = false) Double currentLatitude,
            @RequestParam(name = "current-longitude", required = false) Double currentLongitude) {

        List<ToiletDetailsInfoResponse> response = toiletService.getAllToilets(currentLatitude, currentLongitude);

        return ResponseBuilder.generateResponse(
                "Get list of all toilets successfully!",
                HttpStatus.OK,
                response
        );
    }
}
