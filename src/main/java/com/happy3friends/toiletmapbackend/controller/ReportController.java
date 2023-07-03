package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.base.models.BaseResponse;
import com.happy3friends.toiletmapbackend.config.OpenApiConfig;
import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.request.CreateReportRequest;
import com.happy3friends.toiletmapbackend.response.CreateReportResponse;
import com.happy3friends.toiletmapbackend.response.ReportResponse;
import com.happy3friends.toiletmapbackend.response.ReportResponseForManager;
import com.happy3friends.toiletmapbackend.service.ReportService;
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

@Tag(name = "Report", description = "Report API")
@RestController
@RequestMapping(value = "/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN, RoleConstant.MANAGER})
    @GetMapping
    public ResponseEntity<BaseResponse<List<ReportResponse>>> getReports(
            @ModelAttribute BasePaginationRequest paginationRequest) {

        List<ReportResponse> responses = reportService.getReports(paginationRequest);

        return ResponseBuilder.generateResponse(
                "Get list of all reports successfully!",
                HttpStatus.OK,
                responses
        );
    }

    @Operation(summary = "Create report", description = "[User] create report")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Report Request", required = true, content = @Content(
            examples = {
                    @ExampleObject(value = "{\n" +
                            "  \"toiletId\": 4,\n" +
                            "  \"message\": \"Nhà vệ sinh đóng cửa\"\n" +
                            "}")}))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"id\": 4,\n" +
                            "    \"toiletId\": 4,\n" +
                            "    \"message\": \"Nhà vệ sinh đóng cửa\",\n" +
                            "    \"status\": \"Chưa xử lí\"\n" +
                            "  }")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.USER})
    @PostMapping
    public ResponseEntity<BaseResponse<CreateReportResponse>> createReport(
            @RequestBody CreateReportRequest request) {

        CreateReportResponse responses = reportService.createReport(request);

        return ResponseBuilder.generateResponse(
                "Create report successfully!",
                HttpStatus.OK,
                responses
        );
    }

    @Operation(summary = "Update report", description = "[Admin, Manager] update report")
    @Parameter(name = "report-id", description = "A specific report ID", in = ParameterIn.PATH, required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"id\": 3,\n" +
                            "    \"toiletId\": 4,\n" +
                            "    \"message\": \"Nhà vệ sinh không tồn tại\",\n" +
                            "    \"status\": \"Đã từ chối\"\n" +
                            "  }")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN, RoleConstant.MANAGER})
    @PutMapping(value = "/{report-id}")
    public ResponseEntity<BaseResponse<CreateReportResponse>> updateStatus(
            @PathVariable("report-id") int id,
            String message) {

        CreateReportResponse responses = reportService.updateStatus(id, message);

        return ResponseBuilder.generateResponse(
                "Update report successfully!",
                HttpStatus.OK,
                responses
        );
    }

    @Operation(summary = "Get reports", description = "[Manager] get list reports by company id")
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.MANAGER})
    @GetMapping(value = "/{company-id}")
    public ResponseEntity<BaseResponse<List<ReportResponseForManager>>> getReportsForManager(
            @PathVariable("company-id") int id,
            @ModelAttribute BasePaginationRequest paginationRequest) {

        List<ReportResponseForManager> responses = reportService.getReportsForManager(id ,paginationRequest);

        return ResponseBuilder.generateResponse(
                "Get list of all reports successfully!",
                HttpStatus.OK,
                responses
        );
    }
}
