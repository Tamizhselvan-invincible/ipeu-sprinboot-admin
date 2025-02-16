package com.hetero.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "aggregated_transaction_id")
    private Long aggregatedTransactionId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name = "cashback_amount")
    private BigDecimal cashBack;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "transaction_taken_time")
    private Long transactionTakenTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subscription_plan_id", nullable = false)
    @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
    @JsonBackReference
    private SubscriptionPlan subscriptionPlan;

    @Column(name = "transaction_amount")
    @NotNull
    private BigDecimal amount;

    @Column
    private Platform platformType;

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "created_at")
    @CreationTimestamp
    private Date dateCreated;

    @Column(name = "deleted_at")
    private Date deletedAt;

    @Column(name = "transaction_reference")
    private String transactionReference;
}
