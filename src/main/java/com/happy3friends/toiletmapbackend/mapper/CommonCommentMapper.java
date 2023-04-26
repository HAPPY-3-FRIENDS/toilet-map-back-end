package com.happy3friends.toiletmapbackend.mapper;

import com.happy3friends.toiletmapbackend.entity.CommonCommentEntity;
import com.happy3friends.toiletmapbackend.response.ComboResponse;
import com.happy3friends.toiletmapbackend.response.CommonCommentResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CommonCommentMapper {

    @Autowired
    private ModelMapper modelMapper;


    public CommonCommentResponse convertCommonCommentEntityToCommonCommentResponse(CommonCommentEntity commonCommentEntity) {
        return Objects.isNull(commonCommentEntity)
                ? null
                : modelMapper.map(commonCommentEntity, CommonCommentResponse.class);
    }
}
