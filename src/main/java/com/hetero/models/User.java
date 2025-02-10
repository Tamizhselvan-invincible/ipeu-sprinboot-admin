package com.hetero.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "First Name cannot be NULL")
    @Pattern(regexp = "[A-Za-z.\\s]+", message = "Enter valid characters in first name")
    @Column(name = "first_name", length = 50)
    private String firstName;

    @NotNull(message = "Last Name cannot be NULL")
    @Pattern(regexp = "[A-Za-z.\\s]+", message = "Enter valid characters in last name")
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Email(message = "Please provide a valid email address")
    @Column(unique = true)
    private String email;

    @NotNull(message = "Mobile number cannot be NULL")
    @Pattern(regexp = "[6789]{1}[0-9]{9}", message = "Enter valid 10 digit mobile number")
    @Column(name = "mobile_number",unique = true)
    private String mobileNo;

    @NotNull(message = "M-PIN cannot be null")
    @Column(name = "m_pin")
    @Size(min = 4, max = 6, message = "M-PIN must be between 4 and 6 digits")
    private String mPin;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "is_blocked")
    private boolean isBlocked;

    @Column(name = "account_status")
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus = AccountStatus.ACTIVE;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date dateCreated;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date dateUpdated;

    @Column
    private Platform platformType = Platform.ALL;

    @Column(name = "deleted_at",nullable = true)
    private Date deletedAt;

    @Column(name = "app_version")
    String appVersion;

    @Column(name = "last_login_time")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime lastLoginTime;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    Role userRole;

    @Column(name = "app_updated_at")
    private Date appUpdatedAt;

    @Column(name = "device_brand_name")
    private String deviceBrandName;

    @Column(name = "device_version_code")
    private String deviceVersionCode;

    @Column(name = "os_type")
    private String osType;

    @OneToMany(mappedBy = "userId")
    @JsonIgnore
    @JsonManagedReference
    private List<Transaction> transactions = new ArrayList<>();
}