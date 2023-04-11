package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.entity.PaymentEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Integer> {

    List<PaymentEntity> findAllByAccountId(int accountId, Pageable pageable);
}
