package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
}
