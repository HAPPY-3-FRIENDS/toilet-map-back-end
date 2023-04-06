package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.entity.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<RatingEntity, Integer> {
}
