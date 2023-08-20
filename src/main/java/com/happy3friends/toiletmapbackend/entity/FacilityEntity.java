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
@Table(name = "Facility", schema = "dbo", catalog = "ToiletMap_Final_Final")
public class FacilityEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "Id", nullable = false)
    private int id;
    @Basic
    @Column(name = "Name", nullable = false, length = 200)
    private String name;
    @Basic
    @Column(name = "Type", nullable = false, length = 20)
    private String type;
    @OneToMany(mappedBy = "facilityByFacilityId")
    private Collection<ToiletFacilityEntity> toiletFacilitiesById;
}
