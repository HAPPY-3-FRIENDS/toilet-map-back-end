package com.happy3friends.toiletmapbackend.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "Report", schema = "dbo", catalog = "ToiletMap")
public class ReportEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "Id", nullable = false)
    private int id;
    @Basic
    @Column(name = "ToiletId", nullable = false)
    private int toiletId;
    @Basic
    @Column(name = "Message", nullable = false, length = 100)
    private String message;
    @Basic
    @Column(name = "Status", nullable = true, length = 20)
    private String status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ToiletId", referencedColumnName = "Id", insertable = false, updatable = false)
    private ToiletEntity toiletByToiletId;
}
