package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO [Order] (AccountId, ComboId, TotalTurn, TotalPrice, PaymentMethod, DateTime) " +
            "VALUES (:accountId, :comboId, :totalTurn, :totalPrice, :paymentMethod, :dateTime)", nativeQuery = true)
    void createOrderByAccountId(@Param("accountId") int accountId,
                                @Param("comboId") int comboId,
                                @Param("totalTurn") int totalTurn,
                                @Param("totalPrice") int totalPrice,
                                @Param("paymentMethod") String paymentMethod,
                                @Param("dateTime") Timestamp dateTime);
}
