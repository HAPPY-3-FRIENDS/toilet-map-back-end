package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.dto.CustomToiletDTO;
import com.happy3friends.toiletmapbackend.dto.CustomToiletDetailsInfoDTO;
import com.happy3friends.toiletmapbackend.dto.CustomToiletResponseDTO;
import com.happy3friends.toiletmapbackend.entity.ToiletEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToiletRepository extends JpaRepository<ToiletEntity, Integer>, CrudRepository<ToiletEntity, Integer> {

    @Query(value = "SELECT * " +
            "FROM (SELECT t.Id, " +
            "             t.Name                                                                                AS ToiletName, " +
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
            "             IIF(t.isFree = 0, MIN(s.Price), 0)                                                    AS MinPrice, " +
            "             IIF(t.isFree = 0, MAX(s.Price), 0)                                                    AS MaxPrice, " +
            "             f.Id                                                                                  AS FacilityId, " +
            "             f.Name                                                                                AS FacilityName, " +
            "             f.Type                                                                                AS FacilityType, " +
            "             COALESCE(tf.Quantity, 0)                                                              AS FacilityQuantity, " +
            "             CAST(tf.Description AS NVARCHAR(MAX))                                                 AS FacilityDescription, " +
            "             COALESCE(ROUND(AVG(CAST(Star AS FLOAT)), 1), 0)                                       AS RatingStar, " +
            "             CAST(ti.ImageSource AS NVARCHAR(MAX))                                                 AS ToiletImage, " +
            "             DENSE_RANK() OVER (ORDER BY SQRT(SQUARE(:currentLatitude - t.Latitude) + " +
            "                                              SQUARE(:currentLongitude - t.Longitude)) ASC)        AS Rank " +
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
            "               LEFT JOIN ToiletImage ti " +
            "                         ON t.Id = ti.ToiletId " +
            "      WHERE t.Status != N'Không hoạt động' " +
            "      GROUP BY t.Id, t.Name, t.Address, t.Ward, t.District, t.Province, t.Latitude, t.Longitude, t.NearBy, " +
            "               t.OpenTime, t.CloseTime, t.isFree, f.Id, f.Name, f.Type, tf.Quantity, " +
            "               CAST(tf.Description AS NVARCHAR(MAX)), CAST(ti.ImageSource AS NVARCHAR(MAX))) AS RS " +
            "WHERE Rank <= 10", nativeQuery = true)
    List<CustomToiletDetailsInfoDTO> getTop10ToiletsNearByCurrentLocation(@Param("currentLatitude") Double currentLatitude,
                                                                          @Param("currentLongitude") Double currentLongitude);

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
            "       f.Id                                            AS FacilityId, " +
            "       f.Name                                          AS FacilityName, " +
            "       f.Type                                          AS FacilityType, " +
            "       COALESCE(tf.Quantity, 0)                        AS FacilityQuantity, " +
            "       CAST(tf.Description AS NVARCHAR(MAX))           AS FacilityDescription, " +
            "       COALESCE(ROUND(AVG(CAST(Star AS FLOAT)), 1), 0) AS RatingStar, " +
            "       CAST(ti.ImageSource AS NVARCHAR(MAX))           AS ToiletImage, " +
            "       a.Username, " +
            "       t.Status " +
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
            "         JOIN Account a " +
            "              ON t.Id = a.Id " +
            "WHERE t.Id = :toiletId " +
            "GROUP BY t.Id, t.Name, t.Address, t.Ward, t.District, t.Province, t.Longitude, t.Latitude, t.NearBy, " +
            "         t.OpenTime, t.CloseTime, t.isFree, " +
            "         f.Id, f.Name, f.Type, tf.Quantity, CAST(tf.Description AS NVARCHAR(MAX)), " +
            "         CAST(ti.ImageSource AS NVARCHAR(MAX)), " +
            "         a.Username, t.Status", nativeQuery = true)
    List<CustomToiletDetailsInfoDTO> getCustomToiletInfoDTOByToiletId(@Param("toiletId") int toiletId);

    @Query(value = "SELECT Id, Latitude, Longitude " +
            "FROM Toilet " +
            "WHERE Status != N'Không hoạt động'", nativeQuery = true)
    List<CustomToiletDTO> getAllToiletsIncludeIdLatitudeLongitude();

    @Query(value = "SELECT t.Id, " +
            "       t.Name     AS ToiletName, " +
            "       a.Username AS Username, " +
            "       t.Address, " +
            "       t.Ward, " +
            "       t.District, " +
            "       t.Province, " +
            "       t.Latitude, " +
            "       t.Longitude, " +
            "       t.Status " +
            "FROM Toilet t " +
            "         JOIN Company c " +
            "              ON t.CompanyId = c.Id " +
            "         JOIN Account a " +
            "              ON t.Id = a.Id " +
            "WHERE c.Id = :companyId", nativeQuery = true)
    List<CustomToiletDetailsInfoDTO> getAllToiletsByCompanyId(@Param("companyId") int companyId, Pageable pageable);

    long countByCompanyId(int companyId);

    @Query(value =
            "SELECT Id " +
                    "FROM Toilet", nativeQuery = true)
    List<Integer> getAllToiletId();

    @Query(value = "SELECT t.Id, t.Name, t1.NumberOfRestroom, t2.NumberOfBathroom\n" +
            "FROM Toilet t\n" +
            "INNER JOIN\n" +
            "    (SELECT t.Id ,SUM(tf.Quantity) AS NumberOfRestroom\n" +
            "    FROM Toilet t\n" +
            "    INNER JOIN ToiletFacility tf on t.Id = tf.ToiletId\n" +
            "    WHERE tf.FacilityId = 1 OR tf.FacilityId = 3\n" +
            "    GROUP BY t.Id) t1 ON t.Id = t1.Id\n" +
            "INNER JOIN\n" +
            "    (SELECT t.Id ,SUM(tf.Quantity) AS NumberOfBathroom\n" +
            "    FROM Toilet t\n" +
            "    INNER JOIN ToiletFacility tf on t.Id = tf.ToiletId\n" +
            "    WHERE tf.FacilityId = 2\n" +
            "    GROUP BY t.Id) t2 ON t.Id = t2.Id\n" +
            "WHERE t.District = :district\n" +
            "    AND t.Status = N'Đang hoạt động'", nativeQuery = true)
    List<CustomToiletResponseDTO> getToiletsByDistrict(@Param("district") String district);
}
