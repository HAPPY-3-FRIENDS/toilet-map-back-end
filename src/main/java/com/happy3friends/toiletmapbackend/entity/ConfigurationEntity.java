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
    @Id
    @Column(name = "Id", nullable = false)
    private String id;
    @Basic
    @Column(name = "Value", nullable = false, length = 50)
    private int value;
}
