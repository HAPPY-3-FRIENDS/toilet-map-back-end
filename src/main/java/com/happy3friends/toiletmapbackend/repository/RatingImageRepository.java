package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.entity.RatingImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingImageRepository extends JpaRepository<RatingImageEntity, Integer> {
}
