package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.request.CreateAnnouncementRequest;
import com.happy3friends.toiletmapbackend.response.AnnouncementResponse;

import java.util.List;
import java.util.Map;

public interface AnnouncementService {

    List<AnnouncementResponse> getAllAnnouncements(String announcementType, BasePaginationRequest paginationRequest);

    int count(String announcementType);

    AnnouncementResponse getAnnouncementById(int id);

    AnnouncementResponse updateAnnouncement(Integer id, Map<String, Object> fields);

    AnnouncementResponse createAnnouncement(CreateAnnouncementRequest request);

    boolean delete(Integer id);
}
