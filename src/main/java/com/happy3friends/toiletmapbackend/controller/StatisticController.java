package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.base.models.BaseResponse;
import com.happy3friends.toiletmapbackend.config.OpenApiConfig;
import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.response.StatisticResponse;
import com.happy3friends.toiletmapbackend.service.StatisticService;
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

@Tag(name = "Statistic", description = "Statistic API")
@RestController
@RequestMapping(value = "/api/statistics")
public class StatisticController {

    @Autowired
    private StatisticService statisticService;

    @Operation(summary = "Get all statistics",
            description = "- [Manager] Get a list of all statistics by company ID (All toilets in this company, with pagination and default sort by Total revenue desc)\n" +
                    "- [Manager] Get a list of all statistics by toilet ID (All services in this toilet)\n" +
                    "- [Admin] Default is get all statistics in the system (All companies in this system, with pagination and default sort by Total revenue desc)\n" +
                    "- Default from date and to date is current month")
    @Parameters(value = {
            @Parameter(name = "company-id", description = "A specific company ID", in = ParameterIn.QUERY),
            @Parameter(name = "toilet-id", description = "A specific toilet ID", in = ParameterIn.QUERY),
            @Parameter(name = "from-date", description = "Start of the period (dd/MM/yyyy)", in = ParameterIn.QUERY, allowReserved = true),
            @Parameter(name = "to-date", description = "End of the period (dd/MM/yyyy)", in = ParameterIn.QUERY, allowReserved = true),
            @Parameter(name = "sort",
                    in = ParameterIn.QUERY,
                    description = "Sorting criteria in the format: property(,asc|desc). Default sort order is descending by Total revenue. Multiple sort criteria are supported.",
                    array = @ArraySchema(schema = @Schema(implementation = String.class), maxItems = 5),
                    allowReserved = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(name = "Get a list of all statistics by company ID", value = "[\n" +
                            "    {\n" +
                            "      \"toiletId\": 5,\n" +
                            "      \"toiletName\": \"Nhà vệ sinh lưu động số 2\",\n" +
                            "      \"totalRevenue\": 0,\n" +
                            "      \"walkInGuestRevenue\": 0,\n" +
                            "      \"walkInGuestCount\": 0,\n" +
                            "      \"usingTurnRevenue\": 0,\n" +
                            "      \"usingTurnCount\": 0\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"toiletId\": 4,\n" +
                            "      \"toiletName\": \"Nhà vệ sinh lưu động số 1\",\n" +
                            "      \"totalRevenue\": 40000,\n" +
                            "      \"walkInGuestRevenue\": 10000,\n" +
                            "      \"walkInGuestCount\": 3,\n" +
                            "      \"usingTurnRevenue\": 30000,\n" +
                            "      \"usingTurnCount\": 8\n" +
                            "    }\n" +
                            "]"),
                    @ExampleObject(name = "Get a list of all statistics by toilet ID", value = "[\n" +
                            "    {\n" +
                            "      \"serviceName\": \"Đi vệ sinh (tiểu tiện)\",\n" +
                            "      \"totalRevenue\": 8000,\n" +
                            "      \"walkInGuestRevenue\": 2000,\n" +
                            "      \"walkInGuestCount\": 1,\n" +
                            "      \"usingTurnRevenue\": 6000,\n" +
                            "      \"usingTurnCount\": 2\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"serviceName\": \"Đi vệ sinh (đại tiện)\",\n" +
                            "      \"totalRevenue\": 14000,\n" +
                            "      \"walkInGuestRevenue\": 8000,\n" +
                            "      \"walkInGuestCount\": 2,\n" +
                            "      \"usingTurnRevenue\": 6000,\n" +
                            "      \"usingTurnCount\": 1\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"serviceName\": \"Đi tắm\",\n" +
                            "      \"totalRevenue\": 0,\n" +
                            "      \"walkInGuestRevenue\": 0,\n" +
                            "      \"walkInGuestCount\": 0,\n" +
                            "      \"usingTurnRevenue\": 0,\n" +
                            "      \"usingTurnCount\": 0\n" +
                            "    }\n" +
                            "]"),
                    @ExampleObject(name = "Get a list of all statistics of all companies in the system", value = "[\n" +
                            "    {\n" +
                            "      \"companyId\": 2,\n" +
                            "      \"companyName\": \"Công ty dịch vụ công ích quận 1\",\n" +
                            "      \"totalRevenue\": 40000,\n" +
                            "      \"walkInGuestRevenue\": 10000,\n" +
                            "      \"walkInGuestCount\": 3,\n" +
                            "      \"usingTurnRevenue\": 30000,\n" +
                            "      \"usingTurnCount\": 8\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"companyId\": 2,\n" +
                            "      \"companyName\": \"Công ty dịch vụ công ích quận 1\",\n" +
                            "      \"totalRevenue\": 40000,\n" +
                            "      \"walkInGuestRevenue\": 10000,\n" +
                            "      \"walkInGuestCount\": 3,\n" +
                            "      \"usingTurnRevenue\": 30000,\n" +
                            "      \"usingTurnCount\": 8\n" +
                            "    }\n" +
                            "]")
            })),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN, RoleConstant.MANAGER})
    @GetMapping
    public ResponseEntity<BaseResponse<List<StatisticResponse>>> getAllStatistics(
            @RequestParam(value = "company-id", required = false) Integer companyId,
            @RequestParam(value = "toilet-id", required = false) Integer toiletId,
            @RequestParam(value = "from-date", required = false) String fromStrDate,
            @RequestParam(value = "to-date", required = false) String toStrDate,
            @ModelAttribute BasePaginationRequest paginationRequest) {

        List<StatisticResponse> responses = statisticService.getAllStatistics(
                companyId,
                toiletId,
                fromStrDate,
                toStrDate,
                paginationRequest);

        return ResponseBuilder.generateResponse(
                "Get list of all statistics successfully!",
                HttpStatus.OK,
                responses
        );
    }

    @Operation(summary = "Get total statistic of month",
            description = "- [Manager] Get total statistic of month by company ID (All toilets in this company)\n" +
                    "- [Manager] Get total statistic of month by toilet ID (All services in this toilet)\n" +
                    "- [Admin] Default is get total statistic of month in the system (All companies in this system)\n" +
                    "- From date and to date is current month")
    @Parameters(value = {
            @Parameter(name = "company-id", description = "A specific company ID", in = ParameterIn.QUERY),
            @Parameter(name = "toilet-id", description = "A specific toilet ID", in = ParameterIn.QUERY)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"totalRevenue\": 84000,\n" +
                            "    \"totalTurn\": 25\n" +
                            "}")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN, RoleConstant.MANAGER})
    @GetMapping(value = "/total/month")
    public ResponseEntity<BaseResponse<StatisticResponse>> getTotalStatisticByMonth(
            @RequestParam(value = "company-id", required = false) Integer companyId,
            @RequestParam(value = "toilet-id", required = false) Integer toiletId) {

        StatisticResponse response = statisticService.getTotalStatisticOfMonth(companyId, toiletId);

        return ResponseBuilder.generateResponse(
                "Get total statistic by month successfully!",
                HttpStatus.OK,
                response
        );
    }

    @Operation(summary = "Count all statistics",
            description = "- [Manager] Count a list of all statistics by company ID (All toilets in this company)\n" +
                    "- [Manager] Count a list of all statistics by toilet ID (All services in this toilet)\n" +
                    "- [Admin] Default is get all statistics in the system (All companies in this system)\n" +
                    "- Default from date and to date is current month")
    @Parameters(value = {
            @Parameter(name = "company-id", description = "A specific Company ID", in = ParameterIn.QUERY, example = "6"),
            @Parameter(name = "toilet-id", description = "A specific Toilet ID", in = ParameterIn.QUERY, example = "6"),
            @Parameter(name = "from-date", description = "Start of the period (dd/MM/yyyy)", in = ParameterIn.QUERY, allowReserved = true),
            @Parameter(name = "to-date", description = "End of the period (dd/MM/yyyy)", in = ParameterIn.QUERY, allowReserved = true)
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
    @RolesAllowed({RoleConstant.ADMIN, RoleConstant.MANAGER})
    @GetMapping(value = "/count")
    public ResponseEntity<BaseResponse<Integer>> count(
            @RequestParam(value = "company-id", required = false) Integer companyId,
            @RequestParam(value = "toilet-id", required = false) Integer toiletId,
            @RequestParam(value = "from-date", required = false) String fromStrDate,
            @RequestParam(value = "to-date", required = false) String toStrDate) {

        int response = statisticService.count(companyId ,toiletId, fromStrDate, toStrDate);

        if (toiletId != null) {
            return ResponseBuilder.generateResponse(
                    "Count list of statistic by Company ID and Toilet ID successfully!",
                    HttpStatus.OK,
                    response
            );
        }

        return ResponseBuilder.generateResponse(
                "Count list of statistic successfully!",
                HttpStatus.OK,
                response
        );
    }
}
