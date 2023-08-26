package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.entity.ConfigurationEntity;
import com.happy3friends.toiletmapbackend.response.ConfigurationResponse;

import java.util.HashMap;
import java.util.List;

public interface ConfigurationService {
    List<ConfigurationResponse> getAllConfiguration();
    HashMap<String, List<ConfigurationEntity>> getAllConfigurationWithType();

    ConfigurationResponse updateConfiguration(ConfigurationResponse request);

    ConfigurationResponse getConfigById(String configId);
}
