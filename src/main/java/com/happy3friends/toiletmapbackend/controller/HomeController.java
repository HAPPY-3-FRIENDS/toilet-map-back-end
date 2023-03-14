package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Home", description = "Home API")
@RestController
@RequestMapping(value = "/api")
public class HomeController {

    @Operation(summary = "Home API", description = "Test API", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = {@ExampleObject(name = "boo", value = "example",summary = "example of boo", externalValue = "example of external value")})),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!"),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!")
    })
    @GetMapping(value = "/")
    public ResponseEntity<BaseResponse<String>> home() {

        return ResponseBuilder.generateResponse(
                "Successfully!",
                HttpStatus.OK,
                "Toilet Map API - tienhuynh-tn - Test 2"
        );
    }
}
