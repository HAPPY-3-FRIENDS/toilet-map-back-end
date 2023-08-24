package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.base.models.BaseResponse;
import com.happy3friends.toiletmapbackend.config.OpenApiConfig;
import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.request.CommonCommentRequest;
import com.happy3friends.toiletmapbackend.response.CommonCommentResponse;
import com.happy3friends.toiletmapbackend.service.CommonCommentService;
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
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Map;

@Tag(name = "Common comment", description = "Common comment API")
@RestController
@RequestMapping(value = "/api/common-comments")
public class CommonCommentController {

    @Autowired
    private CommonCommentService commonCommentService;

    @Operation(summary = "Get all common comment", description = "[Admin, Manager, User] Get the list of all common comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "[\n" +
                            "    {\n" +
                            "      \"id\": 1,\n" +
                            "      \"name\": Thái độ nhân viên kém,\n" +
                            "      \"status\": Hiển thị\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": 2,\n" +
                            "      \"name\": Nhà vệ sinh bẩn, hôi,\n" +
                            "      \"status\": Không hiển thị\n" +
                            "    }\n" +
                            "  ]")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN, RoleConstant.MANAGER, RoleConstant.USER})
    @GetMapping
    public ResponseEntity<BaseResponse<List<CommonCommentResponse>>> getAllCommonComment() {

        List<CommonCommentResponse> responses = commonCommentService.getAllCommonComment();

        return ResponseBuilder.generateResponse(
                "Get list of all common comment successfully!",
                HttpStatus.OK,
                responses
        );
    }

    @Operation(summary = "Create common comment", description = "[Admin] create common comment")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Company Create Request", required = true, content = @Content(
            examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"name\": \"Thiếu nước - giấy vệ sinh\"\n" +
                            "  }")}))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"id\": 1,\n" +
                            "    \"name\": \"Thiếu nước - giấy vệ sinh\",\n" +
                            "    \"status\": \"Hiển thị\" \n" +
                            "}")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN})
    @PostMapping
    public ResponseEntity<BaseResponse<CommonCommentResponse>> createCommonComment(@RequestBody CommonCommentRequest request) {

        CommonCommentResponse responses = commonCommentService.createCommonComment(request);

        return ResponseBuilder.generateResponse(
                "Create common comment successfully!",
                HttpStatus.OK,
                responses
        );
    }

    @Operation(summary = "Update common comment", description = "[Admin] update common comment")
    @Parameter(name = "common-comment-id", description = "A specific common comment ID", in = ParameterIn.PATH, required = true, example = "4")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Fields Request", required = true, content = @Content(
            examples = {
                    @ExampleObject(name = "Update name", value = "{\n" +
                            "  \"name\": \"Trang thiết bị hư hỏng\"\n" +
                            "}"),
                    @ExampleObject(name = "Update status", value = "{\n" +
                            "  \"status\": \"Hiển thị\"\n" +
                            "}")}))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"id\": 1,\n" +
                            "    \"name\": \"Thiếu nước - giấy vệ sinh\",\n" +
                            "    \"status\": \"Hiển thị\" \n" +
                            "}")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN})
    @PatchMapping("/{common-comment-id}")
    public ResponseEntity<BaseResponse<CommonCommentResponse>> updateCommonComment(
            @PathVariable("common-comment-id") Integer id,
            @RequestBody Map<String, Object> fields
    ) {

        CommonCommentResponse responses = commonCommentService.updateCommonComment(id, fields);

        return ResponseBuilder.generateResponse(
                "Update common comment successfully!",
                HttpStatus.OK,
                responses
        );
    }

    @Operation(summary = "Delete common comment", description = "[Admin] Delete common comment")
    @Parameter(name = "common-comment-id", description = "A specific common comment ID", in = ParameterIn.PATH, required = true, example = "4")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"id\": 1\n" +
                            "}")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN})
    @DeleteMapping("/{common-comment-id}")
    public ResponseEntity<BaseResponse<Integer>> deleteCommonComment(
            @PathVariable("common-comment-id") Integer id) {

        boolean isRemoved = commonCommentService.delete(id);

        if (!isRemoved) {
            return ResponseBuilder.generateResponse(
                    "Delete common comment failed!",
                    HttpStatus.NOT_FOUND,
                    id
            );
        }

        return ResponseBuilder.generateResponse(
                "Delete common comment successfully!",
                HttpStatus.OK,
                id
        );
    }

    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN})
    @GetMapping("/{common-comment-id}")
    public ResponseEntity<BaseResponse<CommonCommentResponse>> getCommonCommentById(@PathVariable("common-comment-id") int id) {

        CommonCommentResponse responses = commonCommentService.getCommonCommentById(id);

        return ResponseBuilder.generateResponse(
                "Get common comment by id successfully!",
                HttpStatus.OK,
                responses
        );
    }
}
