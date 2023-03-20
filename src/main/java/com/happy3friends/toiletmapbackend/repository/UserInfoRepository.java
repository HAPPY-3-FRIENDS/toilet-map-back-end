package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoEntity, Integer> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO UserInfo (AccountId, FullName, Gmail, Avatar, AccountBalance, AccountTurn, DefaultPayment) " +
            "VALUES ((SELECT Id FROM Account WHERE Username = :username), " +
            "        :fullName, " +
            "        :gmail, " +
            "        :avatar, " +
            "        :accountBalance, " +
            "        :accountTurn, " +
            "        :defaultPayment)", nativeQuery = true)
    void createUserInfo(@Param("username") String username,
                        @Param("fullName") String fullName,
                        @Param("gmail") String gmail,
                        @Param("avatar") String avatar,
                        @Param("accountBalance") Integer accountBalance,
                        @Param("accountTurn") Integer accountTurn,
                        @Param("defaultPayment") String defaultPayment);

    @Modifying
    @Transactional
    @Query(value = "UPDATE UserInfo " +
            "SET AccountBalance = :accountBalance " +
            "WHERE AccountId = :accountId", nativeQuery = true)
    void updateAccountBalance(@Param("accountId") int accountId, @Param("accountBalance") int accountBalance);

    @Modifying
    @Transactional
    @Query(value = "UPDATE UserInfo " +
            "SET AccountTurn = :accountTurn " +
            "WHERE AccountId = :accountId", nativeQuery = true)
    void updateAccountTurn(@Param("accountId") int accountId, @Param("accountTurn") int accountTurn);

    @Modifying
    @Transactional
    @Query(value = "UPDATE UserInfo " +
            "SET AccountBalance = :accountBalance, AccountTurn = :accountTurn " +
            "WHERE AccountId = :accountId", nativeQuery = true)
    void updateAccountBalanceAndAccountTurn(@Param("accountId") int accountId,
                                            @Param("accountBalance") int accountBalance,
                                            @Param("accountTurn") int accountTurn);
}
