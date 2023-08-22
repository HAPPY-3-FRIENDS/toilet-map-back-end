package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.base.models.BaseResponse;
import com.happy3friends.toiletmapbackend.config.OpenApiConfig;
import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.response.ConfigurationResponse;
import com.happy3friends.toiletmapbackend.service.ConfigurationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Tag(name = "Configuration", description = "Configuration API")
@RestController
@RequestMapping(value = "/api/configurations")
public class ConfigurationController {
    @Autowired
    private ConfigurationService configurationService;

    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN})
    @GetMapping
    public ResponseEntity<BaseResponse<List<ConfigurationResponse>>> getAllConfiguration() {
        List<ConfigurationResponse> responses = configurationService.getAllConfiguration();

        return ResponseBuilder.generateResponse(
                "Get list of configurations successfully!",
                HttpStatus.OK,
                responses
        );
    }

    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN})
    @PutMapping
    public ResponseEntity<BaseResponse<ConfigurationResponse>> updateConfiguration(@RequestBody ConfigurationResponse request) {
        ConfigurationResponse responses = configurationService.updateConfiguration(request);

        return ResponseBuilder.generateResponse(
                "Update configuration successfully!",
                HttpStatus.OK,
                responses
        );
    }

    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN})
    @GetMapping("/{config-id}")
    public ResponseEntity<BaseResponse<ConfigurationResponse>> getConfigById(@PathVariable("config-id") String configId) {
        ConfigurationResponse responses = configurationService.getConfigById(configId);

        return ResponseBuilder.generateResponse(
                "Get configuration by id successfully!",
                HttpStatus.OK,
                responses
        );
    }
}
