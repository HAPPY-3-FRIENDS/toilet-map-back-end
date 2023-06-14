package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.entity.SuggestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SuggestionRepository extends JpaRepository<SuggestionEntity, Integer> {

    @Query(value = "SELECT sg.Id, " +
            "       sg.ToiletId, " +
            "       CAST(sg.Message AS NVARCHAR(MAX)) AS Message, " +
            "       sg.IsAccepted, " +
            "       sg.StartDate, " +
            "       sg.EndDate, " +
            "       sg.ExpectedCount, " +
            "       sg.ActualCount " +
            "FROM Toilet t " +
            "         JOIN Company c " +
            "              ON t.CompanyId = c.Id " +
            "         JOIN Suggestion sg " +
            "              ON t.Id = sg.ToiletId " +
            "                  AND ((sg.StartDate = DATEADD(QUARTER, DATEDIFF(QUARTER, 0, GETDATE()) - 2, 0) AND " +
            "                        sg.EndDate = DATEADD(QUARTER, DATEDIFF(QUARTER, 0, GETDATE()) - 1, 0)) " +
            "                      OR " +
            "                       (sg.StartDate = DATEADD(QUARTER, DATEDIFF(QUARTER, 0, GETDATE()) - 1, 0) AND " +
            "                        sg.EndDate = DATEADD(QUARTER, DATEDIFF(QUARTER, 0, GETDATE()), 0)) " +
            "                     ) " +
            "WHERE c.Id = :companyId " +
            "  AND sg.ToiletId IN (:listToiletIds)", nativeQuery = true)
    List<SuggestionEntity> getListSuggestionByListToiletIds(@Param("companyId") int companyId, @Param("listToiletIds") List<Integer> listToiletIds);

    @Query(value =
            "SELECT Id,\n" +
                    "       t1.ToiletId,\n" +
                    "       Message,\n" +
                    "       IsAccepted,\n" +
                    "       StartDate,\n" +
                    "       EndDate,\n" +
                    "       ActualCount,\n" +
                    "       ExpectedCount\n" +
                    "FROM\n" +
                    "    (SELECT ToiletId, COUNT(*) AS Times\n" +
                    "    FROM Suggestion\n" +
                    "    WHERE StartDate = :startDate OR EndDate = :endDate\n" +
                    "        AND IsAccepted = 'false'\n" +
                    "    GROUP BY (ToiletId)) t1\n" +
                    "LEFT JOIN\n" +
                    "    (SELECT *\n" +
                    "    FROM Suggestion\n" +
                    "    WHERE StartDate = :startDate OR EndDate = :endDate\n" +
                    "        AND IsAccepted = 'false') t2\n" +
                    "ON t1.ToiletId = t2.ToiletId\n" +
                    "WHERE t1.Times = 2", nativeQuery = true)
    List<SuggestionEntity> getAllSuggestionsIn2LastQuarter(Date startDate,
                                                           Date endDate);
}
