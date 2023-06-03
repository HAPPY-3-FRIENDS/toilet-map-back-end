package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.response.AnnouncementResponse;

import java.util.List;

public interface AnnouncementService {

    List<AnnouncementResponse> getAllAnnouncements(String announcementType, BasePaginationRequest paginationRequest);

    int count(String announcementType);
}
