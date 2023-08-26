package com.happy3friends.toiletmapbackend.exception;

import com.happy3friends.toiletmapbackend.enums.ToiletMapErrorCodeEnum;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {
    private ToiletMapErrorCodeEnum toiletMapErrorCodeEnum;

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(ToiletMapErrorCodeEnum toiletMapErrorCodeEnum, String errorMessage) {
        super(errorMessage);
        this.toiletMapErrorCodeEnum = toiletMapErrorCodeEnum;
    }
}
