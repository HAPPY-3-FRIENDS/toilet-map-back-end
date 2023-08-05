package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.dto.ServiceDTO;
import com.happy3friends.toiletmapbackend.entity.ServiceEntity;
import com.happy3friends.toiletmapbackend.enums.ToiletMapErrorCodeEnum;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.ServiceMapper;
import com.happy3friends.toiletmapbackend.repository.ServiceRepository;
import com.happy3friends.toiletmapbackend.response.ServiceResponse;
import com.happy3friends.toiletmapbackend.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceServiceImpl implements ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ServiceMapper serviceMapper;

    @Override
    public List<ServiceResponse> getAllService() {
        List<ServiceEntity> serviceEntities = serviceRepository.findAll();
        if (serviceEntities.isEmpty())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_SERVICE, ToiletMapErrorCodeEnum.NOT_FOUND_SERVICE.getMessage());

        return serviceEntities.stream()
                .map(entity -> serviceMapper.convertServiceEntityToServiceResponse(entity))
                .collect(Collectors.toList());
    }

    @Override
    public void updateBatchServices(List<ServiceDTO> serviceDTOS) {
        List<ServiceEntity> oldServiceEntities
                = serviceRepository.findAllById(serviceDTOS.stream().map(ServiceDTO::getId).collect(Collectors.toList()));
        oldServiceEntities = oldServiceEntities.stream()
                .map(oldEntity -> {
                    oldEntity.setPrice(serviceDTOS.stream().filter(dto -> dto.getId() == oldEntity.getId()).findFirst().get().getPrice());
                    return oldEntity;
                })
                .collect(Collectors.toList());
        serviceRepository.saveAll(oldServiceEntities);
    }
}
