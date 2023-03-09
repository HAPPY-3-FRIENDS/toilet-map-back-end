package com.happy3friends.toiletmapbackend.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@EqualsAndHashCode
@Setter
@Getter
@Entity
@Table(name = "UserInfo", schema = "dbo", catalog = "ToiletMap")
public class UserInfoEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "Id", nullable = false)
    private int id;
    @Basic
    @Column(name = "AccountId", nullable = false)
    private int accountId;
    @Basic
    @Column(name = "Gmail", nullable = true, length = 22)
    private String gmail;
    @Basic
    @Column(name = "Avatar", nullable = true, length = 100)
    private String avatar;
    @Basic
    @Column(name = "AccountBalance", nullable = false, precision = 0)
    private double accountBalance;
    @Basic
    @Column(name = "AccountTurn", nullable = false)
    private int accountTurn;
    @Basic
    @Column(name = "DefaultPayment", nullable = false, length = 20)
    private String defaultPayment;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AccountId", referencedColumnName = "Id", insertable = false, updatable = false)
    private AccountEntity accountByAccountId;

}
