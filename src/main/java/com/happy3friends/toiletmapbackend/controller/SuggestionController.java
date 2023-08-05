package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.base.models.BaseResponse;
import com.happy3friends.toiletmapbackend.config.OpenApiConfig;
import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.response.SuggestionAdminResponse;
import com.happy3friends.toiletmapbackend.service.SuggestionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Tag(name = "Suggestion", description = "Suggestion API")
@RestController
@RequestMapping(value = "/api/suggestions")
public class SuggestionController {

    @Autowired
    private SuggestionService suggestionService;

    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.MANAGER})
    @PutMapping("/acceptance")
    public ResponseEntity<BaseResponse<Object>> updateAcceptedSuggestion(
            @RequestParam(name = "suggestion-ids") List<Integer> suggestionIds,
            @RequestParam(name = "is-accepted") Boolean isAccepted) {

        suggestionService.updateAcceptedSuggestion(suggestionIds, isAccepted);

        return ResponseBuilder.generateResponse(
                "Update suggestion successfully!",
                HttpStatus.OK,
                null
        );
    }

    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN})
    @GetMapping
    public ResponseEntity<BaseResponse<List<SuggestionAdminResponse>>> getListOfSuggestions() {

        List<SuggestionAdminResponse> response = suggestionService.getListOfSuggestions();

        return ResponseBuilder.generateResponse(
                "Get list of suggestions successfully!",
                HttpStatus.OK,
                response
        );
    }
}
