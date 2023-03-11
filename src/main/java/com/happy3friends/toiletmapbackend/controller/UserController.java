package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.request.CheckInRequest;
import com.happy3friends.toiletmapbackend.response.BaseResponse;
import com.happy3friends.toiletmapbackend.response.CheckInResponse;
import com.happy3friends.toiletmapbackend.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "User API")
@RestController
@RequestMapping(value = "/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/{userId}/check-in")
    public ResponseEntity<BaseResponse<CheckInResponse>> checkIn(
            @PathVariable("userId") int userId,
            @RequestBody CheckInRequest checkInRequest) {

        CheckInResponse response = userService.checkIn(userId, checkInRequest);

        return ResponseBuilder.generateResponse(
                "User check-in toilet-service successfully!",
                HttpStatus.CREATED,
                response
        );
    }
}
