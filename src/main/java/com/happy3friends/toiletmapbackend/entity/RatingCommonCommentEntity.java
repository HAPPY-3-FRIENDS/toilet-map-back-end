package com.happy3friends.toiletmapbackend.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "RatingCommonComment", schema = "dbo", catalog = "ToiletMap_Final_Final")
public class RatingCommonCommentEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "Id", nullable = false)
    private int id;
    @Basic
    @Column(name = "RatingId", nullable = false)
    private int ratingId;
    @Basic
    @Column(name = "CommonCommentId", nullable = false)
    private int commonCommentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RatingId", referencedColumnName = "Id", insertable = false, updatable = false)
    private RatingEntity ratingByRatingId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CommonCommentId", referencedColumnName = "Id", insertable = false, updatable = false)
    private CommonCommentEntity commonCommentByCommonCommentId;
}
