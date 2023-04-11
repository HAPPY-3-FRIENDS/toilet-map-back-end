package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.dto.ServiceDTO;
import com.happy3friends.toiletmapbackend.entity.ToiletEntity;
import com.happy3friends.toiletmapbackend.entity.ToiletServiceEntity;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.repository.ToiletRepository;
import com.happy3friends.toiletmapbackend.repository.ToiletServiceRepository;
import com.happy3friends.toiletmapbackend.response.ToiletServiceResponse;
import com.happy3friends.toiletmapbackend.service.ToiletServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ToiletServiceServiceImpl implements ToiletServiceService {

    @Autowired
    private ToiletServiceRepository toiletServiceRepository;

    @Autowired
    private ToiletRepository toiletRepository;

    @Override
    public List<ToiletServiceResponse> getToiletServicesByToiletId(int toiletId) {
        Optional<ToiletEntity> toiletEntity = toiletRepository.findById(toiletId);
        if (!toiletEntity.isPresent())
            throw new NotFoundException("Toilet", "Id", toiletId);

        List<ToiletServiceEntity> toiletServiceEntityList
                = toiletServiceRepository.findToiletServiceEntitiesByToiletIdAndFetchServiceEagerly(toiletId);

        return toiletServiceEntityList.stream()
                .map(entity -> {
                    ToiletServiceResponse toiletServiceResponse = new ToiletServiceResponse();
                    toiletServiceResponse.setId(entity.getId());
                    toiletServiceResponse.setToiletId(entity.getToiletId());
                    toiletServiceResponse.setService(new ServiceDTO(
                            entity.getServiceByServiceId().getId(),
                            entity.getServiceByServiceId().getName(),
                            entity.getServiceByServiceId().getPrice(),
                            entity.getServiceByServiceId().getTurn()
                    ));

                    return toiletServiceResponse;
                })
                .collect(Collectors.toList());
    }
}
