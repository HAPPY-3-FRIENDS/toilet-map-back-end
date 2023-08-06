package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.constant.AnnouncementTypeConstant;
import com.happy3friends.toiletmapbackend.constant.DateTimeConstant;
import com.happy3friends.toiletmapbackend.constant.DefaultSortPropertyConstant;
import com.happy3friends.toiletmapbackend.entity.AnnouncementEntity;
import com.happy3friends.toiletmapbackend.enums.ToiletMapErrorCodeEnum;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.AnnouncementMapper;
import com.happy3friends.toiletmapbackend.repository.AnnouncementRepository;
import com.happy3friends.toiletmapbackend.request.CreateAnnouncementRequest;
import com.happy3friends.toiletmapbackend.response.AnnouncementResponse;
import com.happy3friends.toiletmapbackend.service.AnnouncementService;
import com.happy3friends.toiletmapbackend.utils.DateTimeUtil;
import com.happy3friends.toiletmapbackend.utils.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Override
    public List<AnnouncementResponse> getAllAnnouncements(String announcementType, BasePaginationRequest paginationRequest) {
        if (announcementType != null) {
            return getAllAnnouncementsByType(announcementType, paginationRequest);
        } else {
            Sort.Order defaultSortOrder = new Sort.Order(Sort.Direction.DESC, DefaultSortPropertyConstant.ID);
            Pageable pageable = PaginationUtil.getPageable(paginationRequest, defaultSortOrder);

            Page<AnnouncementEntity> announcementEntities = announcementRepository.findAll(pageable);

            return announcementEntities.stream()
                    .map( o -> announcementMapper.convertAnnouncementEntityToAnnouncementResponse(o))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public int count(String announcementType) {
        if (announcementType != null) {
            return announcementRepository.countAnnouncementsByType(announcementType);
        } else {
            return (int) announcementRepository.count();
        }
    }

    @Override
    public AnnouncementResponse getAnnouncementById(int id) {

        Optional<AnnouncementEntity> entity = announcementRepository.findById(id);
        if (entity.isEmpty())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_ANNOUNCEMENT, ToiletMapErrorCodeEnum.NOT_FOUND_ANNOUNCEMENT.getMessage());

        return announcementMapper.convertAnnouncementEntityToAnnouncementResponse(entity.get());
    }

    @Override
    public AnnouncementResponse updateAnnouncement(Integer id, Map<String, Object> fields) {

        Optional<AnnouncementEntity> entity = announcementRepository.findById(id);
        if (entity.isEmpty())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_ANNOUNCEMENT, ToiletMapErrorCodeEnum.NOT_FOUND_ANNOUNCEMENT.getMessage());

        fields.forEach((key, value) -> {
            if (key.equals("startDate")) {
                String value2String = value.toString();
                if (value2String != null) {
                    entity.get().setStartDate(DateTimeUtil.convertStringToDate(value2String, DateTimeConstant.dd__MM__yyyy));
                }
            } else if (key.equals("endDate")) {
                String value2String = value.toString();
                if (value2String != null) {
                    entity.get().setEndDate(DateTimeUtil.convertStringToDate(value2String, DateTimeConstant.dd__MM__yyyy));
                }
            } else {
                Field field = ReflectionUtils.findField(AnnouncementEntity.class, key);
                field.setAccessible(true);
                ReflectionUtils.setField(field, entity.get(), value);
            }
        });

        AnnouncementEntity result = announcementRepository.save(entity.get());

        return announcementMapper.convertAnnouncementEntityToAnnouncementResponse(result);
    }

    @Override
    public AnnouncementResponse createAnnouncement(CreateAnnouncementRequest request) {

        AnnouncementEntity entity = new AnnouncementEntity();

        entity.setTitle(request.getTitle());
        entity.setUrl(request.getUrl());
        entity.setImageSource(request.getImageSource());

        if (request.getType().equals(AnnouncementTypeConstant.INTERNAL)) {
            entity.setStartDate(request.getStartDate());
            entity.setEndDate(request.getEndDate());
        }
        entity.setDescription(request.getDescription());
        entity.setType(request.getType());

        return announcementMapper.convertAnnouncementEntityToAnnouncementResponse(announcementRepository.save(entity));
    }

    @Override
    public boolean delete(Integer id) {
        boolean result = true;

        Optional<AnnouncementEntity> entity = announcementRepository.findById(id);
        if (entity.isEmpty())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_ANNOUNCEMENT, ToiletMapErrorCodeEnum.NOT_FOUND_ANNOUNCEMENT.getMessage());

        try {
            announcementRepository.deleteById(id);
        } catch (Exception e) {
            result = false;
        }

        return result;
    }

    private List<AnnouncementResponse> getAllAnnouncementsByType(String announcementType, BasePaginationRequest paginationRequest) {
        Sort.Order defaultSortOrder = new Sort.Order(Sort.Direction.DESC, DefaultSortPropertyConstant.ID);
        Pageable pageable = PaginationUtil.getPageable(paginationRequest, defaultSortOrder);

        List<AnnouncementEntity> announcementEntities = announcementRepository.getAllAnnouncementsByType(announcementType ,pageable);

        return announcementEntities.stream()
                .map( o -> announcementMapper.convertAnnouncementEntityToAnnouncementResponse(o))
                .collect(Collectors.toList());
    }
}
