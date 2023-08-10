package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.dto.CustomAccountInfoDTO;
import com.happy3friends.toiletmapbackend.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {

    AccountEntity findByUsername(String username);

    @Query(value = "SELECT a.Id AS AccountId, a.Username, a.Password, ui.FullName, ui.Avatar, r.Name as Role, ui.DefaultPayment, ui.AccountBalance, ui.AccountTurn " +
            "FROM Account a " +
            "         LEFT JOIN UserInfo ui " +
            "                   ON a.Id = ui.AccountId " +
            "         JOIN Role r " +
            "              ON r.Id = a.RoleId " +
            "WHERE a.Id = :Id", nativeQuery = true)
    CustomAccountInfoDTO getCustomAccountInfoByAccountId(@Param("Id") int id);

    @Query(value = "SELECT a.Id AS AccountId, a.Username, a.Password, ui.FullName, ui.Avatar, r.Name as Role, ui.DefaultPayment, ui.AccountBalance, ui.AccountTurn " +
            "FROM Account a " +
            "         LEFT JOIN UserInfo ui " +
            "                   ON a.Id = ui.AccountId " +
            "         JOIN Role r " +
            "              ON r.Id = a.RoleId " +
            "WHERE a.Username = :username", nativeQuery = true)
    CustomAccountInfoDTO getCustomAccountInfoByUsername(@Param("username") String username);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Account(Username, Password, Status, RoleId, CompanyId) " +
            "VALUES(:username, :password, :status, (SELECT Id FROM Role WHERE Name = :roleName), :companyId)", nativeQuery = true)
    void createAccount(@Param("username") String username,
                       @Param("password") String password,
                       @Param("status") String status,
                       @Param("roleName") String roleName,
                       @Param("companyId") Integer companyId);
}
