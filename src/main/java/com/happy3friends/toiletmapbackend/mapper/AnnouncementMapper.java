package com.happy3friends.toiletmapbackend.mapper;

import com.happy3friends.toiletmapbackend.entity.AnnouncementEntity;
import com.happy3friends.toiletmapbackend.response.AnnouncementResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AnnouncementMapper {

    @Autowired
    private ModelMapper modelMapper;

    public AnnouncementResponse convertAnnouncementEntityToAnnouncementResponse(AnnouncementEntity announcementEntity) {
        return Objects.isNull(announcementEntity)
                ? null
                : modelMapper.map(announcementEntity, AnnouncementResponse.class);
    }

}
