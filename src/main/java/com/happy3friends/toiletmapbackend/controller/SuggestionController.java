package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.base.models.BaseResponse;
import com.happy3friends.toiletmapbackend.config.OpenApiConfig;
import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.response.SuggestionResponse;
import com.happy3friends.toiletmapbackend.service.SuggestionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.Map;

@Tag(name = "Suggestion", description = "Suggestion API")
@RestController
@RequestMapping(value = "/api/suggestions")
public class SuggestionController {

    @Autowired
    private SuggestionService suggestionService;

    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.MANAGER})
    @PatchMapping("/{suggestion-id}")
    public ResponseEntity<BaseResponse<SuggestionResponse>> updateSuggestion(
            @PathVariable("suggestion-id") Integer id,
            @RequestBody Map<String, Object> fields) {

        SuggestionResponse response = suggestionService.updateSuggestion(id, fields);

        return ResponseBuilder.generateResponse(
                "Update suggestion successfully!",
                HttpStatus.OK,
                response
        );
    }
}
