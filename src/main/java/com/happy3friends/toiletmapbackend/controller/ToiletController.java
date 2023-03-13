package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.request.CheckInRequest;
import com.happy3friends.toiletmapbackend.response.BaseResponse;
import com.happy3friends.toiletmapbackend.response.CheckInResponse;
import com.happy3friends.toiletmapbackend.service.ToiletService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Toilet", description = "Toilet API")
@RestController
@RequestMapping(value = "/api/toilets")
public class ToiletController {

    @Autowired
    private ToiletService toiletService;

    @GetMapping(value = "/{toiletId}/check-in-histories")
    public ResponseEntity<BaseResponse<List<CheckInResponse>>> toiletCheckInHistoriesByToiletId(@PathVariable("toiletId") int toiletId) {

        List<CheckInResponse> response = toiletService.toiletCheckInHistoriesByToiletId(toiletId);

        return ResponseBuilder.generateResponse(
                "Get list of check-in histories by toiletId successfully!",
                HttpStatus.OK,
                response
        );
    }

    @PostMapping(value = "/{toiletId}/user-check-in")
    public ResponseEntity<BaseResponse<CheckInResponse>> userCheckIn(
            @PathVariable("toiletId") int toiletId,
            @RequestBody CheckInRequest checkInRequest) {

        CheckInResponse response = toiletService.userCheckIn(toiletId, checkInRequest);

        return ResponseBuilder.generateResponse(
                "User check-in toilet-service successfully!",
                HttpStatus.CREATED,
                response
        );
    }
}
