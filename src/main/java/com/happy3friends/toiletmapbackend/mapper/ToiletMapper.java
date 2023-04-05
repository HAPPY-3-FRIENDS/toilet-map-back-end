package com.happy3friends.toiletmapbackend.mapper;

import com.happy3friends.toiletmapbackend.dto.CustomToiletDTO;
import com.happy3friends.toiletmapbackend.entity.ToiletEntity;
import com.happy3friends.toiletmapbackend.response.ToiletResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ToiletMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ToiletMapper.class);

    @Autowired
    private ModelMapper modelMapper;

    public ToiletResponse convertToiletEntityToToiletResponse(ToiletEntity toiletEntity) {
        return Objects.isNull(toiletEntity)
                ? null
                : modelMapper.map(toiletEntity, ToiletResponse.class);
    }

    public ToiletResponse convertCustomToiletDTOToToiletResponse(CustomToiletDTO customToiletDTO) {
        return Objects.isNull(customToiletDTO)
                ? null
                : modelMapper.map(customToiletDTO, ToiletResponse.class);
    }
}
