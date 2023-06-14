package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.base.models.BaseResponse;
import com.happy3friends.toiletmapbackend.config.OpenApiConfig;
import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.service.ScriptService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Tag(name = "Script", description = "Script API")
@RestController
@RequestMapping(value = "/api/scripts")
public class ScriptController {

    @Autowired
    private ScriptService scriptService;

    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN, RoleConstant.MANAGER})
    @GetMapping
    public ResponseEntity<BaseResponse<List<String>>> randomUserCheckIn() {

        List<String> response = scriptService.random100UserCheckIn();

        return ResponseBuilder.generateResponse(
                "Random 100 users check-in successfully!",
                HttpStatus.OK,
                response
        );
    }
}
