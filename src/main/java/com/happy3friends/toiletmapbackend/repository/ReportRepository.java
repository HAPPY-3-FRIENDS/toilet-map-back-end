package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.dto.CustomReportDTO;
import com.happy3friends.toiletmapbackend.entity.CheckInEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<CheckInEntity, Integer> {

    @Query(value = "SELECT s.Name                                                      AS ServiceName, " +
            "       COALESCE(SUM(c.Balance), 0) + COALESCE(SUM(c.TurnPrice), 0) AS TotalRevenue, " +
            "       COALESCE(SUM(c.Balance), 0)                                 AS WalkInGuestRevenue, " +
            "       COUNT(c.Balance)                                            AS WalkInGuestCount, " +
            "       COALESCE(SUM(c.TurnPrice), 0)                               AS UsingTurnRevenue, " +
            "       COUNT(c.TurnPrice)                                          AS UsingTurnCount " +
            "FROM CheckIn c " +
            "         RIGHT JOIN ToiletService ts " +
            "                    ON c.ToiletServiceId = ts.Id " +
            "         JOIN Service s " +
            "              ON ts.ServiceId = s.Id " +
            "         JOIN Toilet t " +
            "              ON ts.ToiletId = t.Id " +
            "WHERE t.Id = :toiletId " +
            "  AND (c.DateTime IS NULL OR c.DateTime BETWEEN :fromDate AND :toDate) " +
            "GROUP BY s.Name " +
            "ORDER BY s.Name DESC", nativeQuery = true)
    List<CustomReportDTO> getAllReportsByToiletId(@Param("toiletId") int toiletId,
                                                  @Param("fromDate") Date fromDate,
                                                  @Param("toDate") Date toDate);


    @Query(value = "SELECT * " +
            "FROM (SELECT t.Id                                                          AS ToiletId, " +
            "             t.Name                                                        AS ToiletName, " +
            "             (COALESCE(SUM(c.Balance), 0) + COALESCE(SUM(c.TurnPrice), 0)) AS TotalRevenue, " +
            "             COALESCE(SUM(c.Balance), 0)                                   AS WalkInGuestRevenue, " +
            "             COUNT(c.Balance)                                              AS WalkInGuestCount, " +
            "             COALESCE(SUM(c.TurnPrice), 0)                                 AS UsingTurnRevenue, " +
            "             COUNT(c.TurnPrice)                                            AS UsingTurnCount " +
            "      FROM CheckIn c " +
            "               RIGHT JOIN ToiletService ts " +
            "                          ON c.ToiletServiceId = ts.Id " +
            "               JOIN Service s " +
            "                    ON ts.ServiceId = s.Id " +
            "               JOIN Toilet t " +
            "                    ON ts.ToiletId = t.Id " +
            "               JOIN Company cp " +
            "                    ON t.CompanyId = cp.Id " +
            "      WHERE cp.Id = :companyId " +
            "        AND (c.DateTime IS NULL OR c.DateTime BETWEEN :fromDate AND :toDate) " +
            "      GROUP BY t.Id, t.Name) r", nativeQuery = true)
    List<CustomReportDTO> getAllReportsByCompanyId(@Param("companyId") int companyId,
                                                   @Param("fromDate") Date fromDate,
                                                   @Param("toDate") Date toDate,
                                                   Pageable pageable);

    @Query(value = "SELECT * " +
            "FROM (SELECT cp.Id                                                       AS CompanyId, " +
            "             cp.Name                                                     AS CompanyName, " +
            "             COALESCE(SUM(c.Balance), 0) + COALESCE(SUM(c.TurnPrice), 0) AS TotalRevenue, " +
            "             COALESCE(SUM(c.Balance), 0)                                 AS WalkInGuestRevenue, " +
            "             COUNT(c.Balance)                                            AS WalkInGuestCount, " +
            "             COALESCE(SUM(c.TurnPrice), 0)                               AS UsingTurnRevenue, " +
            "             COUNT(c.TurnPrice)                                          AS UsingTurnCount " +
            "      FROM CheckIn c " +
            "               RIGHT JOIN ToiletService ts " +
            "                          ON c.ToiletServiceId = ts.Id " +
            "               JOIN Service s " +
            "                    ON ts.ServiceId = s.Id " +
            "               JOIN Toilet t " +
            "                    ON ts.ToiletId = t.Id " +
            "               JOIN Company cp " +
            "                    ON t.CompanyId = cp.Id " +
            "      WHERE c.DateTime IS NULL " +
            "         OR (c.DateTime BETWEEN :fromDate AND :toDate) " +
            "      GROUP BY cp.Id, cp.Name) r", nativeQuery = true)
    List<CustomReportDTO> getAllReports(@Param("fromDate") Date fromDate,
                                        @Param("toDate") Date toDate,
                                        Pageable pageable);
}
