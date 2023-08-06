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
            "             CAST(ri.ImageSource AS VARCHAR(MAX)) AS ImageSource, " +
            "             ui.Avatar, " +
            "             r.Status, " +
            "             cc.Name AS CommonComment " +
            "      FROM Rating r " +
            "               JOIN Account a " +
            "                    ON r.AccountId = a.Id " +
            "               JOIN UserInfo ui ON a.Id = ui.AccountId " +
            "               LEFT JOIN RatingCommonComment rcc ON rcc.RatingId = r.Id " +
            "               LEFT JOIN CommonComment cc ON rcc.CommonCommentId = cc.Id " +
            "               LEFT JOIN RatingImage ri ON ri.RatingId = r.Id) r", nativeQuery = true)
    List<CustomRatingDetailsDTO> getAllRatings(Pageable pageable);

    @Query(value = "SELECT * " +
            "FROM (SELECT r.Id, " +
            "       ui.FullName, " +
            "       r.Star, " +
            "       CAST(r.Comment AS NVARCHAR(MAX))          AS Comment, " +
            "       r.DateTime, " +
            "       ui.Avatar, " +
            "       r.Status " +
            "FROM Rating r " +
            "         JOIN Account a " +
            "              ON r.AccountId = a.Id " +
            "         JOIN UserInfo ui ON a.Id = ui.AccountId " +
            "         JOIN Toilet t ON r.ToiletId = t.Id " +
            "WHERE t.Id = :toiletId) r", nativeQuery = true)
    List<CustomRatingDetailsDTO> getAllRatingsByToiletId(@Param("toiletId") int toiletId,
                                                         Pageable pageable);

    @Query(value = "SELECT COUNT(*) " +
            "FROM Rating " +
            "WHERE ToiletId = :toiletId", nativeQuery = true)
    long countByToiletId(int toiletId);

    @Query(value = "SELECT r.Id, " +
            "       CAST(ri.ImageSource AS VARCHAR(MAX)) AS ImageSource, " +
            "       cc.Name                              AS CommonComment " +
            "FROM Rating r " +
            "         LEFT JOIN RatingImage ri ON ri.RatingId = r.Id " +
            "         LEFT JOIN RatingCommonComment rcc ON rcc.RatingId = r.Id " +
            "         LEFT JOIN CommonComment cc ON rcc.CommonCommentId = cc.Id " +
            "WHERE r.Id IN :lstRatingIds", nativeQuery = true)
    List<CustomRatingDetailsDTO> getAllRatingImageAndRatingCommonCommentByListRatingIds(@Param("lstRatingIds") List<Integer> lstRatingIds);

    Boolean existsByCheckInId(Integer checkInId);

    @Query(value = "SELECT * " +
            "FROM (SELECT r.Id, " +
            "             ui.FullName, " +
            "             r.Star, " +
            "             CAST(r.Comment AS NVARCHAR(MAX))                     AS Comment, " +
            "             r.DateTime, " +
            "             CAST(ri.ImageSource AS VARCHAR(MAX))                 AS ImageSource, " +
            "             ui.Avatar, " +
            "             r.Status, " +
            "             cc.Name                                              AS CommonComment, " +
            "             DENSE_RANK() OVER (ORDER BY Star ASC, DateTime DESC) AS Rank " +
            "      FROM Rating r " +
            "               JOIN Account a " +
            "                    ON r.AccountId = a.Id " +
            "               JOIN UserInfo ui ON a.Id = ui.AccountId " +
            "               LEFT JOIN RatingImage ri ON ri.RatingId = r.Id " +
            "               JOIN Toilet t ON r.ToiletId = t.Id " +
            "               LEFT JOIN RatingCommonComment rcc ON rcc.RatingId = r.Id " +
            "               LEFT JOIN CommonComment cc ON rcc.CommonCommentId = cc.Id " +
            "      WHERE t.Id = :toiletId " +
            "    AND r.Star = :star) r " +
            "WHERE Rank BETWEEN (:pageSize * :pageIndex - :pageSize + 1) AND (:pageSize * :pageIndex)", nativeQuery = true)
    List<CustomRatingDetailsDTO> filterRatingByStar(@Param("toiletId") int toiletId,
                                                    @Param("star") int star,
                                                    @Param("pageSize") int pageSize,
                                                    @Param("pageIndex") int pageIndex);

    @Query(value = "SELECT COUNT(*) " +
            "FROM Rating " +
            "WHERE ToiletId = :toiletId " +
            "AND Star = :star", nativeQuery = true)
    int countRatingByStar(@Param("toiletId") int toiletId,
                          @Param("star") int star);

    @Query(value =
            "SELECT * " +
                    "FROM (SELECT r.Id, " +
                    "        ui.FullName, " +
                    "        r.Star, " +
                    "        CAST(r.Comment AS NVARCHAR(MAX)) AS Comment, " +
                    "        r.DateTime, " +
                    "        CAST(ri.ImageSource AS VARCHAR(MAX)) AS ImageSource, " +
                    "        ui.Avatar, " +
                    "        r.Status, " +
                    "        CASE " +
                    "            WHEN cc.Name IS NULL THEN 'No common comment' " +
                    "            ELSE cc.Name " +
                    "        END AS CommonComment, " +
                    "        DENSE_RANK() OVER (ORDER BY Star ASC, DateTime DESC) AS Rank " +
                    "    FROM Rating r " +
                    "    JOIN Account a ON r.AccountId = a.Id " +
                    "    JOIN UserInfo ui ON a.Id = ui.AccountId " +
                    "    LEFT JOIN RatingImage ri ON ri.RatingId = r.Id " +
                    "    JOIN Toilet t ON r.ToiletId = t.Id " +
                    "    LEFT JOIN RatingCommonComment rcc ON rcc.RatingId = r.Id " +
                    "    LEFT JOIN CommonComment cc ON rcc.CommonCommentId = cc.Id " +
                    "    WHERE t.Id = :toiletId " +
                    "    ) r " +
                    "WHERE Rank BETWEEN (:pageSize * :pageIndex - :pageSize + 1) AND (:pageSize * :pageIndex) " +
                    "    AND r.CommonComment IN (:listCommonComment) " +
                    "    AND r.Status IN (:listStatus) " +
                    "    AND r.Star IN (:listStars)", nativeQuery = true)
    List<CustomRatingDetailsDTO> filterRating(@Param("toiletId") int toiletId,
                                              @Param("listCommonComment") List<String> listCommonComment,
                                              @Param("listStars") List<Integer> listStars,
                                              @Param("listStatus") List<String> listStatus,
                                              @Param("pageSize") int pageSize,
                                              @Param("pageIndex") int pageIndex);

    @Query(value =
            "SELECT * " +
                    "FROM (SELECT r.Id, " +
                    "        ui.FullName, " +
                    "        r.Star, " +
                    "        CAST(r.Comment AS NVARCHAR(MAX)) AS Comment, " +
                    "        r.DateTime, " +
                    "        CAST(ri.ImageSource AS VARCHAR(MAX)) AS ImageSource, " +
                    "        ui.Avatar, " +
                    "        r.Status, " +
                    "        CASE " +
                    "            WHEN cc.Name IS NULL THEN 'No common comment' " +
                    "            ELSE cc.Name " +
                    "        END AS CommonComment " +
                    "    FROM Rating r " +
                    "    JOIN Account a ON r.AccountId = a.Id " +
                    "    JOIN UserInfo ui ON a.Id = ui.AccountId " +
                    "    LEFT JOIN RatingImage ri ON ri.RatingId = r.Id " +
                    "    JOIN Toilet t ON r.ToiletId = t.Id " +
                    "    LEFT JOIN RatingCommonComment rcc ON rcc.RatingId = r.Id " +
                    "    LEFT JOIN CommonComment cc ON rcc.CommonCommentId = cc.Id " +
                    "    WHERE t.Id = :toiletId " +
                    "    ) r " +
                    "WHERE r.CommonComment IN (:listCommonComment) " +
                    "    AND r.Status IN (:listStatus) " +
                    "    AND r.Star IN (:listStars)", nativeQuery = true)
    List<CustomRatingDetailsDTO> filterRatingToCount(@Param("toiletId") int toiletId,
                                                     @Param("listCommonComment") List<String> listCommonComment,
                                                     @Param("listStars") List<Integer> listStars,
                                                     @Param("listStatus") List<String> listStatus);

    @Query(value = "SELECT r.Id, " +
            "       r.Star, " +
            "       CAST(r.Comment AS NVARCHAR(MAX))     AS Comment, " +
            "       r.DateTime, " +
            "       CAST(ri.ImageSource AS VARCHAR(MAX)) AS ImageSource, " +
            "       cc.Name                              AS CommonComment " +
            "FROM Rating r " +
            "         LEFT JOIN RatingImage ri ON ri.RatingId = r.Id " +
            "         LEFT JOIN RatingCommonComment rcc ON rcc.RatingId = r.Id " +
            "         LEFT JOIN CommonComment cc ON rcc.CommonCommentId = cc.Id " +
            "WHERE r.Id = :ratingId", nativeQuery = true)
    List<CustomRatingDetailsDTO> getRatingById(@Param("ratingId") int ratingId);
}
