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
@Table(name = "Service", schema = "dbo", catalog = "ToiletMap")
public class ServiceEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "Id", nullable = false)
    private int id;
    @Basic
    @Column(name = "Name", nullable = false, length = 50)
    private String name;
    @Basic
    @Column(name = "Price", nullable = false, precision = 0)
    private double price;
    @Basic
    @Column(name = "Turn", nullable = false)
    private int turn;
    @OneToMany(mappedBy = "serviceByServiceId")
    private Collection<ToiletServiceEntity> toiletServicesById;

}
