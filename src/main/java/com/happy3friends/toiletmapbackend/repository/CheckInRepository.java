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

    @Query(value = "SELECT c.DateTime, " +
            "       t.Name  AS ToiletName, " +
            "       t.Id    AS ToiletId, " +
            "       s.Name  AS ServiceName, " +
            "       c.Balance, " +
            "       c.Turn, " +
            "       c.Id, " +
            "       CASE " +
            "           WHEN r.CheckInId IS NOT NULL THEN N'Đã đánh giá' " +
            "           WHEN DATEDIFF(second, CONVERT(DATETIME2, c.DateTime), sysdatetime()) > 3600 THEN N'Đã hết hạn' " +
            "           ELSE N'Chưa đánh giá' " +
            "           END AS status " +
            "FROM CheckIn c " +
            "         INNER JOIN ToiletService ts " +
            "                    ON c.ToiletServiceId = ts.Id " +
            "         INNER JOIN Toilet t " +
            "                    ON ts.ToiletId = t.Id " +
            "         INNER JOIN Service s " +
            "                    ON ts.ServiceId = s.Id " +
            "         LEFT JOIN Rating r on c.Id = r.CheckInId " +
            "         INNER JOIN Company cp " +
            "                    ON t.CompanyId = cp.Id " +
            "WHERE (:companyId IS NULL OR cp.Id = :companyId) " +
            "  AND (:accountId IS NULL OR c.AccountId = :accountId) " +
            "  AND (:paymentMethod IS NULL OR c.PaymentMethod = :paymentMethod)", nativeQuery = true)
    List<CustomCheckInDTO> getCheckInHistories(@Param("companyId") Integer companyId,
                                               @Param("accountId") Integer accountId,
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
            "         LEFT JOIN Rating r on c.Id = r.CheckInId " +
            "         INNER JOIN Company cp " +
            "                    ON t.CompanyId = cp.Id " +
            "WHERE (:companyId IS NULL OR cp.Id = :companyId) " +
            "  AND (:accountId IS NULL OR c.AccountId = :accountId) " +
            "  AND (:paymentMethod IS NULL OR c.PaymentMethod = :paymentMethod)", nativeQuery = true)
    int countCheckInHistories(@Param("companyId") Integer companyId,
                              @Param("accountId") Integer accountId,
                              @Param("paymentMethod") String paymentMethod);

    @Query(value = "SELECT COUNT(*) " +
            "FROM ( " +
            "    SELECT " +
            "        CASE " +
            "            WHEN r.CheckInId IS NOT NULL THEN N'Đã đánh giá' " +
            "            WHEN DATEDIFF(second, CONVERT(DATETIME2, c.DateTime), DATEADD(hour , 7, sysdatetime())) > 3600 THEN N'Đã hết hạn' " +
            "            ELSE N'Chưa đánh giá' " +
            "        END AS status " +
            "    FROM CheckIn c " +
            "    INNER JOIN ToiletService ts " +
            "        ON c.ToiletServiceId = ts.Id " +
            "    INNER JOIN Toilet t " +
            "        ON ts.ToiletId = t.Id " +
            "    INNER JOIN Service s " +
            "        ON ts.ServiceId = s.Id " +
            "    LEFT JOIN Rating r " +
            "        ON c.Id = r.CheckInId " +
            "    WHERE c.AccountId = :accountId " +
            "    AND (:paymentMethod IS NULL OR c.PaymentMethod = :paymentMethod)) c " +
            "WHERE c.status = N'Chưa đánh giá'", nativeQuery = true)
    int countCheckInNotRatingYetHistoriesByAccountId(@Param("accountId") int accountId,
                                         @Param("paymentMethod") String paymentMethod);

    @Query(value = "SELECT COUNT(*) " +
            "FROM CheckIn c " +
            "INNER JOIN ToiletService ts on c.ToiletServiceId = ts.Id " +
            "WHERE ts.ToiletId = :toiletId " +
            "    AND ts.ServiceId = :serviceId " +
            "    AND (DateTime >= :startDate AND DateTime < :endDate) " +
            "    AND (:now BETWEEN DateTime AND CheckoutTime)", nativeQuery = true)
    int getNumberNotAvailableRoom(@Param("toiletId") int toiletId,
                                      @Param("serviceId") int serviceId,
                                      @Param("startDate") String startDate,
                                      @Param("endDate") String endDate,
                                      @Param("now") String now);

    @Transactional
    @Modifying
    @Query(value = "UPDATE CheckIn " +
            "SET CheckoutTime = :now " +
            "WHERE Id IN " +
            "(SELECT c.Id " +
            "FROM CheckIn c " +
            "INNER JOIN ToiletService ts on c.ToiletServiceId = ts.Id " +
            "WHERE ts.ToiletId = :toiletId " +
            "    AND ts.ServiceId !=  1 " +
            "    AND (DateTime >= :startDate AND DateTime < :endDate) " +
            "    AND (:now BETWEEN DateTime AND CheckoutTime))", nativeQuery = true)
    void checkout(@Param("toiletId") int toiletId,
                  @Param("startDate") String startDate,
                  @Param("endDate") String endDate,
                  @Param("now") String now);

    @Query(value = "SELECT c.Id " +
            "FROM CheckIn c " +
            "INNER JOIN ToiletService ts on c.ToiletServiceId = ts.Id " +
            "WHERE ts.ToiletId = :toiletId " +
            "    AND ts.ServiceId !=  1 " +
            "    AND (DateTime >= :startDate AND DateTime < :endDate) " +
            "    AND (:now BETWEEN DateTime AND CheckoutTime)", nativeQuery = true)
    List<Integer> getListAvailableCheckIn(@Param("toiletId") int toiletId,
                                          @Param("startDate") String startDate,
                                          @Param("endDate") String endDate,
                                          @Param("now") String now);

    @Query(value = "SELECT ui.FullName " +
            "FROM CheckIn c " +
            "INNER JOIN Account a on a.Id = c.AccountId " +
            "INNER JOIN UserInfo ui on a.Id = ui.AccountId " +
            "WHERE c.Id IN :list", nativeQuery = true)
    List<String> getListUserByListCheckInId(@Param("list") List<Integer> list);

    @Query(value = "SELECT * " +
            "FROM CheckIn c " +
            "WHERE c.Id IN :list", nativeQuery = true)
    List<CheckInEntity> getCheckInByListCheckInId(@Param("list") List<Integer> list);

    @Query(value = "SELECT MIN(DATEDIFF(minute, :now, c.CheckoutTime)) " +
            "FROM CheckIn c " +
            "WHERE c.Id IN :list", nativeQuery = true)
    Integer getWaitingTimeOfToilet(@Param("list") List<Integer> list,
                                   @Param("now") String now);

    @Query(value = "SELECT MIN(DATEDIFF(minute, :now, c.CheckoutTime)) " +
            "FROM CheckIn c " +
            "INNER JOIN ToiletService ts on c.ToiletServiceId = ts.Id " +
            "WHERE ts.ToiletId = :toiletId " +
            "    AND ts.ServiceId = :serviceId " +
            "    AND (DateTime >= :startDate AND DateTime < :endDate) " +
            "    AND (:now BETWEEN DateTime AND CheckoutTime)", nativeQuery = true)
    Integer getWaitingTime(@Param("toiletId") int toiletId,
                      @Param("startDate") String startDate,
                      @Param("endDate") String endDate,
                      @Param("now") String now,
                      @Param("serviceId") int serviceId);

    @Query(value = "SELECT c.Id " +
            "FROM CheckIn c " +
            "INNER JOIN ToiletService ts on c.ToiletServiceId = ts.Id " +
            "WHERE ts.ToiletId = :toiletId " +
            "    AND ts.ServiceId = :serviceId " +
            "    AND (DateTime >= :startDate AND DateTime < :endDate) " +
            "    AND (:now BETWEEN DateTime AND CheckoutTime) " +
            "ORDER BY c.CheckoutTime DESC ", nativeQuery = true)
    List<Integer> getListIdNeedCheckout(@Param("toiletId") int toiletId,
                                        @Param("startDate") String startDate,
                                        @Param("endDate") String endDate,
                                        @Param("now") String now,
                                        @Param("serviceId") int serviceId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE CheckIn " +
            "SET CheckoutTime = :now " +
            "WHERE Id IN :listId", nativeQuery = true)
    void checkoutByListId(@Param("listId") List<Integer> listId, @Param("now") String now);
}
