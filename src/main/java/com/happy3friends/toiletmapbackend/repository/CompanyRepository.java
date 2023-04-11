package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Integer> {

    @Query(value = "SELECT c.* " +
            "FROM Company c JOIN Account a " +
            "ON c.Id = a.CompanyId " +
            "WHERE a.Id = :accountId", nativeQuery = true)
    CompanyEntity getCompanyByAccountId(@Param("accountId") int accountID);
}
