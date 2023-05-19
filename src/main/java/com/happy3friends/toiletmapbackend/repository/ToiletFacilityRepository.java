package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.entity.ToiletFacilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ToiletFacilityRepository extends JpaRepository<ToiletFacilityEntity, Integer> {
    @Modifying
    @Query(value = "DELETE " +
            "FROM ToiletFacility " +
            "WHERE ToiletId = :toiletId", nativeQuery = true)
    void deleteByToiletId(@Param("toiletId") int toiletId);
}
