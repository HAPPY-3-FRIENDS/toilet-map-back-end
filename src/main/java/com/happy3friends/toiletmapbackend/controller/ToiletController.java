package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.base.models.BaseResponse;
import com.happy3friends.toiletmapbackend.config.OpenApiConfig;
import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.request.ToiletCreateRequest;
import com.happy3friends.toiletmapbackend.response.ToiletDetailsInfoResponse;
import com.happy3friends.toiletmapbackend.response.UpdateToiletInfoResponse;
import com.happy3friends.toiletmapbackend.service.ToiletService;
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
import java.util.List;
import java.util.Map;

@Tag(name = "Toilet", description = "Toilet API")
@RestController
@RequestMapping(value = "/api/toilets")
public class ToiletController {

    @Autowired
    private ToiletService toiletService;

    @Operation(summary = "Get toilet by toilet ID", description = "[Manager, Toilet, User] Get a specific toilet by toilet ID")
    @Parameter(name = "toilet-id", description = "A specific toilet ID", in = ParameterIn.PATH, required = true, example = "4")
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
                            "    \"openTime\": \"09:00\",\n" +
                            "    \"closeTime\": \"23:00\",\n" +
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
            description = "- [Manager] Get list of all toilets of company by company ID (pagination)\n" +
                    "- [User] Get list of all toilets (only find id, latitude and longitude value)\n" +
                    "- [User] Get list of top 10 toilets near by current location")
    @Parameters(value = {
            @Parameter(name = "company-id", description = "Company ID", in = ParameterIn.QUERY),
            @Parameter(name = "current-latitude", description = "Current latitude", in = ParameterIn.QUERY),
            @Parameter(name = "current-longitude", description = "Current longitude", in = ParameterIn.QUERY),
            @Parameter(name = "sort",
                    in = ParameterIn.QUERY,
                    description = "Sorting criteria in the format: property(,asc|desc). Default sort order is ascending by id. Multiple sort criteria are supported.",
                    example ="[\"toiletName,asc\", \"username,desc\"]",
                    array = @ArraySchema(schema = @Schema(implementation = String.class), maxItems = 5),
                    allowReserved = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(name = "Get list of all toilets of company by company ID", value = "[\n" +
                            "    {\n" +
                            "      \"id\": 4,\n" +
                            "      \"toiletName\": \"Nhà vệ sinh lưu động số 1\",\n" +
                            "      \"address\": \"44 Trần Đình Xu\",\n" +
                            "      \"ward\": \"Cô Giang\",\n" +
                            "      \"district\": \"Quận 1\",\n" +
                            "      \"province\": \"Thành phố Hồ Chí Minh\",\n" +
                            "      \"username\": \"toilet-1\",\n" +
                            "      \"status\": \"Đang hoạt động\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": 5,\n" +
                            "      \"toiletName\": \"Nhà vệ sinh lưu động số 2\",\n" +
                            "      \"address\": \"79 Nguyễn Huệ\",\n" +
                            "      \"ward\": \"Bến Nghé\",\n" +
                            "      \"district\": \"Quận 1\",\n" +
                            "      \"province\": \"Thành phố Hồ Chí Minh\",\n" +
                            "      \"username\": \"toilet-2\",\n" +
                            "      \"status\": \"Đang hoạt động\"\n" +
                            "    }\n" +
                            "  ]"),
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
                            "      \"openTime\": \"09:00\",\n" +
                            "      \"closeTime\": \"23:00\",\n" +
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
                            "      \"openTime\": \"09:00\",\n" +
                            "      \"closeTime\": \"23:00\",\n" +
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
    @RolesAllowed({RoleConstant.MANAGER, RoleConstant.USER})
    @GetMapping
    public ResponseEntity<BaseResponse<List<ToiletDetailsInfoResponse>>> getAllToilets(
            @RequestParam(name = "company-id", required = false) Integer companyId,
            @RequestParam(name = "current-latitude", required = false) Double currentLatitude,
            @RequestParam(name = "current-longitude", required = false) Double currentLongitude,
            @ModelAttribute BasePaginationRequest paginationRequest) {

        List<ToiletDetailsInfoResponse> responses = toiletService.getAllToilets(
                companyId,
                currentLatitude,
                currentLongitude,
                paginationRequest);

        return ResponseBuilder.generateResponse(
                "Get list of all toilets successfully!",
                HttpStatus.OK,
                responses
        );
    }

    @Operation(summary = "Count list of all toilets", description = "[Manager] Count list of all toilet of a specific Company by Company ID")
    @Parameters(value = {
            @Parameter(name = "company-id", description = "Company ID", in = ParameterIn.QUERY, example = "2"),
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
    @RolesAllowed({RoleConstant.MANAGER})
    @GetMapping(value = "/count")
    public ResponseEntity<BaseResponse<Integer>> count(
            @RequestParam(name = "company-id", required = false) Integer companyId) {

        int response = toiletService.count(companyId);

        if (companyId != null) {
            return ResponseBuilder.generateResponse(
                    "Count list of toilets of company by Company ID successfully!",
                    HttpStatus.OK,
                    response
            );
        }

        return ResponseBuilder.generateResponse(
                "Count list of all toilets successfully!",
                HttpStatus.OK,
                response
        );
    }

    @Operation(summary = "Create a toilet", description = "[Manager] Create a toilet and its information")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Toilet Create Request", required = true, content = @Content(
            examples = {
                    @ExampleObject(value = "{\n" +
                            "  \"companyId\": 2,\n" +
                            "  \"username\": \"toilet-3\",\n" +
                            "  \"password\": \"123\",\n" +
                            "  \"name\": \"Toilet 3\",\n" +
                            "  \"address\": \"Trịnh Phong Đáng\",\n" +
                            "  \"ward\": \"Long Thành Bắc\",\n" +
                            "  \"district\": \"Hòa Thành\",\n" +
                            "  \"province\": \"Tây Ninh\",\n" +
                            "  \"latitude\": 123,\n" +
                            "  \"longitude\": 456,\n" +
                            "  \"openTime\": \"08:00\",\n" +
                            "  \"closeTime\": \"20:00\",\n" +
                            "  \"status\": \"Đang hoạt động\",\n" +
                            "  \"toiletImages\": [\n" +
                            "      \"imageSource1\",\n" +
                            "      \"imageSource2\"\n" +
                            "  ],\n" +
                            "  \"toiletFacilities\": [\n" +
                            "    {\n" +
                            "      \"facilityId\": 1,\n" +
                            "      \"quantity\": 4\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"facilityId\": 2,\n" +
                            "      \"quantity\": 1\n" +
                            "    }\n" +
                            "  ],\n" +
                            "  \"free\": true\n" +
                            "}")}))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.MANAGER})
    @PostMapping
    public ResponseEntity<BaseResponse<ToiletCreateRequest>> createToilet(@RequestBody ToiletCreateRequest toiletCreateRequest) throws Exception {

        toiletService.createToilet(toiletCreateRequest);

        return ResponseBuilder.generateResponse(
                "Create toilet successfully!",
                HttpStatus.CREATED,
                toiletCreateRequest
        );
    }


    @Operation(summary = "Get nearest toilet",
            description = "- [USER] Get nearest toilet for urgent situation")
    @Parameters(value = {
            @Parameter(name = "current-latitude", description = "Current latitude", in = ParameterIn.QUERY),
            @Parameter(name = "current-longitude", description = "Current longitude", in = ParameterIn.QUERY),
            @Parameter(name = "vehicle", description = "Vehicle type", in = ParameterIn.QUERY)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "  \"message\": \"Get nearest toilet successfully!\",\n" +
                            "  \"status\": 200,\n" +
                            "  \"data\": {\n" +
                            "    \"id\": 5,\n" +
                            "    \"toiletName\": \"Nhà vệ sinh lưu động số 2\",\n" +
                            "    \"address\": \"79 Nguyễn Huệ\",\n" +
                            "    \"ward\": \"Bến Nghé\",\n" +
                            "    \"district\": \"Quận 1\",\n" +
                            "    \"province\": \"Thành phố Hồ Chí Minh\",\n" +
                            "    \"latitude\": 10.8360458,\n" +
                            "    \"longitude\": 106.8084369,\n" +
                            "    \"nearBy\": null,\n" +
                            "    \"openTime\": \"09:00\",\n" +
                            "    \"closeTime\": \"23:00\",\n" +
                            "    \"minPrice\": 5000,\n" +
                            "    \"maxPrice\": 15000,\n" +
                            "    \"toiletFacilities\": [\n" +
                            "      {\n" +
                            "        \"facilityId\": 0,\n" +
                            "        \"facilityName\": \"Phòng tắm\",\n" +
                            "        \"facilityType\": \"Phòng\",\n" +
                            "        \"quantity\": 0,\n" +
                            "        \"description\": null\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"facilityId\": 0,\n" +
                            "        \"facilityName\": \"Phòng vệ sinh\",\n" +
                            "        \"facilityType\": \"Phòng\",\n" +
                            "        \"quantity\": 0,\n" +
                            "        \"description\": null\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"facilityId\": 0,\n" +
                            "        \"facilityName\": \"Phòng vệ sinh dành cho người khuyết tật\",\n" +
                            "        \"facilityType\": \"Phòng\",\n" +
                            "        \"quantity\": 0,\n" +
                            "        \"description\": null\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"facilityId\": 0,\n" +
                            "        \"facilityName\": \"Giấy vệ sinh\",\n" +
                            "        \"facilityType\": \"Trang thiết bị\",\n" +
                            "        \"quantity\": 0,\n" +
                            "        \"description\": null\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"facilityId\": 0,\n" +
                            "        \"facilityName\": \"Máy sấy tay\",\n" +
                            "        \"facilityType\": \"Trang thiết bị\",\n" +
                            "        \"quantity\": 0,\n" +
                            "        \"description\": null\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"facilityId\": 0,\n" +
                            "        \"facilityName\": \"Vòi xịt\",\n" +
                            "        \"facilityType\": \"Trang thiết bị\",\n" +
                            "        \"quantity\": 0,\n" +
                            "        \"description\": null\n" +
                            "      }\n" +
                            "    ],\n" +
                            "    \"toiletImageSources\": [\n" +
                            "      \"https://anh.eva.vn/upload/2-2015/images/2015-05-13/1431482470-ava.jpg\"\n" +
                            "    ],\n" +
                            "    \"ratingStar\": 2,\n" +
                            "    \"username\": null,\n" +
                            "    \"status\": null,\n" +
                            "    \"free\": false\n" +
                            "  }\n" +
                            "}")
            })),
            @ApiResponse(responseCode = "201", description = "Successfully!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.USER})
    @GetMapping("/nearest-toilet")
    public ResponseEntity<BaseResponse<ToiletDetailsInfoResponse>> getNearestToilet(
            @RequestParam(name = "current-latitude", required = false) Double currentLatitude,
            @RequestParam(name = "current-longitude", required = false) Double currentLongitude,
            @RequestParam(name = "vehicle", required = false) String vehicle) {

        ToiletDetailsInfoResponse responses = toiletService.getNearestToilet(currentLatitude, currentLongitude, vehicle);

        return ResponseBuilder.generateResponse(
                "Get nearest toilet successfully!",
                HttpStatus.OK,
                responses
        );
    }

    @Operation(summary = "Update toilet info", description = "[Manager] Update toilet and its information")
    @Parameter(name = "toilet-id", description = "A specific toilet ID", in = ParameterIn.PATH, required = true, example = "4")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Fields Request", required = true, content = @Content(
            examples = {
                    @ExampleObject(name = "Update toilet info", value = "{\n" +
                            "    \"name\": \"Hieu toilet updated\",\n" +
                            "    \"address\": \"Trường Đại học FPT TP. HCM\",\n" +
                            "    \"ward\": \"Long Thạnh Mỹ\",\n" +
                            "    \"district\": \"Thủ Đức\",\n" +
                            "    \"province\": \"Hồ Chí Minh\",\n" +
                            "    \"openTime\": \"09:00\",\n" +
                            "    \"closeTime\": \"23:00\",\n" +
                            "    \"toiletImagesById\": [\n" +
                            "\t\t\"Update1\",\n" +
                            "\t\t\"Update2\"\n" +
                            "    ],\n" +
                            "\t\"toiletFacilitiesById\": [\n" +
                            "\t\t{\n" +
                            "\t\t\t\"facilityId\": 4,\n" +
                            "\t\t\t\"quantity\":1\n" +
                            "\t\t},\n" +
                            "\t\t{\n" +
                            "\t\t\t\"facilityId\": 5,\n" +
                            "\t\t\t\"quantity\":1\n" +
                            "\t\t},\n" +
                            "\t\t{\n" +
                            "\t\t\t\"facilityId\": 6,\n" +
                            "\t\t\t\"quantity\":1\n" +
                            "\t\t},\n" +
                            "\t\t{\n" +
                            "\t\t\t\"facilityId\": 1,\n" +
                            "\t\t\t\"quantity\":10\n" +
                            "\t\t},\n" +
                            "\t\t{\n" +
                            "\t\t\t\"facilityId\": 2,\n" +
                            "\t\t\t\"quantity\":10\n" +
                            "\t\t},\n" +
                            "\t\t{\n" +
                            "\t\t\t\"facilityId\": 3,\n" +
                            "\t\t\t\"quantity\":10\n" +
                            "\t\t}\n" +
                            "\t],\n" +
                            "    \"status\": \"Không hoạt động\",\n" +
                            "    \"isFree\": true\n" +
                            "}"),
                    @ExampleObject(name = "Update status toilet", value = "{\n" +
                            "    \"status\": \"Để gì cũm được, để trống cũm được, khùn đin cũm được\"\n" +
                            "  }")}))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"id\": 88,\n" +
                            "    \"name\": \"Hieu toilet updated\",\n" +
                            "    \"address\": \"Trường Đại học FPT TP. HCM\",\n" +
                            "    \"ward\": \"Long Thạnh Mỹ\",\n" +
                            "    \"district\": \"Thủ Đức\",\n" +
                            "    \"province\": \"Hồ Chí Minh\",\n" +
                            "    \"openTime\": \"09:00:00\",\n" +
                            "    \"closeTime\": \"23:00:00\",\n" +
                            "    \"toiletImagesById\": [\n" +
                            "      \"Update1\",\n" +
                            "      \"Update2\"\n" +
                            "    ],\n" +
                            "    \"toiletFacilitiesById\": [\n" +
                            "      {\n" +
                            "        \"facilityId\": 4,\n" +
                            "        \"facilityName\": \"Vòi xịt\",\n" +
                            "        \"facilityType\": \"Trang thiết bị\",\n" +
                            "        \"quantity\": 1,\n" +
                            "        \"description\": null\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"facilityId\": 5,\n" +
                            "        \"facilityName\": \"Máy sấy tay\",\n" +
                            "        \"facilityType\": \"Trang thiết bị\",\n" +
                            "        \"quantity\": 1,\n" +
                            "        \"description\": null\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"facilityId\": 6,\n" +
                            "        \"facilityName\": \"Giấy vệ sinh\",\n" +
                            "        \"facilityType\": \"Trang thiết bị\",\n" +
                            "        \"quantity\": 1,\n" +
                            "        \"description\": null\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"facilityId\": 1,\n" +
                            "        \"facilityName\": \"Phòng vệ sinh\",\n" +
                            "        \"facilityType\": \"Phòng\",\n" +
                            "        \"quantity\": 10,\n" +
                            "        \"description\": null\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"facilityId\": 2,\n" +
                            "        \"facilityName\": \"Phòng tắm\",\n" +
                            "        \"facilityType\": \"Phòng\",\n" +
                            "        \"quantity\": 10,\n" +
                            "        \"description\": null\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"facilityId\": 3,\n" +
                            "        \"facilityName\": \"Phòng vệ sinh dành cho người khuyết tật\",\n" +
                            "        \"facilityType\": \"Phòng\",\n" +
                            "        \"quantity\": 10,\n" +
                            "        \"description\": null\n" +
                            "      }\n" +
                            "    ],\n" +
                            "    \"status\": \"Không hoạt động\",\n" +
                            "    \"free\": true\n" +
                            "  }")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.MANAGER})
    @PatchMapping("/{toilet-id}")
    public ResponseEntity<BaseResponse<UpdateToiletInfoResponse>> updateToiletInfo(
            @PathVariable("toilet-id") Integer id,
            @RequestBody Map<String, Object> fields) {

        UpdateToiletInfoResponse response = toiletService.updateToiletInfo(id, fields);

        return ResponseBuilder.generateResponse(
                "Update toilet info successfully!",
                HttpStatus.OK,
                response
        );
    }
}
