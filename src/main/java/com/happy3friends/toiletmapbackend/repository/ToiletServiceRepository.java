package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.entity.ToiletServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToiletServiceRepository extends JpaRepository<ToiletServiceEntity, Integer> {

    @Query(value = "SELECT ts " +
            "FROM ToiletServiceEntity ts " +
            "JOIN FETCH ts.serviceByServiceId " +
            "WHERE ts.toiletId = :toiletId")
    List<ToiletServiceEntity> findToiletServiceEntitiesByToiletIdAndFetchServiceEagerly(@Param("toiletId") int toiletId);
}
