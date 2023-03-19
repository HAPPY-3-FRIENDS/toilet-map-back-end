package com.happy3friends.toiletmapbackend.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "Rating", schema = "dbo", catalog = "ToiletMap")
public class RatingEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "Id", nullable = false)
    private int id;
    @Basic
    @Column(name = "Comment", nullable = false, length = -1)
    private String comment;
    @Basic
    @Column(name = "AccountId", nullable = false)
    private int accountId;
    @Basic
    @Column(name = "ToiletId", nullable = false)
    private int toiletId;
    @Basic
    @Column(name = "CreatedDate", nullable = false)
    private Timestamp createdDate;
    @Basic
    @Column(name = "Status", nullable = true, length = 20)
    private String status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AccountId", referencedColumnName = "Id", insertable = false, updatable = false)
    private AccountEntity accountByAccountId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ToiletId", referencedColumnName = "Id", insertable = false, updatable = false)
    private ToiletEntity toiletByToiletId;
    @OneToMany(mappedBy = "ratingByRatingId")
    private Collection<RatingImageEntity> ratingImagesById;
}
