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
@Table(name = "CommonComment", schema = "dbo", catalog = "ToiletMap_Final_Final")
public class CommonCommentEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "Id", nullable = false)
    private int id;

    @Basic
    @Column(name = "Name", nullable = false, length = 100)
    private String name;

    @Basic
    @Column(name = "Status", nullable = false, length = 20)
    private String status;
    @OneToMany(mappedBy = "commonCommentByCommonCommentId")
    private Collection<RatingCommonCommentEntity> ratingCommonCommentById;
}
