package com.hetero.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "transaction_stats")
public class Ledger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "total_failed_amount", precision = 10, scale = 2)
    private BigDecimal totalFailedAmount;

    @Column(name = "total_transactions")
    private Integer totalTransactions;

    @Column(name = "failed_transactions")
    private Integer failedTransactions;

    @Column(name = "total_users")
    private Integer noOfUsers;

    @Column(name = "blocked_users")
    private Integer noOfBlockedUsers;

    @Column(name = "stats_date")
    @Temporal(TemporalType.DATE)
    private Date statsDate;
}