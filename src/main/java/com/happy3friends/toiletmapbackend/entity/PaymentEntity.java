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
@Table(name = "Payment", schema = "dbo", catalog = "ToiletMap_DEMO")
public class PaymentEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "Id", nullable = false)
    private int id;
    @Basic
    @Column(name = "AccountId", nullable = false)
    private int accountId;
    @Basic
    @Column(name = "Total", nullable = false)
    private int total;
    @Basic
    @Column(name = "Method", nullable = false, length = 100)
    private String method;
    @Basic
    @Column(name = "CreatedDate", nullable = false)
    private Timestamp createdDate;
    @Basic
    @Column(name = "Status", nullable = true, length = 20)
    private String status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AccountId", referencedColumnName = "Id", insertable = false, updatable = false)
    private AccountEntity accountByAccountId;
    @OneToOne(mappedBy = "paymentByPaymentId")
    private TransactionEntity transactionsById;
}
