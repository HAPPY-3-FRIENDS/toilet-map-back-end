package com.happy3friends.toiletmapbackend.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "RatingImage", schema = "dbo", catalog = "ToiletMap_DEMO")
public class RatingImageEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "Id", nullable = false)
    private int id;
    @Basic
    @Column(name = "RatingId", nullable = false)
    private int ratingId;
    @Basic
    @Column(name = "ImageSource", nullable = false, length = -1)
    private String imageSource;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RatingId", referencedColumnName = "Id", insertable = false, updatable = false)
    private RatingEntity ratingByRatingId;
}
