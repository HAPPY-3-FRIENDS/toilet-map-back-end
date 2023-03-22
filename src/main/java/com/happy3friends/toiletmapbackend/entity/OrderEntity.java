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
@Table(name = "Order", schema = "dbo", catalog = "ToiletMap")
public class OrderEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "Id", nullable = false)
    private int id;
    @Basic
    @Column(name = "AccountId", nullable = false)
    private int accountId;
    @Basic
    @Column(name = "TotalTurn", nullable = false)
    private int totalTurn;
    @Basic
    @Column(name = "TotalPrice", nullable = false)
    private int totalPrice;
    @Basic
    @Column(name = "PaymentMethod", nullable = false, length = 20)
    private String paymentMethod;
    @Basic
    @Column(name = "DateTime", nullable = false)
    private Timestamp dateTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AccountId", referencedColumnName = "Id", insertable = false, updatable = false)
    private AccountEntity accountByAccountId;
}
