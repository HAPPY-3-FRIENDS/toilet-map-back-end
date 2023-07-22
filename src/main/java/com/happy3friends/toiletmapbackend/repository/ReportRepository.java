package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.dto.CustomReportDTO;
import com.happy3friends.toiletmapbackend.dto.CustomReportForManagerDTO;
import com.happy3friends.toiletmapbackend.entity.ReportEntity;
import com.happy3friends.toiletmapbackend.request.UpdateListReportRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Integer> {

    @Query(value = "SELECT t.Name AS ToiletName, COUNT(r.Message) AS TotalReport, r.Message, r.Status, COUNT(r.Status) AS TotalStatus " +
            "FROM Report r " +
            "         JOIN Toilet t ON r.ToiletId = t.Id " +
            "WHERE t.CompanyId = :companyId " +
            "GROUP BY r.Message, t.Name, r.Status, r.ToiletId " +
            "ORDER BY t.Name", nativeQuery = true)
    List<CustomReportDTO> getReportsByCompanyId(@Param("companyId") int companyId,
                                                Pageable pageable);

    @Query(value = "SELECT r.Id, t.Name, r.Message, r.Status, r.CreateDate " +
            "FROM Report r " +
            "LEFT JOIN Toilet t on t.Id = r.ToiletId " +
            "WHERE r.ToiletId = :toiletId", nativeQuery = true)
    List<CustomReportForManagerDTO> getReportsByToiletIdForManager(int toiletId, Pageable pageable);

    @Query(value = "SELECT COUNT(*) " +
            "FROM Report r " +
            "LEFT JOIN Toilet t on t.Id = r.ToiletId " +
            "WHERE r.ToiletId = :toiletId", nativeQuery = true)
    int countReportsByToiletIdForManager(int toiletId);

    @Query(value = "SELECT r.Id, t.Name, r.Message, r.Status, r.CreateDate " +
            "FROM Report r " +
            "LEFT JOIN Toilet t on t.Id = r.ToiletId " +
            "WHERE t.CompanyId = :companyId", nativeQuery = true)
    List<CustomReportForManagerDTO> getReportsByCompanyIdForManager(int companyId, Pageable pageable);

    @Query(value = "SELECT COUNT(*) " +
            "FROM Report r " +
            "LEFT JOIN Toilet t on t.Id = r.ToiletId " +
            "WHERE t.CompanyId = :companyId", nativeQuery = true)
    int countReportsByCompanyIdForManager(int companyId);
}
