package com.happy3friends.toiletmapbackend.mapper;

import com.happy3friends.toiletmapbackend.entity.ComboEntity;
import com.happy3friends.toiletmapbackend.response.ComboResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ComboMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComboMapper.class);

    @Autowired
    private ModelMapper modelMapper;

    public ComboResponse convertComboEntityToComboResponse(ComboEntity comboEntity) {
        return Objects.isNull(comboEntity)
                ? null
                : modelMapper.map(comboEntity, ComboResponse.class);
    }
}
