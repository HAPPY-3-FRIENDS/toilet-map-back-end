package com.happy3friends.toiletmapbackend.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "Account", schema = "dbo", catalog = "ToiletMap_Final_Final")
public class AccountEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "Id", nullable = false)
    private int id;
    @Basic
    @Column(name = "Username", nullable = false, length = 20)
    private String username;
    @Basic
    @Column(name = "Password", nullable = true, length = 60)
    private String password;
    @Basic
    @Column(name = "Status", nullable = false, length = 20)
    private String status;
    @Basic
    @Column(name = "RoleId", nullable = false)
    private int roleId;
    @Basic
    @Column(name = "CompanyId", nullable = true)
    private Integer companyId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RoleId", referencedColumnName = "Id", insertable = false, updatable = false)
    private RoleEntity roleByRoleId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CompanyId", referencedColumnName = "Id", insertable = false, updatable = false)
    private CompanyEntity companyByCompanyId;
    @OneToMany(mappedBy = "accountByAccountId")
    private Collection<CheckInEntity> checkInsById;
    @OneToMany(mappedBy = "accountByAccountId")
    private Collection<OrderEntity> ordersById;
    @OneToOne(mappedBy = "accountByAccountId")
    private UserInfoEntity userInfoById;
    @OneToMany(mappedBy = "accountByAccountId")
    private Collection<PaymentEntity> paymentsById;
    @OneToOne(mappedBy = "accountById", cascade = CascadeType.ALL)
    private ToiletEntity toiletById;
}
