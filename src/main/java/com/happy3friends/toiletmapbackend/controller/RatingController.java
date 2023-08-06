package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.base.models.BaseResponse;
import com.happy3friends.toiletmapbackend.config.OpenApiConfig;
import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.request.FilterRatingRequest;
import com.happy3friends.toiletmapbackend.request.RatingRequest;
import com.happy3friends.toiletmapbackend.response.RatingResponse;
import com.happy3friends.toiletmapbackend.service.RatingService;
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

@Tag(name = "Rating", description = "Rating API")
@RestController
@RequestMapping(value = "/api/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @Operation(summary = "Create a rating", description = "[User] Create a rating and its information")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Toilet Create Request", required = true, content = @Content(
            examples = {
                    @ExampleObject(value = "{\n" +
                            "  \"toiletId\": 4,\n" +
                            "  \"star\": 5,\n" +
                            "  \"comment\": \"Nhà vệ sinh rất sạch, tớ sẽ ghé sử dụng thường xuyên\",\n" +
                            "  \"accountId\": 6,\n" +
                            "  \"checkInId\": 547,\n" +
                            "  \"imageSources\": [\n" +
                            "    \"https://meovatchamsocgiadinh.com/ckfinder/userfiles/images/cau-chuyen-ve-chiec-bon-cau-ban-va-cach-xu-ly-ham-cau-bi-day/cau-chuyen-ve-chiec-bon-cau-ban.jpg\",\n" +
                            "    \"https://afamilycdn.com/k:thumb_w/600/h6ZUmntrbWseKUPJv6Yt1NY22jQBtc/Image/2014/12/6-8429e/tu-su-cua-chiec-bon-cau-ban-va-chuyen-di-ve-sinh-cua-be-o-truong.jpg\"\n" +
                            "  ],\n" +
                            "  \"commonComments\": [\n" +
                            "    2,\n" +
                            "    3,\n" +
                            "    4,\n" +
                            "    5\n" +
                            "  ]\n" +
                            "}")}))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"id\": 8,\n" +
                            "    \"fullName\": \"Huỳnh Lê Thủy Tiên\",\n" +
                            "    \"star\": 5,\n" +
                            "    \"comment\": \"Nhà vệ sinh rất sạch, tớ sẽ ghé sử dụng thường xuyên\",\n" +
                            "    \"dateTime\": \"16/04/2023 - 13:40:06\",\n" +
                            "    \"imageSources\": [\n" +
                            "      \"https://meovatchamsocgiadinh.com/ckfinder/userfiles/images/cau-chuyen-ve-chiec-bon-cau-ban-va-cach-xu-ly-ham-cau-bi-day/cau-chuyen-ve-chiec-bon-cau-ban.jpg\",\n" +
                            "      \"https://afamilycdn.com/k:thumb_w/600/h6ZUmntrbWseKUPJv6Yt1NY22jQBtc/Image/2014/12/6-8429e/tu-su-cua-chiec-bon-cau-ban-va-chuyen-di-ve-sinh-cua-be-o-truong.jpg\"\n" +
                            "    ]\n" +
                            "  }")
            })),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.USER})
    @PostMapping
    public ResponseEntity<BaseResponse<RatingResponse>> createRating(@RequestBody RatingRequest ratingRequest) {

        RatingResponse response = ratingService.createRating(ratingRequest);

        return ResponseBuilder.generateResponse(
                "Create rating successfully!",
                HttpStatus.CREATED,
                response
        );
    }

    @Operation(summary = "Get all ratings", description = "[Manager, User] Get a list of all ratings")
    @Parameters(value = {
            @Parameter(name = "toilet-id", description = "A specific toilet ID", in = ParameterIn.QUERY),
            @Parameter(name = "sort",
                    in = ParameterIn.QUERY,
                    description = "Sorting criteria in the format: property(,asc|desc). Default sort order is ascending by Star and descending by DateTime. Multiple sort criteria are supported.",
                    array = @ArraySchema(schema = @Schema(implementation = String.class), maxItems = 5),
                    allowReserved = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(name = "Get all ratings", value = "[\n" +
                            "    {\n" +
                            "      \"id\": 1,\n" +
                            "      \"fullName\": \"Huỳnh Lê Thủy Tiên\",\n" +
                            "      \"star\": 5,\n" +
                            "      \"comment\": \"Nhà vệ sinh tuyệt đỉnh\",\n" +
                            "      \"dateTime\": \"14/04/2023 - 16:45:00\",\n" +
                            "      \"imageSources\": [\n" +
                            "        \"https://nhavesinhcongcong.vn/wp-content/uploads/2018/02/NT-nh%C3%A0-vs-%C4%91%C3%B4i.jpg\",\n" +
                            "        \"https://ashui.com/mag/images/stories/202008/toilet1.jpg\"\n" +
                            "      ]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": 2,\n" +
                            "      \"fullName\": \"Phạm Huỳnh Anh Hoàng\",\n" +
                            "      \"star\": 4,\n" +
                            "      \"comment\": \"Nhà vệ sinh sạch quá\",\n" +
                            "      \"dateTime\": \"14/04/2023 - 16:45:00\",\n" +
                            "      \"imageSources\": [\n" +
                            "        null\n" +
                            "      ]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": 3,\n" +
                            "      \"fullName\": \"Huỳnh Lê Thủy Tiên\",\n" +
                            "      \"star\": 2,\n" +
                            "      \"comment\": \"Nhà vệ sinh dơ\",\n" +
                            "      \"dateTime\": \"14/04/2023 - 16:45:00\",\n" +
                            "      \"imageSources\": [\n" +
                            "        null\n" +
                            "      ]\n" +
                            "    }\n" +
                            "  ]"),
                    @ExampleObject(name = "Get all ratings by toilet ID", value = "[\n" +
                            "    {\n" +
                            "      \"id\": 2,\n" +
                            "      \"fullName\": \"Phạm Huỳnh Anh Hoàng\",\n" +
                            "      \"star\": 4,\n" +
                            "      \"comment\": \"Nhà vệ sinh sạch quá\",\n" +
                            "      \"dateTime\": \"14/04/2023 - 16:45:00\",\n" +
                            "      \"imageSources\": [\n" +
                            "        null\n" +
                            "      ]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": 1,\n" +
                            "      \"fullName\": \"Huỳnh Lê Thủy Tiên\",\n" +
                            "      \"star\": 5,\n" +
                            "      \"comment\": \"Nhà vệ sinh tuyệt đỉnh\",\n" +
                            "      \"dateTime\": \"14/04/2023 - 16:45:00\",\n" +
                            "      \"imageSources\": [\n" +
                            "        \"https://nhavesinhcongcong.vn/wp-content/uploads/2018/02/NT-nh%C3%A0-vs-%C4%91%C3%B4i.jpg\",\n" +
                            "        \"https://ashui.com/mag/images/stories/202008/toilet1.jpg\"\n" +
                            "      ]\n" +
                            "    }\n" +
                            "  ]")
            })),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.MANAGER, RoleConstant.USER})
    @GetMapping
    public ResponseEntity<BaseResponse<List<RatingResponse>>> getAllRatings(
            @RequestParam(value = "toilet-id", required = false) Integer toiletId,
            @ModelAttribute BasePaginationRequest paginationRequest) {

        List<RatingResponse> responses = ratingService.getAllRatings(toiletId, paginationRequest);

        return ResponseBuilder.generateResponse(
                "Get list of all ratings successfully!",
                HttpStatus.OK,
                responses
        );
    }


    @Operation(summary = "Count list of all ratings", description = "[Manager] Count list of rating of a specific Toilet by Toilet ID")
    @Parameters(value = {
            @Parameter(name = "toilet-id", description = "A specific Toilet ID", in = ParameterIn.QUERY, example = "6")
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
    @RolesAllowed({RoleConstant.MANAGER, RoleConstant.USER})
    @GetMapping(value = "/count")
    public ResponseEntity<BaseResponse<Integer>> count(
            @RequestParam(name = "toilet-id", required = false) Integer toiletId) {

        int response = ratingService.count(toiletId);

        if (toiletId != null) {
            return ResponseBuilder.generateResponse(
                    "Count list of rating by Toilet ID successfully!",
                    HttpStatus.OK,
                    response
            );
        }

        return ResponseBuilder.generateResponse(
                "Count list of rating successfully!",
                HttpStatus.OK,
                response
        );
    }

    @Operation(summary = "Update rating info", description = "[Manager] Update rating and its information")
    @Parameter(name = "rating-id", description = "A specific rating ID", in = ParameterIn.PATH, required = true, example = "4")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Fields Request", required = true, content = @Content(
            examples = {
                    @ExampleObject(name = "Update status rating", value = "{\n" +
                            "    \"status\": \"Đã giải quyết // Từ chối giải quyết\"\n" +
                            "  }")}))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "hihihi ai rảnh đâu mà viết doc, giờ đang gấp lắm")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.MANAGER})
    @PatchMapping("/{rating-id}")
    public ResponseEntity<BaseResponse<RatingResponse>> update(
            @PathVariable("rating-id") Integer id,
            @RequestBody Map<String, Object> fields) {

        RatingResponse response = ratingService.update(id, fields);

        return ResponseBuilder.generateResponse(
                "Update rating info successfully!",
                HttpStatus.OK,
                response
        );
    }

    @Operation(summary = "Filter rating by star", description = "[User] Filter rating by star")
    @Parameters(value = {
            @Parameter(name = "toilet-id", description = "A specific toilet ID", in = ParameterIn.QUERY),
            @Parameter(name = "star", description = "Number of star", in = ParameterIn.QUERY),
            @Parameter(name = "sort",
                    in = ParameterIn.QUERY,
                    description = "Sorting criteria in the format: property(,asc|desc). Default sort order is ascending by Star and descending by DateTime. Multiple sort criteria are supported.",
                    array = @ArraySchema(schema = @Schema(implementation = String.class), maxItems = 5),
                    allowReserved = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(name = "Filter rating by star", value = "[\n" +
                            "    {\n" +
                            "      \"id\": 1,\n" +
                            "      \"fullName\": \"Huỳnh Lê Thủy Tiên\",\n" +
                            "      \"star\": 5,\n" +
                            "      \"comment\": \"Nhà vệ sinh tuyệt đỉnh\",\n" +
                            "      \"dateTime\": \"14/04/2023 - 16:45:00\",\n" +
                            "      \"imageSources\": [\n" +
                            "        \"https://nhavesinhcongcong.vn/wp-content/uploads/2018/02/NT-nh%C3%A0-vs-%C4%91%C3%B4i.jpg\",\n" +
                            "        \"https://ashui.com/mag/images/stories/202008/toilet1.jpg\"\n" +
                            "      ]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": 2,\n" +
                            "      \"fullName\": \"Phạm Huỳnh Anh Hoàng\",\n" +
                            "      \"star\": 4,\n" +
                            "      \"comment\": \"Nhà vệ sinh sạch quá\",\n" +
                            "      \"dateTime\": \"14/04/2023 - 16:45:00\",\n" +
                            "      \"imageSources\": [\n" +
                            "        null\n" +
                            "      ]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": 3,\n" +
                            "      \"fullName\": \"Huỳnh Lê Thủy Tiên\",\n" +
                            "      \"star\": 2,\n" +
                            "      \"comment\": \"Nhà vệ sinh dơ\",\n" +
                            "      \"dateTime\": \"14/04/2023 - 16:45:00\",\n" +
                            "      \"imageSources\": [\n" +
                            "        null\n" +
                            "      ]\n" +
                            "    }\n" +
                            "  ]")
            })),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.USER})
    @GetMapping("/filter-rating-by-star")
    public ResponseEntity<BaseResponse<List<RatingResponse>>> filterRatingByStar(
            @RequestParam(value = "toilet-id", required = false) Integer toiletId,
            @RequestParam(value = "star", required = false) Integer star,
            @ModelAttribute BasePaginationRequest paginationRequest) {

        List<RatingResponse> responses = ratingService.filterRatingByStar(toiletId, star, paginationRequest);

        if (responses.isEmpty()) {
            return ResponseBuilder.generateResponse(
                    "Filter rating by star successfully!",
                    HttpStatus.NO_CONTENT,
                    responses
            );
        } else {
            return ResponseBuilder.generateResponse(
                    "Filter rating by star successfully!",
                    HttpStatus.OK,
                    responses
            );
        }
    }

    @Operation(summary = "Count list of all ratings when filter by star", description = "[Manager] Count list of rating of a specific Toilet by Toilet ID when filter by star")
    @Parameters(value = {
            @Parameter(name = "toilet-id", description = "A specific Toilet ID", in = ParameterIn.QUERY, example = "6"),
            @Parameter(name = "star", description = "Number of star", in = ParameterIn.QUERY),
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
    @RolesAllowed({RoleConstant.MANAGER, RoleConstant.USER})
    @GetMapping(value = "/count-rating-by-star")
    public ResponseEntity<BaseResponse<Integer>> countTheListRatingWhenFilterByStar(
            @RequestParam(name = "toilet-id", required = false) Integer toiletId,
            @RequestParam(value = "star", required = false) Integer star) {

        int response = ratingService.countTheListRatingWhenFilterByStar(toiletId, star);

        return ResponseBuilder.generateResponse(
                "Count list of rating when filter by star successfully!",
                HttpStatus.OK,
                response
        );
    }


    @Operation(summary = "Filter rating", description = "[Manager, User] Filter rating")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(name = "Filter rating", value = "[\n" +
                            "    {\n" +
                            "      \"id\": 19,\n" +
                            "      \"fullName\": \"Đức Quân\",\n" +
                            "      \"star\": 1,\n" +
                            "      \"comment\": \"Đây là hàng fake\",\n" +
                            "      \"dateTime\": \"26/05/2023 - 04:36:35\",\n" +
                            "      \"imageSources\": [\n" +
                            "        null\n" +
                            "      ],\n" +
                            "      \"avatar\": null,\n" +
                            "      \"status\": \"Đã giải quyết\",\n" +
                            "      \"commonComments\": [\n" +
                            "        \"Thái độ nhân viên kém\",\n" +
                            "        \"Nhà vệ sinh bẩn, hôi\",\n" +
                            "        \"Trang thiết bị hư hỏng\"\n" +
                            "      ]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": 21,\n" +
                            "      \"fullName\": \"Đức Quân\",\n" +
                            "      \"star\": 2,\n" +
                            "      \"comment\": \"Xấu\",\n" +
                            "      \"dateTime\": \"30/05/2023 - 14:40:10\",\n" +
                            "      \"imageSources\": [\n" +
                            "        null\n" +
                            "      ],\n" +
                            "      \"avatar\": null,\n" +
                            "      \"status\": \"Từ chối giải quyết\",\n" +
                            "      \"commonComments\": [\n" +
                            "        \"No common comment\"\n" +
                            "      ]\n" +
                            "    }\n" +
                            "  ]")
            })),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.MANAGER, RoleConstant.USER})
    @GetMapping("/filter-rating")
    public ResponseEntity<BaseResponse<List<RatingResponse>>> filterRating(
            FilterRatingRequest request,
            @ModelAttribute BasePaginationRequest paginationRequest) {

        List<RatingResponse> responses = ratingService.filterRating(request, paginationRequest);

        if (responses.isEmpty()) {
            return ResponseBuilder.generateResponse(
                    "Filter rating successfully!",
                    HttpStatus.NO_CONTENT,
                    responses
            );
        } else {
            return ResponseBuilder.generateResponse(
                    "Filter rating successfully!",
                    HttpStatus.OK,
                    responses
            );
        }
    }

    @Operation(summary = "Count list of all ratings when filter", description = "[Manager, User] Count list of rating of a specific Toilet by Toilet ID when filter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {@ExampleObject(value = "10")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.MANAGER, RoleConstant.USER})
    @GetMapping(value = "/count-rating")
    public ResponseEntity<BaseResponse<Integer>> countFilterRating(
            FilterRatingRequest request) {

        int response = ratingService.countFilterRating(request);

        return ResponseBuilder.generateResponse(
                "Count list of rating when filter successfully!",
                HttpStatus.OK,
                response
        );
    }

    @Operation(summary = "Get rating by rating ID", description = "[Manager] Get a specific rating by rating ID")
    @Parameters(value = {
            @Parameter(name = "rating-id", description = "A specific rating ID", in = ParameterIn.PATH, required = true, example = "1"),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"id\": 20,\n" +
                            "    \"fullName\": null,\n" +
                            "    \"star\": 5,\n" +
                            "    \"comment\": \"Nhà vệ sinh rất sạch, tớ sẽ ghé sử dụng thường xuyên\",\n" +
                            "    \"dateTime\": \"27/05/2023 - 23:37:42\",\n" +
                            "    \"imageSources\": [\n" +
                            "      \"https://meovatchamsocgiadinh.com/ckfinder/userfiles/images/cau-chuyen-ve-chiec-bon-cau-ban-va-cach-xu-ly-ham-cau-bi-day/cau-chuyen-ve-chiec-bon-cau-ban.jpg\",\n" +
                            "      \"https://meovatchamsocgiadinh.com/ckfinder/userfiles/images/cau-chuyen-ve-chiec-bon-cau-ban-va-cach-xu-ly-ham-cau-bi-day/cau-chuyen-ve-chiec-bon-cau-ban.jpg\",\n" +
                            "      \"https://meovatchamsocgiadinh.com/ckfinder/userfiles/images/cau-chuyen-ve-chiec-bon-cau-ban-va-cach-xu-ly-ham-cau-bi-day/cau-chuyen-ve-chiec-bon-cau-ban.jpg\",\n" +
                            "      \"https://meovatchamsocgiadinh.com/ckfinder/userfiles/images/cau-chuyen-ve-chiec-bon-cau-ban-va-cach-xu-ly-ham-cau-bi-day/cau-chuyen-ve-chiec-bon-cau-ban.jpg\",\n" +
                            "      \"https://afamilycdn.com/k:thumb_w/600/h6ZUmntrbWseKUPJv6Yt1NY22jQBtc/Image/2014/12/6-8429e/tu-su-cua-chiec-bon-cau-ban-va-chuyen-di-ve-sinh-cua-be-o-truong.jpg\",\n" +
                            "      \"https://afamilycdn.com/k:thumb_w/600/h6ZUmntrbWseKUPJv6Yt1NY22jQBtc/Image/2014/12/6-8429e/tu-su-cua-chiec-bon-cau-ban-va-chuyen-di-ve-sinh-cua-be-o-truong.jpg\",\n" +
                            "      \"https://afamilycdn.com/k:thumb_w/600/h6ZUmntrbWseKUPJv6Yt1NY22jQBtc/Image/2014/12/6-8429e/tu-su-cua-chiec-bon-cau-ban-va-chuyen-di-ve-sinh-cua-be-o-truong.jpg\",\n" +
                            "      \"https://afamilycdn.com/k:thumb_w/600/h6ZUmntrbWseKUPJv6Yt1NY22jQBtc/Image/2014/12/6-8429e/tu-su-cua-chiec-bon-cau-ban-va-chuyen-di-ve-sinh-cua-be-o-truong.jpg\"\n" +
                            "    ],\n" +
                            "    \"avatar\": null,\n" +
                            "    \"status\": null,\n" +
                            "    \"commonComments\": [\n" +
                            "      \"Thái độ nhân viên kém\",\n" +
                            "      \"Nhà vệ sinh bẩn, hôi\",\n" +
                            "      \"Trang thiết bị hư hỏng\",\n" +
                            "      \"Thiếu nước - giấy vệ sinh\",\n" +
                            "      \"Thái độ nhân viên kém\",\n" +
                            "      \"Nhà vệ sinh bẩn, hôi\",\n" +
                            "      \"Trang thiết bị hư hỏng\",\n" +
                            "      \"Thiếu nước - giấy vệ sinh\"\n" +
                            "    ]\n" +
                            "  }")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.MANAGER})
    @GetMapping(value = "/{rating-id}")
    public ResponseEntity<BaseResponse<RatingResponse>> getRatingByRatingId(@PathVariable("rating-id") int ratingId) {

        RatingResponse response = ratingService.getRatingByRatingId(ratingId);

        return ResponseBuilder.generateResponse(
                "Get rating by rating ID successfully!",
                HttpStatus.OK,
                response
        );
    }
}
