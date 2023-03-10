package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.dto.CustomCheckInDTO;
import com.happy3friends.toiletmapbackend.entity.CheckInEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckInRepository extends JpaRepository<CheckInEntity, Integer> {

    @Query(value = "SELECT ui.FullName, c.DateTime, s.Name as 'ServiceName', c.PaymentType, c.Balance, c.Turn " +
            "FROM CheckIn c INNER JOIN ToiletService ts " +
            "ON c.ToiletServiceId = ts.Id " +
            "INNER JOIN Account a " +
            "ON c.AccountId = a.Id " +
            "INNER JOIN UserInfo ui " +
            "ON a.Id = ui.AccountId " +
            "INNER JOIN Service s " +
            "ON ts.ServiceId = s.Id " +
            "WHERE ts.ToiletId = :toiletId", nativeQuery = true)
    List<CustomCheckInDTO> toiletCheckInHistoriesByToiletId(@Param("toiletId") int toiletId);
}
