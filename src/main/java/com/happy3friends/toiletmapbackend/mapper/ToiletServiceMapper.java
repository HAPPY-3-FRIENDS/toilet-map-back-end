package com.happy3friends.toiletmapbackend.mapper;

import com.happy3friends.toiletmapbackend.entity.ToiletServiceEntity;
import com.happy3friends.toiletmapbackend.response.ToiletServiceResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ToiletServiceMapper {

    @Autowired
    private ModelMapper modelMapper;

    public ToiletServiceResponse convertToiletServiceEntityToToiletServiceResponse(ToiletServiceEntity toiletServiceEntity) {
        return Objects.isNull(toiletServiceEntity)
                ? null
                : modelMapper.map(toiletServiceEntity, ToiletServiceResponse.class);
    }
}
