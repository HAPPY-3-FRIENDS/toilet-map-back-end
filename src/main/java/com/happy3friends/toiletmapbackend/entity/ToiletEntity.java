package com.happy3friends.toiletmapbackend.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Time;
import java.util.Collection;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "Toilet", schema = "dbo", catalog = "ToiletMap_Final_Final")
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
    @Column(name = "Ward", nullable = false, length = 50)
    private String ward;
    @Basic
    @Column(name = "District", nullable = false, length = 50)
    private String district;
    @Basic
    @Column(name = "Province", nullable = false, length = 50)
    private String province;
    @Basic
    @Column(name = "Latitude", precision = 0)
    private double latitude;
    @Basic
    @Column(name = "Longitude", precision = 0)
    private double longitude;
    @Basic
    @Column(name = "NearBy", nullable = true, length = 200)
    private String nearBy;
    @Basic
    @Column(name = "isFree", nullable = false)
    private boolean isFree;
    @Basic
    @Column(name = "OpenTime")
    private Time openTime;
    @Basic
    @Column(name = "CloseTime")
    private Time closeTime;
    @Basic
    @Column(name = "CompanyId", nullable = false)
    private int companyId;
    @Basic
    @Column(name = "Status", nullable = false, length = 20)
    private String status;
    @OneToMany(mappedBy = "toiletByToiletId")
    private Collection<RatingEntity> ratingsById;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CompanyId", referencedColumnName = "Id", insertable = false, updatable = false)
    private CompanyEntity companyByCompanyId;
    @OneToMany(mappedBy = "toiletByToiletId", cascade = CascadeType.ALL)
    private Collection<ToiletFacilityEntity> toiletFacilitiesById;
    @OneToMany(mappedBy = "toiletByToiletId", cascade = CascadeType.ALL)
    private Collection<ToiletImageEntity> toiletImagesById;
    @OneToMany(mappedBy = "toiletByToiletId", cascade = CascadeType.ALL)
    private Collection<ToiletServiceEntity> toiletServicesById;
    @OneToOne
    @MapsId
    @JoinColumn(name = "Id", referencedColumnName = "Id", nullable = false)
    private AccountEntity accountById;
    @OneToMany(mappedBy = "toiletByToiletId")
    private Collection<SuggestionEntity> suggestionsById;
    @OneToMany(mappedBy = "toiletByToiletId")
    private Collection<ReportEntity> reportsById;
}
