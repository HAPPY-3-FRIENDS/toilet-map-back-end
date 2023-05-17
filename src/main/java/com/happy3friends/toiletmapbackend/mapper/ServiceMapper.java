package com.happy3friends.toiletmapbackend.mapper;

import com.happy3friends.toiletmapbackend.entity.ServiceEntity;
import com.happy3friends.toiletmapbackend.response.ServiceResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ServiceMapper {

    @Autowired
    private ModelMapper modelMapper;

    public ServiceResponse convertServiceEntityToServiceResponse(ServiceEntity serviceEntity) {
        return Objects.isNull(serviceEntity)
                ? null
                : modelMapper.map(serviceEntity, ServiceResponse.class);
    }
}
