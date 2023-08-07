package com.happy3friends.toiletmapbackend.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "ToiletFacility", schema = "dbo", catalog = "ToiletMap")
public class ToiletFacilityEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "Id", nullable = false)
    private int id;
    @Basic
    @Column(name = "ToiletId", nullable = false)
    private int toiletId;
    @Basic
    @Column(name = "FacilityId", nullable = false)
    private int facilityId;
    @Basic
    @Column(name = "Quantity")
    private Integer quantity;
    @Basic
    @Column(name = "TotalQuantity")
    private Integer totalQuantity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ToiletId", referencedColumnName = "Id", insertable = false, updatable = false)
    private ToiletEntity toiletByToiletId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FacilityId", referencedColumnName = "Id", insertable = false, updatable = false)
    private FacilityEntity facilityByFacilityId;
}
