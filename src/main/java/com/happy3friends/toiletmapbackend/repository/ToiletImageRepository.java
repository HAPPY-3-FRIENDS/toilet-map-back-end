package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.entity.ToiletImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ToiletImageRepository extends JpaRepository<ToiletImageEntity, Integer> {
    @Modifying
    @Query(value = "DELETE " +
            "FROM ToiletImage " +
            "WHERE ToiletId = :toiletId", nativeQuery = true)
    void deleteByToiletId(@Param("toiletId") int toiletId);
}
