package com.happy3friends.toiletmapbackend.exception;

import com.happy3friends.toiletmapbackend.enums.ToiletMapErrorCodeEnum;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    private ToiletMapErrorCodeEnum toiletMapErrorCodeEnum;

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(ToiletMapErrorCodeEnum toiletMapErrorCodeEnum, String errorMessage) {
        super(errorMessage);
        this.toiletMapErrorCodeEnum = toiletMapErrorCodeEnum;
    }
}