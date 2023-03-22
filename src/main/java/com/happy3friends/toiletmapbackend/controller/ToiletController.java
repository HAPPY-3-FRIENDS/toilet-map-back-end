package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.config.OpenApiConfig;
import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.request.CheckInRequest;
import com.happy3friends.toiletmapbackend.response.BaseResponse;
import com.happy3friends.toiletmapbackend.response.CheckInResponse;
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
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@Tag(name = "Toilet", description = "Toilet API")
@RestController
@RequestMapping(value = "/api/toilets")
public class ToiletController {

    @Autowired
    private ToiletService toiletService;

    @Operation(summary = "Check-in histories", description = "Get the list of check-in histories of a specific toilet by toilet-id")
    @Parameter(name = "toilet-id", description = "A specific toilet ID", in = ParameterIn.PATH, required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                                            "    \"fullName\": \"Huỳnh Lê Thủy Tiên\",\n" +
                                            "    \"dateTime\": \"29/10/2001 - 10:30:00\",\n" +
                                            "    \"serviceName\": \"Đi vệ sinh (Đại tiện)\",\n" +
                                            "    \"paymentMethod\": \"Số dư\",\n" +
                                            "    \"balance\": 4000,\n" +
                                            "    \"turn\": null\n" +
                                            "}")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN, RoleConstant.MANAGER})
    @GetMapping(value = "/{toilet-id}/check-in-histories")
    public ResponseEntity<BaseResponse<List<CheckInResponse>>> toiletCheckInHistoriesByToiletId(@PathVariable("toilet-id") int toiletId) {

        List<CheckInResponse> response = toiletService.toiletCheckInHistoriesByToiletId(toiletId);

        return ResponseBuilder.generateResponse(
                "Get list of check-in histories by toilet-id successfully!",
                HttpStatus.OK,
                response
        );
    }

    @Operation(summary = "User check-in", description = "User check in a specific toilet")
    @Parameter(name = "toilet-id", description = "A specific toilet ID", in = ParameterIn.PATH, required = true, example = "1")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Check-in Request", required = true, content = @Content(
            examples = {
                    @ExampleObject(value = "{\n" +
                            "  \"accountId\": 1,\n" +
                            "  \"serviceName\": \"Đi vệ sinh (tiểu tiện)\",\n" +
                            "  \"datetime\": \"2001-10-29 10:30:00.123456\"\n" +
                            "}")}))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                                            "    \"fullName\": \"Huỳnh Lê Thủy Tiên\",\n" +
                                            "    \"dateTime\": \"29/10/2001 - 10:30:00\",\n" +
                                            "    \"serviceName\": \"Đi vệ sinh (Đại tiện)\",\n" +
                                            "    \"paymentMethod\": \"Số dư\",\n" +
                                            "    \"balance\": 4000,\n" +
                                            "    \"turn\": null\n" +
                                            "}")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed(RoleConstant.STAFF)
    @PostMapping(value = "/{toilet-id}/user/check-in")
    public ResponseEntity<BaseResponse<CheckInResponse>> userCheckIn(
            @PathVariable("toilet-id") int toiletId,
            @RequestBody @Valid CheckInRequest checkInRequest) {

        CheckInResponse response = toiletService.userCheckIn(toiletId, checkInRequest);

        return ResponseBuilder.generateResponse(
                "User check-in toilet successfully!",
                HttpStatus.CREATED,
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
