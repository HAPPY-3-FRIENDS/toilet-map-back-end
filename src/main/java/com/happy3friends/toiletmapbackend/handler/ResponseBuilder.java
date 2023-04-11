package com.happy3friends.toiletmapbackend.handler;

import com.happy3friends.toiletmapbackend.base.models.BaseResponse;
import com.happy3friends.toiletmapbackend.response.ErrorResponse;
import com.happy3friends.toiletmapbackend.utils.DateTimeUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseBuilder {
    public static <T> ResponseEntity<BaseResponse<T>> generateResponse(String message, HttpStatus httpStatus, T responseObj) {
        BaseResponse response = new BaseResponse(message, httpStatus.value(), responseObj);

        return new ResponseEntity<>(response, httpStatus);
    }

    public static ResponseEntity<ErrorResponse> generateErrorResponse(String message, HttpStatus httpStatus, List<String> errors) {
        ErrorResponse errorResponse = new ErrorResponse(
                DateTimeUtil.getZoneDateTimeNow(),
                httpStatus.value(),
                message,
                errors
        );
        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}
