package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.dto.CustomRatingDetailsDTO;
import com.happy3friends.toiletmapbackend.entity.RatingEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<RatingEntity, Integer> {

    @Query(value = "SELECT * " +
            "FROM (SELECT r.Id, " +
            "             ui.FullName, " +
            "             r.Star, " +
            "             CAST(r.Comment AS NVARCHAR(MAX))     AS Comment, " +
            "             r.DateTime, " +
            "             CAST(ri.ImageSource AS VARCHAR(MAX)) AS ImageSource " +
            "      FROM Rating r " +
            "               JOIN Account a " +
            "                    ON r.AccountId = a.Id " +
            "               JOIN UserInfo ui ON a.Id = ui.AccountId " +
            "               LEFT JOIN RatingImage ri ON ri.RatingId = r.Id) r", nativeQuery = true)
    List<CustomRatingDetailsDTO> getAllRatings(Pageable pageable);

    @Query(value = "SELECT * " +
            "FROM (SELECT r.Id, " +
            "             ui.FullName, " +
            "             r.Star, " +
            "             CAST(r.Comment AS NVARCHAR(MAX))     AS Comment, " +
            "             r.DateTime, " +
            "             CAST(ri.ImageSource AS VARCHAR(MAX)) AS ImageSource " +
            "      FROM Rating r " +
            "               JOIN Account a " +
            "                    ON r.AccountId = a.Id " +
            "               JOIN UserInfo ui ON a.Id = ui.AccountId " +
            "               LEFT JOIN RatingImage ri ON ri.RatingId = r.Id " +
            "               JOIN Toilet t ON r.ToiletId = t.Id " +
            "      WHERE t.Id = :toiletId) r", nativeQuery = true)
    List<CustomRatingDetailsDTO> getAllRatingsByToiletId(@Param("toiletId") int toiletId,
                                                         Pageable pageable);

    long countByToiletId(int toiletId);

    Boolean existsByCheckInId(Integer checkInId);
}
