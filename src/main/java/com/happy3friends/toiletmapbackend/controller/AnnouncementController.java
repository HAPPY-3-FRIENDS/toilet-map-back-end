package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.base.models.BaseResponse;
import com.happy3friends.toiletmapbackend.config.OpenApiConfig;
import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.request.CreateAnnouncementRequest;
import com.happy3friends.toiletmapbackend.response.AnnouncementResponse;
import com.happy3friends.toiletmapbackend.service.AnnouncementService;
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

@Tag(name = "Announcement", description = "Announcement API")
@RestController
@RequestMapping(value = "/api/announcements")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @Operation(summary = "Get all announcements", description = "[Admin, User] Get a list of all announcements")
    @Parameter(name = "announcement-type", description = "A type of announcement", in = ParameterIn.QUERY)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(name = "Get all announcements", value = "[\n" +
                            "    {\n" +
                            "      \"id\": 2,\n" +
                            "      \"title\": \"Lễ nên cho free\",\n" +
                            "      \"url\": \"Url\",\n" +
                            "      \"imageSource\": \"ImageSource\",\n" +
                            "      \"startDate\": \"2023-06-03\",\n" +
                            "      \"endDate\": \"2023-06-04\",\n" +
                            "      \"description\": \"Description nè\",\n" +
                            "      \"type\": \"Internal\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": 1,\n" +
                            "      \"title\": \"Tra cứu hóa đơn tiền rác\",\n" +
                            "      \"url\": \"Url\",\n" +
                            "      \"imageSource\": \"ImageSource\",\n" +
                            "      \"startDate\": \"2023-06-03\",\n" +
                            "      \"endDate\": \"2023-06-07\",\n" +
                            "      \"description\": \"Description nè\",\n" +
                            "      \"type\": \"External\"\n" +
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
    @RolesAllowed({RoleConstant.ADMIN, RoleConstant.MANAGER, RoleConstant.STAFF, RoleConstant.TOILET, RoleConstant.USER})
    @GetMapping
    public ResponseEntity<BaseResponse<List<AnnouncementResponse>>> getAllAnnouncements(
            @RequestParam(value = "announcement-type", required = false) String announcementType,
            @ModelAttribute BasePaginationRequest paginationRequest) {

        List<AnnouncementResponse> responses = announcementService.getAllAnnouncements(announcementType, paginationRequest);

        if (announcementType != null) {
            return ResponseBuilder.generateResponse(
                    "Get list of all announcements by type successfully!",
                    HttpStatus.OK,
                    responses
            );
        }

        return ResponseBuilder.generateResponse(
                "Get list of all announcements successfully!",
                HttpStatus.OK,
                responses
        );
    }


    @Operation(summary = "Count list of all announcements", description = "[Admin, User] Count list of announcements by type")
    @Parameter(name = "announcement-type", description = "A type of announcement", in = ParameterIn.QUERY, example = "External")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {@ExampleObject(value = "10")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN, RoleConstant.MANAGER, RoleConstant.STAFF, RoleConstant.TOILET,  RoleConstant.USER})
    @GetMapping(value = "/count")
    public ResponseEntity<BaseResponse<Integer>> count(
            @RequestParam(value = "announcement-type", required = false) String announcementType) {

        int response = announcementService.count(announcementType);

        if (announcementType != null) {
            return ResponseBuilder.generateResponse(
                    "Count list of announcement by type successfully!",
                    HttpStatus.OK,
                    response
            );
        }

        return ResponseBuilder.generateResponse(
                "Count list of announcement successfully!",
                HttpStatus.OK,
                response
        );
    }

    @Operation(summary = "Get announcement info by id", description = "[Admin, User] Get announcement info by id")
    @Parameter(name = "announcement-id", description = "A specific announcement ID", in = ParameterIn.PATH, required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"id\": 1,\n" +
                            "    \"title\": \"Tra cứu hóa đơn tiền rác\",\n" +
                            "    \"url\": \"Url\",\n" +
                            "    \"imageSource\": \"ImageSource\",\n" +
                            "    \"startDate\": \"2023-06-03\",\n" +
                            "    \"endDate\": \"2023-06-07\",\n" +
                            "    \"description\": \"Description nè\",\n" +
                            "    \"type\": \"External\"\n" +
                            "  }")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN, RoleConstant.MANAGER, RoleConstant.STAFF, RoleConstant.TOILET,  RoleConstant.USER})
    @GetMapping(value = "/{announcement-id}")
    public ResponseEntity<BaseResponse<AnnouncementResponse>> getAnnouncementById(
            @PathVariable("announcement-id") int announcementId) {

        AnnouncementResponse responses = announcementService.getAnnouncementById(announcementId);

        return ResponseBuilder.generateResponse(
                "Get list of all announcements successfully!",
                HttpStatus.OK,
                responses
        );
    }

    @Operation(summary = "Update announcement", description = "[Admin] update announcement")
    @Parameter(name = "announcement-id", description = "A specific announcement ID", in = ParameterIn.PATH, required = true, example = "1")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Fields Request", required = true, content = @Content(
            examples = {
                    @ExampleObject(name = "Update announcement", value = "{\n" +
                            "  \"title\": \"Update nè\",\n" +
                            "  \"url\": \"Url nè\",\n" +
                            "  \"startDate\": \"2023-06-04\",\n" +
                            "  \"endDate\": \"2023-06-10\",\n" +
                            "  \"type\": \"External\"\n" +
                            "}")}))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"id\": 1,\n" +
                            "    \"title\": \"Update nè\",\n" +
                            "    \"url\": \"Url nè\",\n" +
                            "    \"imageSource\": \"ImageSource\",\n" +
                            "    \"startDate\": \"2023-06-03T17:00:00.000+00:00\",\n" +
                            "    \"endDate\": \"2023-06-09T17:00:00.000+00:00\",\n" +
                            "    \"description\": \"Description nè\",\n" +
                            "    \"type\": \"External\"\n" +
                            "  }")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN, RoleConstant.MANAGER, RoleConstant.STAFF, RoleConstant.TOILET})
    @PatchMapping("/{announcement-id}")
    public ResponseEntity<BaseResponse<AnnouncementResponse>> updateAnnouncement(
            @PathVariable("announcement-id") int announcementId,
            @RequestBody Map<String, Object> fields) {

        AnnouncementResponse responses = announcementService.updateAnnouncement(announcementId, fields);

        return ResponseBuilder.generateResponse(
                "Update announcement successfully!",
                HttpStatus.OK,
                responses
        );
    }

    @Operation(summary = "Create announcement", description = "[Admin] create announcement")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Announcement Request", required = true, content = @Content(
            examples = {
                    @ExampleObject(value = "{\n" +
                            "  \"title\": \"Create nè\",\n" +
                            "  \"url\": \"Url\",\n" +
                            "  \"imageSource\": \"Image\",\n" +
                            "  \"startDate\": \"2023-06-04\",\n" +
                            "  \"endDate\": \"2023-06-10\",\n" +
                            "  \"description\": \"Description\",\n" +
                            "  \"type\": \"Internal\"\n" +
                            "}")}))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"id\": 6,\n" +
                            "    \"title\": \"Create nè\",\n" +
                            "    \"url\": \"Url\",\n" +
                            "    \"imageSource\": \"Image\",\n" +
                            "    \"startDate\": \"2023-06-04T00:00:00.000+00:00\",\n" +
                            "    \"endDate\": \"2023-06-10T00:00:00.000+00:00\",\n" +
                            "    \"description\": \"Description\",\n" +
                            "    \"type\": \"Internal\"\n" +
                            "  }")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN,  RoleConstant.MANAGER, RoleConstant.STAFF, RoleConstant.TOILET})
    @PostMapping
    public ResponseEntity<BaseResponse<AnnouncementResponse>> createAnnouncement(@RequestBody CreateAnnouncementRequest request) {

        AnnouncementResponse responses = announcementService.createAnnouncement(request);

        return ResponseBuilder.generateResponse(
                "Create announcement successfully!",
                HttpStatus.OK,
                responses
        );
    }

    @Operation(summary = "Delete announcement", description = "[Admin] Delete announcement")
    @Parameter(name = "announcement-id", description = "A specific announcement ID", in = ParameterIn.PATH, required = true, example = "4")
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
    @RolesAllowed({RoleConstant.ADMIN,  RoleConstant.MANAGER, RoleConstant.STAFF, RoleConstant.TOILET})
    @DeleteMapping("/{announcement-id}")
    public ResponseEntity<BaseResponse<Integer>> deleteAnnouncement(
            @PathVariable("announcement-id") Integer id) {

        boolean isRemoved = announcementService.delete(id);

        if (!isRemoved) {
            return ResponseBuilder.generateResponse(
                    "Delete announcement failed!",
                    HttpStatus.NOT_FOUND,
                    id
            );
        }

        return ResponseBuilder.generateResponse(
                "Delete announcement successfully!",
                HttpStatus.OK,
                id
        );
    }
}
