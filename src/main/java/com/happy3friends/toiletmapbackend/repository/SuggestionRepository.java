package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.entity.SuggestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
