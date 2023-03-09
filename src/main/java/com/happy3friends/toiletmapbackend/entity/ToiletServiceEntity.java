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
@Table(name = "ToiletService", schema = "dbo", catalog = "ToiletMap")
public class ToiletServiceEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "Id", nullable = false)
    private int id;
    @Basic
    @Column(name = "ServiceId", nullable = false)
    private int serviceId;
    @Basic
    @Column(name = "ToiletId", nullable = false)
    private int toiletId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ServiceId", referencedColumnName = "Id", nullable = false, insertable=false, updatable=false)
    private ServiceEntity serviceByServiceId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ToiletId", referencedColumnName = "Id", nullable = false, insertable=false, updatable=false)
    private ToiletEntity toiletByToiletId;
    @OneToMany(mappedBy = "toiletServiceByToiletServiceId")
    private Collection<CheckInEntity> checkInsById;
}
