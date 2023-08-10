package com.happy3friends.toiletmapbackend.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "UserInfo", schema = "dbo", catalog = "ToiletMap")
public class UserInfoEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "AccountId", nullable = false)
    private int accountId;
    @Basic
    @Column(name = "FullName", nullable = false, length = 100)
    private String fullName;
    @Basic
    @Column(name = "Avatar", nullable = true, length = 2147483647)
    private String avatar;
    @Column(name = "AccountBalance", nullable = false)
    private int accountBalance;
    @Basic
    @Column(name = "AccountTurn", nullable = false)
    private int accountTurn;
    @Basic
    @Column(name = "DefaultPayment", nullable = false, length = 20)
    private String defaultPayment;
    @OneToOne
    @JoinColumn(name = "AccountId", referencedColumnName = "Id", nullable = false)
    private AccountEntity accountByAccountId;
}
