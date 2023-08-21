package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.entity.ConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends JpaRepository<ConfigurationEntity, Integer> {
    @Query(value = "SELECT * " +
            "FROM Configuration " +
            "WHERE Id = :id", nativeQuery = true)
    ConfigurationEntity getConfigById(@Param("id") String id);
}
