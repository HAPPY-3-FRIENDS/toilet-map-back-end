package com.happy3friends.toiletmapbackend.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
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
    private Timestamp dateTime;
    @Basic
    @Column(name = "PaymentMethod", nullable = false, length = 20)
    private String paymentMethod;
    @Basic
    @Column(name = "Balance", nullable = true)
    private Integer balance;
    @Basic
    @Column(name = "Turn", nullable = true)
    private Integer turn;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AccountId", referencedColumnName = "Id", insertable = false, updatable = false)
    private AccountEntity accountByAccountId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ToiletServiceId", referencedColumnName = "Id", insertable = false, updatable = false)
    private ToiletServiceEntity toiletServiceByToiletServiceId;
    @Basic
    @Column(name = "TurnPrice", nullable = true)
    private Integer turnPrice;
}
