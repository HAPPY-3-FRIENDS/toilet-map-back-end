package com.happy3friends.toiletmapbackend.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "Configuration", schema = "dbo", catalog = "ToiletMap_Final_Final")
public class ConfigurationEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "Id", nullable = false)
    private int id;
    @Basic
    @Column(name = "Name", nullable = false, length = 50)
    private String name;
    @Basic
    @Column(name = "Value", nullable = false, length = 50)
    private String value;
}
