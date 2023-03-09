package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.entity.CheckInEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckInRepository extends JpaRepository<CheckInEntity, Integer> {

    @Query(value = "SELECT * " +
            "FROM CheckIn c INNER JOIN ToiletService ts " +
            "ON c.ToiletServiceId = ts.Id " +
            "WHERE ts.ToiletId = :toiletId", nativeQuery = true)
    List<CheckInEntity> toiletCheckInHistoriesByToiletId(@Param("toiletId") int toiletId);
}
