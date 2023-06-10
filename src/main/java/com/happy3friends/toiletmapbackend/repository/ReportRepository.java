package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.dto.CustomReportDTO;
import com.happy3friends.toiletmapbackend.entity.ReportEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Integer> {

    @Query(value = "SELECT r.ToiletId, t.Name AS ToiletName, r.Message, COUNT(r.Message) AS count " +
            "FROM Report r " +
            "         INNER JOIN Toilet t " +
            "                    ON r.ToiletId = t.Id " +
            "GROUP BY r.Message, r.ToiletId, t.Name, r.Status " +
            "ORDER BY count DESC", nativeQuery = true)
    List<CustomReportDTO> getReports(Pageable pageable);
}
