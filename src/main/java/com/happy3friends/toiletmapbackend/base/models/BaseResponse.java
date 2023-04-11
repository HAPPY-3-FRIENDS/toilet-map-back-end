package com.happy3friends.toiletmapbackend.base.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {
    private String message;
    private int status;
    private T data;
}
