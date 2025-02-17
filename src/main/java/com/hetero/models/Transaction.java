package com.hetero.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.Date;


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

    public Transaction () {
    }

    public Transaction (Long id, Long aggregatedTransactionId, TransactionStatus status, BigDecimal cashBack, boolean isDeleted, Integer userId, Long transactionTakenTime, SubscriptionPlan subscriptionPlan, BigDecimal amount, Platform platformType, PaymentMethod paymentMethod, Date dateCreated, Date deletedAt, String transactionReference) {
        this.id = id;
        this.aggregatedTransactionId = aggregatedTransactionId;
        this.status = status;
        this.cashBack = cashBack;
        this.isDeleted = isDeleted;
        this.userId = userId;
        this.transactionTakenTime = transactionTakenTime;
        this.subscriptionPlan = subscriptionPlan;
        this.amount = amount;
        this.platformType = platformType;
        this.paymentMethod = paymentMethod;
        this.dateCreated = dateCreated;
        this.deletedAt = deletedAt;
        this.transactionReference = transactionReference;
    }

    public Long getId () {
        return id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public Long getAggregatedTransactionId () {
        return aggregatedTransactionId;
    }

    public void setAggregatedTransactionId (Long aggregatedTransactionId) {
        this.aggregatedTransactionId = aggregatedTransactionId;
    }

    public TransactionStatus getStatus () {
        return status;
    }

    public void setStatus (TransactionStatus status) {
        this.status = status;
    }

    public BigDecimal getCashBack () {
        return cashBack;
    }

    public void setCashBack (BigDecimal cashBack) {
        this.cashBack = cashBack;
    }

    public boolean isDeleted () {
        return isDeleted;
    }

    public void setDeleted (boolean deleted) {
        isDeleted = deleted;
    }

    public Integer getUserId () {
        return userId;
    }

    public void setUserId (Integer userId) {
        this.userId = userId;
    }

    public Long getTransactionTakenTime () {
        return transactionTakenTime;
    }

    public void setTransactionTakenTime (Long transactionTakenTime) {
        this.transactionTakenTime = transactionTakenTime;
    }

    public SubscriptionPlan getSubscriptionPlan () {
        return subscriptionPlan;
    }

    public void setSubscriptionPlan (SubscriptionPlan subscriptionPlan) {
        this.subscriptionPlan = subscriptionPlan;
    }

    public @NotNull BigDecimal getAmount () {
        return amount;
    }

    public void setAmount (@NotNull BigDecimal amount) {
        this.amount = amount;
    }

    public Platform getPlatformType () {
        return platformType;
    }

    public void setPlatformType (Platform platformType) {
        this.platformType = platformType;
    }

    public PaymentMethod getPaymentMethod () {
        return paymentMethod;
    }

    public void setPaymentMethod (PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Date getDateCreated () {
        return dateCreated;
    }

    public void setDateCreated (Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDeletedAt () {
        return deletedAt;
    }

    public void setDeletedAt (Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getTransactionReference () {
        return transactionReference;
    }

    public void setTransactionReference (String transactionReference) {
        this.transactionReference = transactionReference;
    }
}
