package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.dto.CustomPaymentDTO;
import com.happy3friends.toiletmapbackend.entity.PaymentEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Integer>, CrudRepository<PaymentEntity, Integer> {

    @Query(value = "SELECT * " +
            "FROM Payment " +
            "WHERE AccountId = :accountId", nativeQuery = true)
    List<CustomPaymentDTO> findAllByAccountId(int accountId, Pageable pageable);

    long countByAccountId(int accountId);
}
