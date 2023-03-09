package com.happy3friends.toiletmapbackend.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@EqualsAndHashCode
@Setter
@Getter
@Entity
@Table(name = "CheckIn", schema = "dbo", catalog = "ToiletMap")
public class CheckInEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "Id", nullable = false)
    private int id;
    @Basic
    @Column(name = "AccountId", nullable = false)
    private int accountId;
    @Basic
    @Column(name = "ToiletServiceId", nullable = false)
    private int toiletServiceId;
    @Basic
    @Column(name = "DateTime", nullable = false)
    private Date dateTime;
    @Basic
    @Column(name = "PaymentType", nullable = false, length = 20)
    private String paymentType;
    @Basic
    @Column(name = "Balance", nullable = true, precision = 0)
    private Double balance;
    @Basic
    @Column(name = "Turn", nullable = true)
    private Integer turn;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AccountId", referencedColumnName = "Id", insertable = false, updatable = false)
    private AccountEntity accountByAccountId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ToiletServiceId", referencedColumnName = "Id", insertable = false, updatable = false)
    private ToiletServiceEntity toiletServiceByToiletServiceId;
}
