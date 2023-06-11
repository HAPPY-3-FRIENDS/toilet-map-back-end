package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.dto.CompanyHasStatusDTO;
import com.happy3friends.toiletmapbackend.entity.CompanyEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Integer> {

    @Query(value = "SELECT c.* " +
            "FROM Company c JOIN Account a " +
            "ON c.Id = a.CompanyId " +
            "WHERE a.Id = :accountId", nativeQuery = true)
    CompanyEntity getCompanyByAccountId(@Param("accountId") int accountID);

    @Query(value =
            "SELECT c.*,\n" +
            "       (SELECT COUNT(*)\n" +
            "        FROM Report r\n" +
            "        LEFT JOIN Toilet t ON r.ToiletId = t.Id\n" +
            "        LEFT JOIN Company co on t.CompanyId = co.Id\n" +
            "        WHERE co.Id = c.Id) AS NumberOfReport\n" +
            "FROM Company c\n" +
            "INNER JOIN Account a ON c.Id = a.CompanyId\n" +
            "WHERE a.RoleId = 2", nativeQuery = true)
    List<CompanyHasStatusDTO> getAllCompanies(Pageable pageable);

    @Query(value = "SELECT c.* " +
            "FROM Company c " +
            "INNER JOIN Account a " +
            "    ON c.Id = a.CompanyId " +
            "WHERE a.RoleId = 2 " +
            "    AND (UPPER(c.Name) LIKE :searchText " +
            "        OR UPPER(c.Address) LIKE :searchText " +
            "        OR UPPER(c.Ward) LIKE :searchText " +
            "        OR UPPER(c.District) LIKE :searchText " +
            "        OR UPPER(c.Province) LIKE :searchText)", nativeQuery = true)
    List<CompanyHasStatusDTO> searchCompany(String searchText, Pageable pageable);


    @Query(value = "SELECT COUNT(*) " +
            "FROM " +
            "    (SELECT c.* " +
            "    FROM Company c " +
            "    INNER JOIN Account a " +
            "        ON c.Id = a.CompanyId " +
            "    WHERE a.RoleId = 2 " +
            "        AND (UPPER(c.Name) LIKE :searchText " +
            "        OR UPPER(c.Address) LIKE :searchText " +
            "        OR UPPER(c.Ward) LIKE :searchText " +
            "        OR UPPER(c.District) LIKE :searchText " +
            "        OR UPPER(c.Province) LIKE :searchText)) r", nativeQuery = true)
    int countSearchingCompanies(String searchText);
}
