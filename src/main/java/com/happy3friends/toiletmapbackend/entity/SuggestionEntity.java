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
@Table(name = "Suggestion", schema = "dbo", catalog = "ToiletMap")
public class SuggestionEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ToiletId", nullable = false)
    private int toiletId;
    @Basic
    @Column(name = "Message", nullable = true, length = -1)
    private String message;
    @Basic
    @Column(name = "IsAccepted", nullable = true)
    private Boolean isAccepted;
    @OneToOne
    @JoinColumn(name = "ToiletId", referencedColumnName = "Id", nullable = false)
    private ToiletEntity toiletByToiletId;

}
