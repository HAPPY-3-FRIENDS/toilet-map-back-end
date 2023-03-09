package com.happy3friends.toiletmapbackend.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@EqualsAndHashCode
@Setter
@Getter
@Entity
@Table(name = "Account", schema = "dbo", catalog = "ToiletMap")
public class AccountEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "Id", nullable = false)
    private int id;
    @Basic
    @Column(name = "Username", nullable = false, length = 20)
    private String username;
    @Basic
    @Column(name = "Status", nullable = true, length = 20)
    private String status;
    @Basic
    @Column(name = "RoleId", nullable = false)
    private int roleId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RoleId", referencedColumnName = "Id", nullable = false, insertable=false, updatable=false)
    private RoleEntity roleByRoleId;
    @OneToMany(mappedBy = "accountByAccountId")
    private Collection<UserInfoEntity> userInfosById;
    @OneToMany(mappedBy = "accountByAccountId")
    private Collection<CheckInEntity> checkInsById;
}
