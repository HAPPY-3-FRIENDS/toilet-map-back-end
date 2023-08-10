package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.entity.ComboEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComboRepository extends JpaRepository<ComboEntity, Integer> {

    ComboEntity findByTotalTurn(int totalTurn);
}
