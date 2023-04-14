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
    @Column(name = "Price", nullable = false)
    private int price;
    @Basic
    @Column(name = "Turn", nullable = false)
    private int turn;
    @OneToMany(mappedBy = "serviceByServiceId")
    private Collection<ToiletServiceEntity> toiletServicesById;
    @Basic
    @Column(name = "TurnPrice", nullable = false)
    private int turnPrice;
}
