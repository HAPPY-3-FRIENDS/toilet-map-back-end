package com.happy3friends.toiletmapbackend.mapper;

import com.happy3friends.toiletmapbackend.dto.ToiletImageDTO;
import com.happy3friends.toiletmapbackend.entity.ToiletImageEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ToiletImageMapper {

    @Autowired
    private ModelMapper modelMapper;

    public ToiletImageEntity convertToiletImageDTOToToiletImageEntity(ToiletImageDTO toiletImageDTO) {
        return Objects.isNull(toiletImageDTO)
                ? null
                : modelMapper.map(toiletImageDTO, ToiletImageEntity.class);
    }
}
