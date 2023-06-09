package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.entity.SuggestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuggestionRepository extends JpaRepository<SuggestionEntity, Integer> {
}
