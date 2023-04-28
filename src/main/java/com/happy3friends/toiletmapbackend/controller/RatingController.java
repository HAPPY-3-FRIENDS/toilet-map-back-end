package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.base.models.BaseResponse;
import com.happy3friends.toiletmapbackend.config.OpenApiConfig;
import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
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

@Tag(name = "Rating", description = "Rating API")
@RestController
@RequestMapping(value = "/api/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @Operation(summary = "Create a rating", description = "[User] Create a rating and its images")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Toilet Create Request", required = true, content = @Content(
            examples = {
                    @ExampleObject(value = "{\n" +
                            "  \"toiletId\": 4,\n" +
                            "  \"star\": 5,\n" +
                            "  \"comment\": \"Nhà vệ sinh rất sạch, tớ sẽ ghé sử dụng thường xuyên\",\n" +
                            "  \"accountId\": 6,\n" +
                            "  \"checkInId\": 6,\n" +
                            "  \"imageSources\": [\n" +
                            "    \"https://meovatchamsocgiadinh.com/ckfinder/userfiles/images/cau-chuyen-ve-chiec-bon-cau-ban-va-cach-xu-ly-ham-cau-bi-day/cau-chuyen-ve-chiec-bon-cau-ban.jpg\",\n" +
                            "    \"https://afamilycdn.com/k:thumb_w/600/h6ZUmntrbWseKUPJv6Yt1NY22jQBtc/Image/2014/12/6-8429e/tu-su-cua-chiec-bon-cau-ban-va-chuyen-di-ve-sinh-cua-be-o-truong.jpg\"\n" +
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
}
