package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.dto.CustomStatisticDTO;
import com.happy3friends.toiletmapbackend.dto.CustomStatisticForSuggestionDTO;
import com.happy3friends.toiletmapbackend.entity.CheckInEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface StatisticRepository extends JpaRepository<CheckInEntity, Integer> {

    @Query(value = "SELECT s.Name                                                      AS ServiceName, " +
            "       COALESCE(SUM(c.Balance), 0) + COALESCE(SUM(c.TurnPrice), 0) AS TotalRevenue, " +
            "       COALESCE(SUM(c.Balance), 0)                                 AS WalkInGuestRevenue, " +
            "       COUNT(c.Balance)                                            AS WalkInGuestCount, " +
            "       COALESCE(SUM(c.TurnPrice), 0)                               AS UsingTurnRevenue, " +
            "       COUNT(c.TurnPrice)                                          AS UsingTurnCount " +
            "FROM CheckIn c " +
            "         RIGHT JOIN ToiletService ts " +
            "                    ON c.ToiletServiceId = ts.Id AND (c.DateTime IS NULL OR c.DateTime BETWEEN :fromDate AND :toDate) " +
            "         JOIN Service s " +
            "              ON ts.ServiceId = s.Id " +
            "         JOIN Toilet t " +
            "              ON ts.ToiletId = t.Id " +
            "WHERE t.Id = :toiletId " +
            "GROUP BY s.Name " +
            "ORDER BY s.Name DESC", nativeQuery = true)
    List<CustomStatisticDTO> getAllStatisticsByToiletId(@Param("toiletId") int toiletId,
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
            "                          ON c.ToiletServiceId = ts.Id AND " +
            "                             (c.DateTime IS NULL OR c.DateTime BETWEEN :fromDate AND :toDate) " +
            "               JOIN Service s " +
            "                    ON ts.ServiceId = s.Id " +
            "               JOIN Toilet t " +
            "                    ON ts.ToiletId = t.Id " +
            "               JOIN Company cp " +
            "                    ON t.CompanyId = cp.Id " +
            "      WHERE cp.Id = :companyId " +
            "      GROUP BY t.Id, t.Name) r", nativeQuery = true)
    List<CustomStatisticDTO> getAllStatisticsByCompanyId(@Param("companyId") int companyId,
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
            "               RIGHT JOIN Company cp " +
            "                          ON t.CompanyId = cp.Id " +
            "      WHERE cp.Id != 1 AND c.DateTime IS NULL " +
            "         OR (c.DateTime BETWEEN :fromDate AND :toDate) " +
            "      GROUP BY cp.Id, cp.Name) r", nativeQuery = true)
    List<CustomStatisticDTO> getAllStatistics(@Param("fromDate") Date fromDate,
                                           @Param("toDate") Date toDate,
                                           Pageable pageable);

    @Query(value = "SELECT (COALESCE(SUM(c.Balance), 0) + COALESCE(SUM(c.TurnPrice), 0)) AS TotalRevenue, " +
            "       COUNT(c.Balance) + COUNT(c.TurnPrice)                         AS TotalTurn, " +
            "       COUNT(c.Balance)                                              AS walkInGuestCount, " +
            "       COUNT(c.TurnPrice)                                            AS usingTurnCount " +
            "FROM CheckIn c " +
            "         RIGHT JOIN ToiletService ts " +
            "                    ON c.ToiletServiceId = ts.Id AND " +
            "                       (c.DateTime IS NULL OR c.DateTime BETWEEN :fromDate AND :toDate) " +
            "         JOIN Toilet t " +
            "              ON ts.ToiletId = t.Id " +
            "         JOIN Company cp " +
            "              ON t.CompanyId = cp.Id " +
            "WHERE (:toiletId IS NULL OR t.Id = :toiletId) " +
            "  AND (:companyId IS NULL OR cp.Id = :companyId)", nativeQuery = true)
    CustomStatisticDTO getTotalStatisticOfMonth(@Param("companyId") Integer companyId,
                                                @Param("toiletId") Integer toiletId,
                                                @Param("fromDate") Date fromDate,
                                                @Param("toDate") Date toDate);

    @Query(value = "SELECT COUNT(*) " +
            "FROM (SELECT t.Id                                                          AS ToiletId, " +
            "             t.Name                                                        AS ToiletName, " +
            "             (COALESCE(SUM(c.Balance), 0) + COALESCE(SUM(c.TurnPrice), 0)) AS TotalRevenue, " +
            "             COALESCE(SUM(c.Balance), 0)                                   AS WalkInGuestRevenue, " +
            "             COUNT(c.Balance)                                              AS WalkInGuestCount, " +
            "             COALESCE(SUM(c.TurnPrice), 0)                                 AS UsingTurnRevenue, " +
            "             COUNT(c.TurnPrice)                                            AS UsingTurnCount, " +
            "             CAST(sg.Message AS NVARCHAR(MAX))                             AS Message, " +
            "             sg.IsAccepted " +
            "      FROM CheckIn c " +
            "               RIGHT JOIN ToiletService ts " +
            "                          ON c.ToiletServiceId = ts.Id AND " +
            "                             (c.DateTime IS NULL OR c.DateTime BETWEEN :fromDate AND :toDate) " +
            "               JOIN Service s " +
            "                    ON ts.ServiceId = s.Id " +
            "               JOIN Toilet t " +
            "                    ON ts.ToiletId = t.Id " +
            "               JOIN Company cp " +
            "                    ON t.CompanyId = cp.Id " +
            "               LEFT JOIN Suggestion sg " +
            "                         ON t.Id = sg.ToiletId " +
            "      WHERE cp.Id = :companyId " +
            "      GROUP BY t.Id, t.Name, CAST(sg.Message AS NVARCHAR(MAX)), sg.IsAccepted) r", nativeQuery = true)
    int countAllStatisticsByCompanyId(Integer companyId, Date fromDate, Date toDate);

    @Query(value =
            "SELECT COUNT(*) " +
            "FROM (SELECT s.Name AS ServiceName, " +
            "    COALESCE(SUM(c.Balance), 0) + COALESCE(SUM(c.TurnPrice), 0) AS TotalRevenue, " +
            "    COALESCE(SUM(c.Balance), 0) AS WalkInGuestRevenue, " +
            "    COUNT(c.Balance) AS WalkInGuestCount, " +
            "    COALESCE(SUM(c.TurnPrice), 0) AS UsingTurnRevenue, " +
            "    COUNT(c.TurnPrice) AS UsingTurnCount " +
            "FROM CheckIn c " +
            "    RIGHT JOIN ToiletService ts " +
            "        ON c.ToiletServiceId = ts.Id AND (c.DateTime IS NULL OR c.DateTime BETWEEN :fromDate AND :toDate) " +
            "    JOIN Service s " +
            "        ON ts.ServiceId = s.Id " +
            "    JOIN Toilet t " +
            "        ON ts.ToiletId = t.Id " +
            "WHERE t.Id = :toiletId " +
            "GROUP BY s.Name) a", nativeQuery = true)
    int countAllStatisticsByToiletId(Integer toiletId, Date fromDate, Date toDate);

    @Query(value = "SELECT COUNT(*) " +
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
            "               RIGHT JOIN Company cp " +
            "                          ON t.CompanyId = cp.Id " +
            "      WHERE cp.Id != 1 AND c.DateTime IS NULL " +
            "         OR (c.DateTime BETWEEN :fromDate AND :toDate) " +
            "      GROUP BY cp.Id, cp.Name) r", nativeQuery = true)
    int countAllStatistics(Date fromDate, Date toDate);

    @Query(value =
            "SELECT r.ToiletId, " +
                    "       r.WalkInGuestCount + r.UsingTurnCount AS ActualCount, " +
                    "       CAST(DATEDIFF(MINUTE, t.OpenTime, t.CloseTime) AS float)/60 AS Hours " +
                    "FROM (SELECT t.Id AS ToiletId, " +
                    "        COUNT(c.Balance) AS WalkInGuestCount, " +
                    "        COUNT(c.TurnPrice) AS UsingTurnCount " +
                    "    FROM CheckIn c " +
                    "    RIGHT JOIN ToiletService ts " +
                    "        ON c.ToiletServiceId = ts.Id AND " +
                    "            (c.DateTime IS NULL OR c.DateTime BETWEEN :fromDate AND :toDate) " +
                    "    JOIN Service s " +
                    "        ON ts.ServiceId = s.Id " +
                    "    JOIN Toilet t " +
                    "        ON ts.ToiletId = t.Id " +
                    "    WHERE t.Id = :toiletId " +
                    "    GROUP BY t.Id, t.Name) r " +
                    "LEFT JOIN Toilet t ON r.ToiletId = t.Id", nativeQuery = true)
    List<CustomStatisticForSuggestionDTO> getStatisticsByToiletId(@Param("toiletId") Integer toiletId,
                                                                  @Param("fromDate") Date fromDate,
                                                                  @Param("toDate") Date toDate);
}
