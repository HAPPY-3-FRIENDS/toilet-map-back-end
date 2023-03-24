package com.happy3friends.toiletmapbackend.handler;

import com.happy3friends.toiletmapbackend.exception.BadRequestException;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.response.ErrorResponse;
import com.happy3friends.toiletmapbackend.utils.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalServerException(Exception ex) {
        LOGGER.error("An exception occurred: ", ex);
        return ResponseBuilder.generateErrorResponse("Internal Server Error!", HttpStatus.INTERNAL_SERVER_ERROR, Collections.singletonList(String.valueOf(ex)));
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());
        LOGGER.warn("Access Denied: " + details);
        return ResponseBuilder.generateErrorResponse("Access Denied!", HttpStatus.FORBIDDEN, details);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(NotFoundException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());
        LOGGER.warn("Resource not found: " + details);
        return ResponseBuilder.generateErrorResponse("Resource Not Found!", HttpStatus.NOT_FOUND, details);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errorList = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .collect(Collectors.toList());
        ErrorResponse errorResponse = new ErrorResponse(
                DateTimeUtil.getZoneDateTimeNow(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request (Invalid Validation)!",
                errorList
        );
        return handleExceptionInternal(ex, errorResponse, headers, HttpStatus.valueOf(errorResponse.getStatus()), request);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());
        LOGGER.warn("An invalid request was rejected: " + details);
        return ResponseBuilder.generateErrorResponse("Bad Request!", HttpStatus.BAD_REQUEST, details);
    }
}