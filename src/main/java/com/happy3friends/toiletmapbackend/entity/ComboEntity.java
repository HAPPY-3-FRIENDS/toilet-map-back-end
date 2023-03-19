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
@Table(name = "Combo", schema = "dbo", catalog = "ToiletMap")
public class ComboEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "Id", nullable = false)
    private int id;
    @Basic
    @Column(name = "TotalTurn", nullable = false)
    private int totalTurn;
    @Basic
    @Column(name = "Price", nullable = false)
    private int price;
    @OneToMany(mappedBy = "comboByComboId")
    private Collection<OrderEntity> ordersById;
}
