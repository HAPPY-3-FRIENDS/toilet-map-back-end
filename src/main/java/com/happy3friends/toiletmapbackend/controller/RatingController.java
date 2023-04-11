package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.base.models.BaseResponse;
import com.happy3friends.toiletmapbackend.config.OpenApiConfig;
import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.request.RatingRequest;
import com.happy3friends.toiletmapbackend.response.RatingResponse;
import com.happy3friends.toiletmapbackend.service.RatingService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@Hidden
@Tag(name = "Rating", description = "Rating API")
@RestController
@RequestMapping(value = "/api/rating")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.USER})
    @PostMapping(value = "/toilets/{toilet-id}")
    public ResponseEntity<BaseResponse<RatingResponse>> createRating(
            @PathVariable("toilet-id") int toiletId,
            @RequestBody RatingRequest ratingRequest) {

        RatingResponse response = ratingService.createRating(toiletId, ratingRequest);

        return ResponseBuilder.generateResponse(
                "Create rating successfully!",
                HttpStatus.OK,
                response
        );
    }
}
