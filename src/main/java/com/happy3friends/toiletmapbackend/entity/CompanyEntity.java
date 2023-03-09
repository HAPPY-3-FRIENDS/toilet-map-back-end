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
    @Column(name = "Address", nullable = false, length = 100)
    private String address;
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
    private Collection<ToiletEntity> toiletsById;

}
