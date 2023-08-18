package com.happy3friends.toiletmapbackend.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "Announcement", schema = "dbo", catalog = "ToiletMap_DEMO")
public class AnnouncementEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "Id", nullable = false)
    private int id;

    @Basic
    @Column(name = "Title", nullable = false)
    private String title;

    @Basic
    @Column(name = "Url", nullable = false, length = -1)
    private String url;

    @Basic
    @Column(name = "ImageSource", nullable = false, length = -1)
    private String imageSource;

    @Basic
    @Column(name = "StartDate", nullable = false)
    private Date startDate;

    @Basic
    @Column(name = "EndDate", nullable = false)
    private Date endDate;

    @Basic
    @Column(name = "Description", nullable = false, length = -1)
    private String description;

    @Basic
    @Column(name = "Type", nullable = false, length = 20)
    private String type;
}
