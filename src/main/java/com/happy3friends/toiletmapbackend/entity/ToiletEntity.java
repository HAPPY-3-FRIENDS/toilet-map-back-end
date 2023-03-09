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
@Table(name = "Toilet", schema = "dbo", catalog = "ToiletMap")
public class ToiletEntity {
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
    @Column(name = "Status", nullable = true, length = 20)
    private String status;
    @Basic
    @Column(name = "CompanyId", nullable = false)
    private int companyId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CompanyId", referencedColumnName = "Id", insertable = false, updatable = false)
    private CompanyEntity companyByCompanyId;
    @OneToMany(mappedBy = "toiletByToiletId")
    private Collection<ToiletServiceEntity> toiletServicesById;

}
