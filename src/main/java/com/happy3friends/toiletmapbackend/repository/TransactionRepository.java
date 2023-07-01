package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO [Transaction] (TransactionNo, PaymentId, BankCode, BankTranNo, CardType, PayDate)\n" +
            "VALUES (:transactionNo, :paymentId, :bankCode, :bankTranNo, :cardType, :payDate)", nativeQuery = true)
    void saveTransaction(@Param("transactionNo") int transactionNo,
                                @Param("paymentId") int paymentId,
                                @Param("bankCode") String bankCode,
                                @Param("bankTranNo") String bankTranNo,
                                @Param("cardType") String cardType,
                                @Param("payDate") Timestamp payDate);
}
