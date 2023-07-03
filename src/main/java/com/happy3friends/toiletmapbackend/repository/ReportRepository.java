package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.dto.CustomReportDTO;
import com.happy3friends.toiletmapbackend.dto.CustomReportForManagerDTO;
import com.happy3friends.toiletmapbackend.entity.ReportEntity;
import com.happy3friends.toiletmapbackend.request.UpdateListReportRequest;
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

    @Query(value = "SELECT r.Id, t.Name, r.Message, r.Status\n" +
            "FROM Report r\n" +
            "LEFT JOIN Toilet t on t.Id = r.ToiletId\n" +
            "WHERE t.CompanyId = :companyId", nativeQuery = true)
    List<CustomReportForManagerDTO> getReportsForManager(int companyId, Pageable pageable);

    @Query(value = "SELECT COUNT(*)\n" +
            "FROM Report r\n" +
            "LEFT JOIN Toilet t on t.Id = r.ToiletId\n" +
            "WHERE t.CompanyId = :id", nativeQuery = true)
    int countReportsForManager(int id);
}
