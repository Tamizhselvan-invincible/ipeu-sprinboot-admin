package com.hetero.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hetero.security.AESEncryptor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;


@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(name = "aggregated_transaction_id")
    @Convert(converter = AESEncryptor.class)
    private Long aggregatedTransactionId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @Convert(converter = AESEncryptor.class)
    private TransactionStatus status;

    @Column(name = "cashback_amount")
    @Convert(converter = AESEncryptor.class)
    private String cashBack;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name = "user_id", nullable = false)

    private Integer userId;

    @Column(name = "transaction_taken_time")
    @Convert(converter = AESEncryptor.class)
    private Long transactionTakenTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subscription_plan_id", nullable = false)
    @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
    @JsonBackReference
    private SubscriptionPlan subscriptionPlan;

    @Column(name = "transaction_amount")
    @NotNull
    @Convert(converter = AESEncryptor.class)
    private String amount;

    @Column
    @Enumerated(EnumType.STRING)
    private Platform platformType;

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    @Convert(converter = AESEncryptor.class)
    private PaymentMethod paymentMethod;

    @Column(name = "created_at")
    @CreationTimestamp
    @Convert(converter = AESEncryptor.class)
    private Date dateCreated;

    @Column(name = "deleted_at")
    private Date deletedAt;

    @Column(name = "transaction_reference")
    @Convert(converter = AESEncryptor.class)
    private String transactionReference;


    public Transaction () {
    }

    public Transaction (Long id, Long aggregatedTransactionId, TransactionStatus status, String cashBack, boolean isDeleted, Integer userId, Long transactionTakenTime, SubscriptionPlan subscriptionPlan, String amount, Platform platformType, PaymentMethod paymentMethod, Date dateCreated, Date deletedAt, String transactionReference) {
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

    public String getCashBack () {
        return cashBack;
    }

    public void setCashBack (String cashBack) {
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

    public @NotNull String getAmount () {
        return amount;
    }

    public void setAmount (@NotNull String amount) {
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
