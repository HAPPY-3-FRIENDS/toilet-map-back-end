package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.constant.DefaultAccountNameConstant;
import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.dto.CustomCheckInDTO;
import com.happy3friends.toiletmapbackend.entity.CheckInEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CheckInRepository extends JpaRepository<CheckInEntity, Integer> {

    @Query(value = "SELECT (IIF(r.Name = '" + RoleConstant.TOILET + "', '" + DefaultAccountNameConstant.WALK_IN_GUEST + "', ui.FullName)) as FullName, " +
            "       c.DateTime, " +
            "       s.Name                                                 as 'ServiceName', " +
            "       c.PaymentMethod, " +
            "       c.Balance, " +
            "       c.Turn " +
            "FROM CheckIn c " +
            "         INNER JOIN ToiletService ts " +
            "                    ON c.ToiletServiceId = ts.Id " +
            "         INNER JOIN Account a " +
            "                    ON c.AccountId = a.Id " +
            "         INNER JOIN Role r " +
            "                    ON r.Id = a.RoleId " +
            "         INNER JOIN Service s " +
            "                    ON ts.ServiceId = s.Id " +
            "         LEFT JOIN UserInfo ui on a.Id = ui.AccountId " +
            "WHERE ts.ToiletId = :toiletId " +
            "ORDER BY DateTime DESC ", nativeQuery = true)
    List<CustomCheckInDTO> getCheckInHistoriesByToiletId(@Param("toiletId") int toiletId);

    @Query(value = "SELECT c.DateTime,\n" +
            "    t.Name AS ToiletName,\n" +
            "    t.Id AS ToiletId,\n" +
            "    s.Name AS ServiceName,\n" +
            "    c.Balance,\n" +
            "    c.Turn,\n" +
            "    c.Id,\n" +
            "    CASE\n" +
            "        WHEN r.CheckInId IS NOT NULL THEN N'Đã đánh giá'\n" +
            "        WHEN DATEDIFF(second, CONVERT(DATETIME2, c.DateTime), DATEADD(hour , 7, sysdatetime())) > 3600 THEN N'Đã hết hạn'\n" +
            "        ELSE N'Chưa đánh giá'\n" +
            "    END AS status\n" +
            "FROM CheckIn c\n" +
            "INNER JOIN ToiletService ts\n" +
            "    ON c.ToiletServiceId = ts.Id\n" +
            "INNER JOIN Toilet t\n" +
            "    ON ts.ToiletId = t.Id\n" +
            "INNER JOIN Service s\n" +
            "    ON ts.ServiceId = s.Id\n" +
            "LEFT JOIN Rating r on c.Id = r.CheckInId\n" +
            "WHERE c.AccountId = :accountId\n" +
            "    AND (:paymentMethod IS NULL OR c.PaymentMethod = :paymentMethod)",
            nativeQuery = true)
    List<CustomCheckInDTO> getCheckInHistoriesByAccountId(@Param("accountId") int accountId,
                                                          @Param("paymentMethod") String paymentMethod,
                                                          Pageable pageable);

    @Query(value = "SELECT COUNT(*) " +
            "FROM CheckIn c " +
            "         INNER JOIN ToiletService ts " +
            "                    ON c.ToiletServiceId = ts.Id " +
            "         INNER JOIN Toilet t " +
            "                    ON ts.ToiletId = t.Id " +
            "         INNER JOIN Service s " +
            "                    ON ts.ServiceId = s.Id " +
            "WHERE c.AccountId = :accountId " +
            "  AND (:paymentMethod IS NULL OR c.PaymentMethod = :paymentMethod) ", nativeQuery = true)
    int countCheckInHistoriesByAccountId(@Param("accountId") int accountId,
                                         @Param("paymentMethod") String paymentMethod);

    @Query(value = "SELECT COUNT(*)\n" +
            "FROM (\n" +
            "    SELECT\n" +
            "        CASE\n" +
            "            WHEN r.CheckInId IS NOT NULL THEN N'Đã đánh giá'\n" +
            "            WHEN DATEDIFF(second, CONVERT(DATETIME2, c.DateTime), DATEADD(hour , 7, sysdatetime())) > 3600 THEN N'Đã hết hạn'\n" +
            "            ELSE N'Chưa đánh giá'\n" +
            "        END AS status\n" +
            "    FROM CheckIn c\n" +
            "    INNER JOIN ToiletService ts\n" +
            "        ON c.ToiletServiceId = ts.Id\n" +
            "    INNER JOIN Toilet t\n" +
            "        ON ts.ToiletId = t.Id\n" +
            "    INNER JOIN Service s\n" +
            "        ON ts.ServiceId = s.Id\n" +
            "    LEFT JOIN Rating r\n" +
            "        ON c.Id = r.CheckInId\n" +
            "    WHERE c.AccountId = :accountId\n" +
            "    AND (:paymentMethod IS NULL OR c.PaymentMethod = :paymentMethod)) c\n" +
            "WHERE c.status = N'Chưa đánh giá'", nativeQuery = true)
    int countCheckInNotRatingYetHistoriesByAccountId(@Param("accountId") int accountId,
                                         @Param("paymentMethod") String paymentMethod);

    @Query(value = "SELECT COUNT(*)\n" +
            "FROM CheckIn c\n" +
            "INNER JOIN ToiletService ts on c.ToiletServiceId = ts.Id\n" +
            "WHERE ts.ToiletId = :toiletId\n" +
            "    AND c.Turn = :turn\n" +
            "    AND (DateTime >= :startDate AND DateTime < :endDate)\n" +
            "    AND (:now BETWEEN DateTime AND CheckoutTime)", nativeQuery = true)
    int getNumberNotAvailableRoom(@Param("toiletId") int toiletId,
                                      @Param("turn") int turn,
                                      @Param("startDate") String startDate,
                                      @Param("endDate") String endDate,
                                      @Param("now") String now);

    @Transactional
    @Modifying
    @Query(value = "UPDATE CheckIn\n" +
            "SET CheckoutTime = :now\n" +
            "WHERE Id IN\n" +
            "(SELECT c.Id\n" +
            "FROM CheckIn c\n" +
            "INNER JOIN ToiletService ts on c.ToiletServiceId = ts.Id\n" +
            "WHERE ts.ToiletId = :toiletId\n" +
            "    AND (c.Turn = 2 OR c.Turn = 3)\n" +
            "    AND (DateTime >= :startDate AND DateTime < :endDate)\n" +
            "    AND (:now BETWEEN DateTime AND CheckoutTime))", nativeQuery = true)
    void checkout(@Param("toiletId") int toiletId,
                  @Param("startDate") String startDate,
                  @Param("endDate") String endDate,
                  @Param("now") String now);

    @Query(value = "SELECT c.Id\n" +
            "FROM CheckIn c\n" +
            "INNER JOIN ToiletService ts on c.ToiletServiceId = ts.Id\n" +
            "WHERE ts.ToiletId = :toiletId\n" +
            "    AND (c.Turn = 2 OR c.Turn = 3)\n" +
            "    AND (DateTime >= :startDate AND DateTime < :endDate)\n" +
            "    AND (:now BETWEEN DateTime AND CheckoutTime)", nativeQuery = true)
    List<Integer> getListAvailableCheckIn(@Param("toiletId") int toiletId,
                                          @Param("startDate") String startDate,
                                          @Param("endDate") String endDate,
                                          @Param("now") String now);

    @Query(value = "SELECT ui.FullName\n" +
            "FROM CheckIn c\n" +
            "INNER JOIN Account a on a.Id = c.AccountId\n" +
            "INNER JOIN UserInfo ui on a.Id = ui.AccountId\n" +
            "WHERE c.Id IN :list", nativeQuery = true)
    List<String> getListUserByListCheckInId(@Param("list") List<Integer> list);
}
