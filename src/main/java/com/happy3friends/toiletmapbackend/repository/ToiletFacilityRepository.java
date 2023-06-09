package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.entity.ToiletFacilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ToiletFacilityRepository extends JpaRepository<ToiletFacilityEntity, Integer> {
    @Modifying
    @Query(value = "DELETE " +
            "FROM ToiletFacility " +
            "WHERE ToiletId = :toiletId", nativeQuery = true)
    void deleteByToiletId(@Param("toiletId") int toiletId);

    boolean existsByFacilityId(int id);

    @Query(value =
            "SELECT *\n" +
            "FROM ToiletFacility\n" +
            "WHERE ToiletId = :toiletId\n" +
            "    AND (FacilityId = 1\n" +
            "    OR FacilityId = 2\n" +
            "    OR FacilityId = 3)", nativeQuery = true)
    List<ToiletFacilityEntity> getListToiletFacilityByToiletId(@Param("toiletId") int toiletId);
}
