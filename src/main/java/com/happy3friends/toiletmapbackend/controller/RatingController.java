package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
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
import java.util.List;

@Tag(name = "Rating", description = "Rating API")
@RestController
@RequestMapping(value = "/api/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @Hidden
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.USER})
    @PostMapping
    public ResponseEntity<BaseResponse<RatingResponse>> createRating(@RequestBody RatingRequest ratingRequest) {

        RatingResponse response = ratingService.createRating(ratingRequest);

        return ResponseBuilder.generateResponse(
                "Create rating successfully!",
                HttpStatus.OK,
                response
        );
    }

    @Hidden
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.MANAGER, RoleConstant.USER})
    @GetMapping
    public ResponseEntity<BaseResponse<List<RatingResponse>>> getAllRatings(
            @RequestParam(value = "toilet-id", required = false) Integer toiletId,
            @ModelAttribute BasePaginationRequest paginationRequest) {

        List<RatingResponse> responses = ratingService.getAllRatings(toiletId, paginationRequest);

        return ResponseBuilder.generateResponse(
                "Get list of all ratings successfully!",
                HttpStatus.OK,
                responses
        );
    }
}
