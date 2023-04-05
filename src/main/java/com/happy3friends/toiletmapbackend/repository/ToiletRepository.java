package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.dto.CustomToiletDTO;
import com.happy3friends.toiletmapbackend.dto.CustomToiletDetailsInfoDTO;
import com.happy3friends.toiletmapbackend.entity.ToiletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToiletRepository extends JpaRepository<ToiletEntity, Integer> {

    @Query(value = "SELECT * " +
            "FROM (SELECT *, " +
            "             DENSE_RANK() OVER (ORDER BY A.Distance ASC) AS Rank " +
            "      FROM (SELECT t.Id, " +
            "                   t.Name                                                                                AS ToiletName, " +
            "                   t.Address, " +
            "                   t.Ward, " +
            "                   t.District, " +
            "                   t.Province, " +
            "                   t.Latitude, " +
            "                   t.Longitude, " +
            "                   t.NearBy, " +
            "                   t.OpenTime, " +
            "                   t.CloseTime, " +
            "                   t.isFree, " +
            "                   IIF(t.isFree = 0, MIN(s.Price), 0)                                                    AS MinPrice, " +
            "                   IIF(t.isFree = 0, MAX(s.Price), 0)                                                    AS MaxPrice, " +
            "                   f.Name                                                                                AS FacilityName, " +
            "                   COALESCE(tf.Quantity, 0)                                                              AS FacilityQuantity, " +
            "                   CAST(tf.Description AS NVARCHAR(MAX))                                                 AS FacilityDescription, " +
            "                   COALESCE(ROUND(AVG(CAST(Star AS FLOAT)), 1), 0)                                       AS RatingStar, " +
            "                   CAST(ti.ImageSource AS NVARCHAR(MAX))                                                 AS ToiletImage, " +
            "                   SQRT(SQUARE(:currentLatitude - t.Latitude) + SQUARE(:currentLongitude - t.Longitude)) AS Distance, " +
            "                   ROW_NUMBER() OVER (PARTITION BY t.Id, t.Name, t.Address, t.Ward, t.District, t.Province, t.Latitude, " +
            "                       t.Longitude, t.NearBy, t.OpenTime, t.CloseTime, t.isFree, f.Name, tf.Quantity " +
            "                       ORDER BY t.Id)                                                                    AS RowNumber " +
            "            FROM Toilet t " +
            "                     CROSS JOIN Facility f " +
            "                     JOIN ToiletService ts " +
            "                          ON t.Id = ts.ToiletId " +
            "                     JOIN Service s " +
            "                          ON ts.ServiceId = s.Id " +
            "                     LEFT JOIN ToiletFacility tf " +
            "                               ON f.Id = tf.FacilityId AND t.Id = tf.ToiletId " +
            "                     LEFT JOIN Rating r " +
            "                               ON r.ToiletId = t.Id " +
            "                     LEFT JOIN ToiletImage ti " +
            "                               ON t.Id = ti.ToiletId " +
            "            WHERE SQRT(SQUARE(:currentLatitude - t.Latitude) + " +
            "                       SQUARE(:currentLongitude - t.Longitude)) BETWEEN (0) AND (:distanceCurrentAndDeviationMax) " +
            "            GROUP BY t.Id, t.Name, t.Address, t.Ward, t.District, t.Province, t.Latitude, t.Longitude, t.NearBy, " +
            "                     t.OpenTime, t.CloseTime, t.isFree, f.Name, " +
            "                     tf.Quantity, CAST(tf.Description AS NVARCHAR(MAX)), CAST(ti.ImageSource AS NVARCHAR(MAX))) AS A " +
            "      WHERE A.RowNumber = 1) AS RS " +
            "WHERE RS.Rank <= 10", nativeQuery = true)
    List<CustomToiletDetailsInfoDTO> getTop10ToiletsNearByCurrentLocation(@Param("currentLatitude") Double currentLatitude,
                                                                          @Param("currentLongitude") Double currentLongitude,
                                                                          @Param("distanceCurrentAndDeviationMax") Double distanceCurrentAndDeviationMax);

    @Query(value = "SELECT t.Id, " +
            "       t.Name                                          AS ToiletName, " +
            "       t.Address, " +
            "       t.Ward, " +
            "       t.District, " +
            "       t.Province, " +
            "       t.Latitude, " +
            "       t.Longitude, " +
            "       t.NearBy, " +
            "       t.OpenTime, " +
            "       t.CloseTime, " +
            "       t.isFree, " +
            "       IIF(t.isFree = 0, MIN(s.Price), 0)              AS MinPrice, " +
            "       IIF(t.isFree = 0, MAX(s.Price), 0)              AS MaxPrice, " +
            "       f.Name                                          AS FacilityName, " +
            "       COALESCE(tf.Quantity, 0)                        AS FacilityQuantity, " +
            "       CAST(tf.Description AS NVARCHAR(MAX))           AS FacilityDescription, " +
            "       COALESCE(ROUND(AVG(CAST(Star AS FLOAT)), 1), 0) AS RatingStar, " +
            "       CAST(ti.ImageSource AS NVARCHAR(MAX))           AS ToiletImage " +
            "FROM Toilet t " +
            "         CROSS JOIN Facility f " +
            "         JOIN ToiletService ts " +
            "              ON t.Id = ts.ToiletId " +
            "         JOIN Service s " +
            "              ON ts.ServiceId = s.Id " +
            "         LEFT JOIN ToiletImage ti " +
            "                   ON t.Id = ti.ToiletId " +
            "         LEFT JOIN ToiletFacility tf " +
            "                   ON f.Id = tf.FacilityId AND t.Id = tf.ToiletId " +
            "         LEFT JOIN Rating r " +
            "                   ON r.ToiletId = t.Id " +
            "WHERE t.AccountId = :accountId " +
            "GROUP BY t.Id, t.Name, t.Address, t.Ward, t.District, t.Province, t.Longitude, t.Latitude, t.NearBy, " +
            "         t.OpenTime, t.CloseTime, t.isFree, " +
            "         f.Name, tf.Quantity, CAST(tf.Description AS NVARCHAR(MAX)), CAST(ti.ImageSource AS NVARCHAR(MAX))", nativeQuery = true)
    List<CustomToiletDetailsInfoDTO> getCustomToiletInfoDTOByAccountId(@Param("accountId") int accountId);

    @Query(value = "SELECT t.Id, " +
            "       t.Name                                          AS ToiletName, " +
            "       t.Address, " +
            "       t.Ward, " +
            "       t.District, " +
            "       t.Province, " +
            "       t.Latitude, " +
            "       t.Longitude, " +
            "       t.NearBy, " +
            "       t.OpenTime, " +
            "       t.CloseTime, " +
            "       t.isFree, " +
            "       IIF(t.isFree = 0, MIN(s.Price), 0)              AS MinPrice, " +
            "       IIF(t.isFree = 0, MAX(s.Price), 0)              AS MaxPrice, " +
            "       f.Name                                          AS FacilityName, " +
            "       COALESCE(tf.Quantity, 0)                        AS FacilityQuantity, " +
            "       CAST(tf.Description AS NVARCHAR(MAX))           AS FacilityDescription, " +
            "       COALESCE(ROUND(AVG(CAST(Star AS FLOAT)), 1), 0) AS RatingStar, " +
            "       CAST(ti.ImageSource AS NVARCHAR(MAX))           AS ToiletImage " +
            "FROM Toilet t " +
            "         CROSS JOIN Facility f " +
            "         JOIN ToiletService ts " +
            "              ON t.Id = ts.ToiletId " +
            "         JOIN Service s " +
            "              ON ts.ServiceId = s.Id " +
            "         LEFT JOIN ToiletImage ti " +
            "                   ON t.Id = ti.ToiletId " +
            "         LEFT JOIN ToiletFacility tf " +
            "                   ON f.Id = tf.FacilityId AND t.Id = tf.ToiletId " +
            "         LEFT JOIN Rating r " +
            "                   ON r.ToiletId = t.Id " +
            "WHERE t.Id = :toiletId " +
            "GROUP BY t.Id, t.Name, t.Address, t.Ward, t.District, t.Province, t.Longitude, t.Latitude, t.NearBy, " +
            "         t.OpenTime, t.CloseTime, t.isFree, " +
            "         f.Name, tf.Quantity, CAST(tf.Description AS NVARCHAR(MAX)), CAST(ti.ImageSource AS NVARCHAR(MAX))", nativeQuery = true)
    List<CustomToiletDetailsInfoDTO> getCustomToiletInfoDTOByToiletId(@Param("toiletId") int toiletId);

    @Query(value = "SELECT Id, Latitude, Longitude " +
            "FROM Toilet", nativeQuery = true)
    List<CustomToiletDTO> getAllToiletsIncludeIdLatitudeLongitude();
}
