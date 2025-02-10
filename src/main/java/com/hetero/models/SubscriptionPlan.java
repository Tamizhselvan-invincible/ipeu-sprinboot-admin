package com.hetero.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Getter
@Setter
@Table(name = "subscription_plans")
public class SubscriptionPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Plan name is required")
    @Column(name = "plan_name")
    private String name;

    @NotBlank(message = "Operator name is required")
    private String operator;

    @NotNull(message = "Amount is required")
    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    @NotNull(message = "Validity is required")
    private Integer validity;

    @Column(name = "data_limit")
    private String dataLimit;

    @Column(name = "call_benefits")
    private String callBenefits;

    @Column(name = "sms_benefits")
    private String smsBenefits;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "created_at")
    @CreationTimestamp
    private Date dateCreated;

}
