package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.base.models.BaseResponse;
import com.happy3friends.toiletmapbackend.config.OpenApiConfig;
import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.entity.SensitiveWordEntity;
import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.service.SensitiveWordService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Tag(name = "Sensitive Word", description = "Sensitive Word API")
@RestController
@RequestMapping(value = "/api/sensitive-words")
public class SensitiveWordController {

    @Autowired
    private SensitiveWordService sensitiveWordService;

    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN})
    @GetMapping
    public ResponseEntity<BaseResponse<List<SensitiveWordEntity>>> getAllSensitiveWords(
            @ModelAttribute BasePaginationRequest basePaginationRequest) {

        List<SensitiveWordEntity> responses = sensitiveWordService.getAllSensitiveWords(basePaginationRequest);

        return ResponseBuilder.generateResponse(
                "Get list of all sensitive words successfully!",
                HttpStatus.OK,
                responses
        );
    }

    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN})
    @GetMapping(value = "/count")
    public ResponseEntity<BaseResponse<Integer>> count() {

        int response = sensitiveWordService.count();

        return ResponseBuilder.generateResponse(
                "Count list of all sensitive words successfully!",
                HttpStatus.OK,
                response
        );
    }

    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN})
    @PostMapping
    public ResponseEntity<BaseResponse<String>> create(@RequestBody String word) {

        sensitiveWordService.create(word);

        return ResponseBuilder.generateResponse(
                "Create sensitive word successfully!",
                HttpStatus.CREATED,
                "Created word: " + word
        );
    }

    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN})
    @PutMapping(value = "/{id}")
    public ResponseEntity<BaseResponse<SensitiveWordEntity>> update(@PathVariable("id") int id, @RequestBody String word) {

        SensitiveWordEntity response = sensitiveWordService.update(id, word);

        return ResponseBuilder.generateResponse(
                "Update sensitive word successfully!",
                HttpStatus.OK,
                response
        );
    }

    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN})
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<BaseResponse<String>> delete(@PathVariable("id") int id) {

        sensitiveWordService.delete(id);

        return ResponseBuilder.generateResponse(
                "Delete sensitive word successfully!",
                HttpStatus.OK,
                "Xóa thành công rồi nè hihi!"
        );
    }
}
