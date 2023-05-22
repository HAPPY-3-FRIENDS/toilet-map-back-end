package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.entity.SensitiveWordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensitiveWordRepository extends JpaRepository<SensitiveWordEntity, Integer> {
}
