package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.entity.AnnouncementEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<AnnouncementEntity, Integer> {

    @Query(value =
            "SELECT * " +
            "FROM Announcement " +
            "WHERE Type = :announcementType", nativeQuery = true)
    List<AnnouncementEntity> getAllAnnouncementsByType(String announcementType ,Pageable pageable);

    @Query(value =
            "SELECT COUNT(*) " +
            "FROM Announcement " +
            "WHERE Type = :announcementType", nativeQuery = true)
    int countAnnouncementsByType(String announcementType);
}
