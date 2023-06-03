package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.constant.DefaultSortPropertyConstant;
import com.happy3friends.toiletmapbackend.entity.AnnouncementEntity;
import com.happy3friends.toiletmapbackend.mapper.AnnouncementMapper;
import com.happy3friends.toiletmapbackend.repository.AnnouncementRepository;
import com.happy3friends.toiletmapbackend.response.AnnouncementResponse;
import com.happy3friends.toiletmapbackend.service.AnnouncementService;
import com.happy3friends.toiletmapbackend.utils.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
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

    private List<AnnouncementResponse> getAllAnnouncementsByType(String announcementType, BasePaginationRequest paginationRequest) {
        Sort.Order defaultSortOrder = new Sort.Order(Sort.Direction.DESC, DefaultSortPropertyConstant.ID);
        Pageable pageable = PaginationUtil.getPageable(paginationRequest, defaultSortOrder);

        List<AnnouncementEntity> announcementEntities = announcementRepository.getAllAnnouncementsByType(announcementType ,pageable);

        return announcementEntities.stream()
                .map( o -> announcementMapper.convertAnnouncementEntityToAnnouncementResponse(o))
                .collect(Collectors.toList());
    }
}
