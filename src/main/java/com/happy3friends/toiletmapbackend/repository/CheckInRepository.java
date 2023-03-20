package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.dto.CustomCheckInDTO;
import com.happy3friends.toiletmapbackend.entity.CheckInEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckInRepository extends JpaRepository<CheckInEntity, Integer> {

    @Query(value = "SELECT (IIF(r.Name = '" + RoleConstant.STAFF + "', 'Khách vãng lai', ui.FullName)) as FullName, " +
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
    List<CustomCheckInDTO> toiletCheckInHistoriesByToiletId(@Param("toiletId") int toiletId);
}
