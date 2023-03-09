package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoEntity, Integer> {
}
