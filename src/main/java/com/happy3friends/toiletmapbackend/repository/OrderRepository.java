package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.entity.OrderEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO [Order] (AccountId, TotalTurn, TotalPrice, PaymentMethod, DateTime) " +
            "VALUES (:accountId, :totalTurn, :totalPrice, :paymentMethod, :dateTime)", nativeQuery = true)
    void createOrderByAccountId(@Param("accountId") int accountId,
                                @Param("totalTurn") int totalTurn,
                                @Param("totalPrice") int totalPrice,
                                @Param("paymentMethod") String paymentMethod,
                                @Param("dateTime") Timestamp dateTime);

    @Query(value = "SELECT * " +
            "FROM [Order] " +
            "WHERE AccountId = :accountId", nativeQuery = true)
    List<OrderEntity> findAllByAccountId(@Param("accountId") int accountId,
                                         Pageable pageable);

    @Query(value = "SELECT COUNT(*) " +
            "FROM [Order] " +
            "WHERE AccountId = :accountId", nativeQuery = true)
    int countOrderHistoriesByAccountId(@Param("accountId") int accountId);

    @Query(value = "SELECT COUNT(*) " +
            "FROM [Order] ", nativeQuery = true)
    long count();
}
