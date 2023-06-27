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
            "       sg.ActualCount, " +
            "       sg.Streak " +
            "FROM Toilet t " +
            "         JOIN Company c " +
            "              ON t.CompanyId = c.Id " +
            "         JOIN Suggestion sg " +
            "              ON t.Id = sg.ToiletId " +
            "                  AND ((sg.StartDate = DATEADD(QUARTER, DATEDIFF(QUARTER, 0, GETDATE()) - 2, 0) AND " +
            "                        sg.EndDate = DATEADD(QUARTER, DATEDIFF(QUARTER, 0, GETDATE()) - 1, -1)) " +
            "                      OR " +
            "                       (sg.StartDate = DATEADD(QUARTER, DATEDIFF(QUARTER, 0, GETDATE()) - 1, 0) AND " +
            "                        sg.EndDate = DATEADD(QUARTER, DATEDIFF(QUARTER, 0, GETDATE()), -1)) " +
            "                     ) " +
            "WHERE c.Id = :companyId " +
            "  AND sg.ToiletId IN (:listToiletIds) " +
            "ORDER BY t.Id ASC, sg.Streak DESC", nativeQuery = true)
    List<SuggestionEntity> getListSuggestionByListToiletIds(@Param("companyId") int companyId, @Param("listToiletIds") List<Integer> listToiletIds);

    @Query(value =
            "SELECT Id, " +
                    "       t1.ToiletId, " +
                    "       Message, " +
                    "       IsAccepted, " +
                    "       StartDate, " +
                    "       EndDate, " +
                    "       ActualCount, " +
                    "       ExpectedCount, " +
                    "       Streak, " +
                    "       IsLow " +
                    "FROM " +
                    "    (SELECT ToiletId, COUNT(*) AS Times " +
                    "    FROM Suggestion " +
                    "    WHERE StartDate = :startDate OR EndDate = :endDate " +
                    "    GROUP BY (ToiletId)) t1 " +
                    "LEFT JOIN " +
                    "    (SELECT * " +
                    "    FROM Suggestion " +
                    "    WHERE StartDate = :startDate OR EndDate = :endDate " +
                    "    ) t2 " +
                    "ON t1.ToiletId = t2.ToiletId " +
                    "WHERE t1.Times = 2", nativeQuery = true)
    List<SuggestionEntity> getAllSuggestionsIn2LastQuarter(Date startDate,
                                                           Date endDate);

    @Query(value =
            "SELECT * " +
            "FROM Suggestion " +
            "WHERE ToiletId = :toiletId " +
            "      AND EndDate = :endDate " +
            "      AND IsLow = 'false'", nativeQuery = true)
    SuggestionEntity getPreviousQuarterSuggestion(int toiletId, Date endDate);
}
