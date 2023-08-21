package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.response.ConfigurationResponse;

import java.util.List;

public interface ConfigurationService {
    List<ConfigurationResponse> getAllConfiguration();

    ConfigurationResponse updateConfiguration(ConfigurationResponse request);
}
