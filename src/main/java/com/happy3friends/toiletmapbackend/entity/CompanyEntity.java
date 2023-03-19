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
@Table(name = "Company", schema = "dbo", catalog = "ToiletMap")
public class CompanyEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "Id", nullable = false)
    private int id;
    @Basic
    @Column(name = "Name", nullable = false, length = 50)
    private String name;
    @Basic
    @Column(name = "Logo", nullable = true, length = -1)
    private String logo;
    @Basic
    @Column(name = "Address", nullable = false, length = 100)
    private String address;
    @Basic
    @Column(name = "Ward", nullable = false, length = 50)
    private String ward;
    @Basic
    @Column(name = "District", nullable = false, length = 50)
    private String district;
    @Basic
    @Column(name = "Province", nullable = false, length = 50)
    private String province;
    @Basic
    @Column(name = "Phone", nullable = true, length = 20)
    private String phone;
    @OneToMany(mappedBy = "companyByCompanyId")
    private Collection<AccountEntity> accountsById;
    @OneToMany(mappedBy = "companyByCompanyId")
    private Collection<ToiletEntity> toiletsById;
}
