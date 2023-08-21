package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.entity.ConfigurationEntity;
import com.happy3friends.toiletmapbackend.enums.ToiletMapErrorCodeEnum;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.ConfigurationMapper;
import com.happy3friends.toiletmapbackend.repository.ConfigurationRepository;
import com.happy3friends.toiletmapbackend.response.ConfigurationResponse;
import com.happy3friends.toiletmapbackend.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private ConfigurationMapper configurationMapper;

    @Override
    public List<ConfigurationResponse> getAllConfiguration() {
        List<ConfigurationEntity> configurationEntities = configurationRepository.findAll();
        if (configurationEntities.isEmpty())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_CONFIGURATION, ToiletMapErrorCodeEnum.NOT_FOUND_CONFIGURATION.getMessage());

        return configurationEntities.stream()
                .map(entity -> configurationMapper.convertConfigurationEntityToConfigurationResponse(entity))
                .collect(Collectors.toList());
    }

    @Override
    public ConfigurationResponse updateConfiguration(ConfigurationResponse request) {
        ConfigurationEntity entity = configurationRepository.getConfigById(request.getId());
        if (Objects.isNull(entity)) {
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_CONFIGURATION, ToiletMapErrorCodeEnum.NOT_FOUND_CONFIGURATION.getMessage());
        }

        entity.setValue(request.getValue());
        configurationRepository.save(entity);

        return configurationMapper.convertConfigurationEntityToConfigurationResponse(entity);
    }
}
