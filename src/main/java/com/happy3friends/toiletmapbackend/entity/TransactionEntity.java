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
@Table(name = "Transaction", schema = "dbo", catalog = "ToiletMap_Final_Final")
public class TransactionEntity {
    @Id
    @Column(name = "TransactionNo", nullable = false)
    private int transactionNo;
    @Basic
    @Column(name = "PaymentId", nullable = false)
    private int paymentId;
    @Basic
    @Column(name = "BankCode", nullable = false, length = 20)
    private String bankCode;
    @Basic
    @Column(name = "BankTranNo", nullable = false, length = 20)
    private String bankTranNo;
    @Basic
    @Column(name = "CardType", nullable = false, length = 20)
    private String cardType;
    @Basic
    @Column(name = "PayDate", nullable = false)
    private Timestamp payDate;
    @OneToOne
    @JoinColumn(name = "PaymentId", referencedColumnName = "Id", insertable = false, updatable = false)
    private PaymentEntity paymentByPaymentId;
}
