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
    @Column(name = "ComboId", nullable = false)
    private int comboId;
    @Basic
    @Column(name = "Total", nullable = false)
    private int total;
    @Basic
    @Column(name = "PaymentType", nullable = false, length = 20)
    private String paymentType;
    @Basic
    @Column(name = "DateTime", nullable = false)
    private Timestamp dateTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AccountId", referencedColumnName = "Id", insertable = false, updatable = false)
    private AccountEntity accountByAccountId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ComboId", referencedColumnName = "Id", insertable = false, updatable = false)
    private ComboEntity comboByComboId;
}
