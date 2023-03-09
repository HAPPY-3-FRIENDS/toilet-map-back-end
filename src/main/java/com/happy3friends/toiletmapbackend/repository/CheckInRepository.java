package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.entity.CheckInEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckInRepository extends JpaRepository<CheckInEntity, Integer> {
}
