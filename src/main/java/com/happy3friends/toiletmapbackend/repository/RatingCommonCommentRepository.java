package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.entity.RatingCommonCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingCommonCommentRepository extends JpaRepository<RatingCommonCommentEntity, Integer> {
}
