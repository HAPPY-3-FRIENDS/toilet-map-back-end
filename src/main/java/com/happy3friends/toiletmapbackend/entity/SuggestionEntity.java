package com.happy3friends.toiletmapbackend.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;
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
    @Column(name = "Id", nullable = false)
    private int id;
    @Column(name = "ToiletId", nullable = false)
    private int toiletId;
    @Basic
    @Column(name = "Message", nullable = true, length = -1)
    private String message;
    @Basic
    @Column(name = "IsAccepted", nullable = true)
    private Boolean isAccepted;
    @Basic
    @Column(name = "StartDate", nullable = true)
    private Date startDate;
    @Basic
    @Column(name = "EndDate", nullable = true)
    private Date endDate;
    @Basic
    @Column(name = "ActualCount", nullable = true)
    private Integer actualCount;
    @Basic
    @Column(name = "ExpectedCount", nullable = true, precision = 0)
    private Double expectedCount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ToiletId", referencedColumnName = "Id", insertable = false, updatable = false)
    private ToiletEntity toiletByToiletId;
}
