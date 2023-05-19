package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.entity.FacilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacilityRepository extends JpaRepository<FacilityEntity, Integer> {
    List<FacilityEntity> findByType(String type);

    boolean existsByName(String name);
}
