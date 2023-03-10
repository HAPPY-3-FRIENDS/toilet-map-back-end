package com.happy3friends.toiletmapbackend.handler;

import com.happy3friends.toiletmapbackend.exception.BadRequestException;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalServerException(Exception ex) {
        LOGGER.error("An exception occurred: ", ex.getMessage());
        return ResponseBuilder.generateErrorResponse("Internal Server Error!", HttpStatus.INTERNAL_SERVER_ERROR, Collections.singletonList(ex.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(NotFoundException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());
        LOGGER.warn("Resource not found: ", ex.getMessage());
        return ResponseBuilder.generateErrorResponse("Resource Not Found!", HttpStatus.NOT_FOUND, details);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());
        LOGGER.warn("An invalid request was rejected: ", ex.getMessage());
        return ResponseBuilder.generateErrorResponse("Bad Requests!", HttpStatus.BAD_REQUEST, details);
    }
}