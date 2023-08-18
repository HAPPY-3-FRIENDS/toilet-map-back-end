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
@Table(name = "ToiletService", schema = "dbo", catalog = "ToiletMap_DEMO")
public class ToiletServiceEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "Id", nullable = false)
    private int id;
    @Basic
    @Column(name = "ToiletId", nullable = false)
    private int toiletId;
    @Basic
    @Column(name = "ServiceId", nullable = false)
    private int serviceId;
    @OneToMany(mappedBy = "toiletServiceByToiletServiceId")
    private Collection<CheckInEntity> checkInsById;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ToiletId", referencedColumnName = "Id", insertable = false, updatable = false)
    private ToiletEntity toiletByToiletId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ServiceId", referencedColumnName = "Id", insertable = false, updatable = false)
    private ServiceEntity serviceByServiceId;
}
