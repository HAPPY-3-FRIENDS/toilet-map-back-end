package com.happy3friends.toiletmapbackend.repository;

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
            "FROM (SELECT t.Id, " +
            "             t.Name                                                                                    AS ToiletName, " +
            "             t.Address, " +
            "             t.Ward, " +
            "             t.District, " +
            "             t.Province, " +
            "             t.Latitude, " +
            "             t.Longitude, " +
            "             t.NearBy, " +
            "             t.OpenTime, " +
            "             t.CloseTime, " +
            "             t.isFree, " +
            "             IIF(t.isFree = 0, MIN(s.Price), 0)                                                        AS MinPrice, " +
            "             IIF(t.isFree = 0, MAX(s.Price), 0)                                                        AS MaxPrice, " +
            "             f.Name                                                                                    AS FacilityName, " +
            "             (CASE f.Type " +
            "                  WHEN N'Phòng' THEN COALESCE(tf.Quantity, 0) " +
            "                 END)                                                                                  AS FacilityQuantity, " +
            "             IIF(tf.Id IS NOT NULL, CAST(1 AS BIT), CAST(0 AS BIT))                                    AS isFacilityHave, " +
            "             COALESCE(ROUND(AVG(CAST(Star AS FLOAT)), 1), 0)                                           AS RatingStar, " +
            "             CAST(ti.ImageSource AS NVARCHAR(MAX))                                                     AS ToiletImage, " +
            "             ROW_NUMBER() OVER (PARTITION BY t.Id, t.Name, t.Address, t.Ward, t.District, t.Province, t.Longitude, t.Latitude, t.NearBy, " +
            "                 t.isFree, t.OpenTime, t.CloseTime, f.Name, f.Type, tf.Quantity, tf.Id ORDER BY t.Id ) AS RowNumber " +
            "      FROM Toilet t " +
            "               CROSS JOIN Facility f " +
            "               JOIN ToiletService ts " +
            "                    ON t.Id = ts.ToiletId " +
            "               JOIN Service s " +
            "                    ON ts.ServiceId = s.Id " +
            "               LEFT JOIN ToiletFacility tf " +
            "                         ON f.Id = tf.FacilityId AND t.Id = tf.ToiletId " +
            "               LEFT JOIN Rating r " +
            "                         ON r.ToiletId = t.Id " +
            "               left JOIN ToiletImage ti " +
            "                         ON t.Id = ti.ToiletId " +
            "      GROUP BY t.Id, t.Name, t.Address, t.Ward, t.District, t.Province, t.Longitude, t.Latitude, t.NearBy, " +
            "               t.isFree, t.OpenTime, t.CloseTime, f.Name, f.Type, " +
            "               tf.Quantity, tf.Id, CAST(ti.ImageSource AS NVARCHAR(MAX))) AS A " +
            "WHERE RowNumber = 1", nativeQuery = true)
    List<CustomToiletDetailsInfoDTO> getListCustomToiletInfoDTO();

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
}
