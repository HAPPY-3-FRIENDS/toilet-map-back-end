package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.base.models.BaseResponse;
import com.happy3friends.toiletmapbackend.config.OpenApiConfig;
import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.request.CheckInFullAToiletRequest;
import com.happy3friends.toiletmapbackend.request.CheckInScriptRequest;
import com.happy3friends.toiletmapbackend.response.CheckInFullAToiletResponse;
import com.happy3friends.toiletmapbackend.response.CheckInScriptResponse;
import com.happy3friends.toiletmapbackend.response.SuggestionSchedulerResponse;
import com.happy3friends.toiletmapbackend.service.ScriptService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.text.ParseException;
import java.util.List;

@Tag(name = "Script", description = "Script API")
@RestController
@RequestMapping(value = "/api/scripts")
public class ScriptController {

    @Autowired
    private ScriptService scriptService;

    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN, RoleConstant.MANAGER})
    @PostMapping("/check-in")
    public ResponseEntity<BaseResponse<CheckInScriptResponse>> randomUserCheckIn(@RequestBody CheckInScriptRequest request) {

        CheckInScriptResponse response = scriptService.randomUserCheckIn(request);

        return ResponseBuilder.generateResponse(
                "Random user check-in successfully!",
                HttpStatus.OK,
                response
        );
    }

    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN, RoleConstant.MANAGER})
    @PostMapping
    public ResponseEntity<BaseResponse<CheckInFullAToiletResponse>> checkInFullAToiletRequest(CheckInFullAToiletRequest request) {

        CheckInFullAToiletResponse response = scriptService.checkInFullAToilet(request);

        return ResponseBuilder.generateResponse(
                "Check-in full a toilet successfully!",
                HttpStatus.OK,
                response
        );
    }

    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN})
    @GetMapping("/checkout")
    public ResponseEntity<BaseResponse<List<String>>> checkout(@Param("toilet-id") int toiletId) {

        List<String> response = scriptService.checkout(toiletId);

        return ResponseBuilder.generateResponse(
                "Checkout all user successfully!",
                HttpStatus.OK,
                response
        );
    }

    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN, RoleConstant.MANAGER})
    @PostMapping("/manual-scheduler/{date}")
    public ResponseEntity<BaseResponse<List<SuggestionSchedulerResponse>>> runScheduler(@PathVariable("date") String date) throws ParseException {

        List<SuggestionSchedulerResponse> response = scriptService.runScheduler(date);

        return ResponseBuilder.generateResponse(
                "Run scheduler successfully!",
                HttpStatus.OK,
                response
        );
    }
}
