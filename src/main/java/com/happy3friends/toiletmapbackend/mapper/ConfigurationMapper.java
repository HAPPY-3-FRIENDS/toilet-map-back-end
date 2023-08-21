package com.happy3friends.toiletmapbackend.mapper;

import com.happy3friends.toiletmapbackend.entity.ConfigurationEntity;
import com.happy3friends.toiletmapbackend.response.ConfigurationResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ConfigurationMapper {

    @Autowired
    private ModelMapper modelMapper;

    public ConfigurationResponse convertConfigurationEntityToConfigurationResponse(ConfigurationEntity configurationEntity) {
        return Objects.isNull(configurationEntity)
                ? null
                : modelMapper.map(configurationEntity, ConfigurationResponse.class);
    }
}
