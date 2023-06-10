package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Integer> {
}
