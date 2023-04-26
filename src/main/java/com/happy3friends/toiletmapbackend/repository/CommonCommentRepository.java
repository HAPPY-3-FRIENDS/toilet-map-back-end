package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.entity.CommonCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommonCommentRepository extends JpaRepository<CommonCommentEntity, Integer> {

}
