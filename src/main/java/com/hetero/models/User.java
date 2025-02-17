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


@Entity
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

    public User () {
    }

    public User (Integer id, String firstName, String lastName, String email, String mobileNo, String mPin, String profilePicture, boolean isBlocked, AccountStatus accountStatus, Date dateCreated, Date dateUpdated, Platform platformType, Date deletedAt, String appVersion, LocalDateTime lastLoginTime, Role userRole, Date appUpdatedAt, String deviceBrandName, String deviceVersionCode, String osType, List<Transaction> transactions) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNo = mobileNo;
        this.mPin = mPin;
        this.profilePicture = profilePicture;
        this.isBlocked = isBlocked;
        this.accountStatus = accountStatus;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.platformType = platformType;
        this.deletedAt = deletedAt;
        this.appVersion = appVersion;
        this.lastLoginTime = lastLoginTime;
        this.userRole = userRole;
        this.appUpdatedAt = appUpdatedAt;
        this.deviceBrandName = deviceBrandName;
        this.deviceVersionCode = deviceVersionCode;
        this.osType = osType;
        this.transactions = transactions;
    }

    public Integer getId () {
        return id;
    }

    public void setId (Integer id) {
        this.id = id;
    }

    public @NotNull(message = "First Name cannot be NULL") @Pattern(regexp = "[A-Za-z.\\s]+", message = "Enter valid characters in first name") String getFirstName () {
        return firstName;
    }

    public void setFirstName (@NotNull(message = "First Name cannot be NULL") @Pattern(regexp = "[A-Za-z.\\s]+", message = "Enter valid characters in first name") String firstName) {
        this.firstName = firstName;
    }

    public @NotNull(message = "Last Name cannot be NULL") @Pattern(regexp = "[A-Za-z.\\s]+", message = "Enter valid characters in last name") String getLastName () {
        return lastName;
    }

    public void setLastName (@NotNull(message = "Last Name cannot be NULL") @Pattern(regexp = "[A-Za-z.\\s]+", message = "Enter valid characters in last name") String lastName) {
        this.lastName = lastName;
    }

    public @Email(message = "Please provide a valid email address") String getEmail () {
        return email;
    }

    public void setEmail (@Email(message = "Please provide a valid email address") String email) {
        this.email = email;
    }

    public @NotNull(message = "Mobile number cannot be NULL") @Pattern(regexp = "[6789]{1}[0-9]{9}", message = "Enter valid 10 digit mobile number") String getMobileNo () {
        return mobileNo;
    }

    public void setMobileNo (@NotNull(message = "Mobile number cannot be NULL") @Pattern(regexp = "[6789]{1}[0-9]{9}", message = "Enter valid 10 digit mobile number") String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public @NotNull(message = "M-PIN cannot be null") @Size(min = 4, max = 6, message = "M-PIN must be between 4 and 6 digits") String getmPin () {
        return mPin;
    }

    public void setmPin (@NotNull(message = "M-PIN cannot be null") @Size(min = 4, max = 6, message = "M-PIN must be between 4 and 6 digits") String mPin) {
        this.mPin = mPin;
    }

    public String getProfilePicture () {
        return profilePicture;
    }

    public void setProfilePicture (String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public boolean isBlocked () {
        return isBlocked;
    }

    public void setBlocked (boolean blocked) {
        isBlocked = blocked;
    }

    public AccountStatus getAccountStatus () {
        return accountStatus;
    }

    public void setAccountStatus (AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Date getDateCreated () {
        return dateCreated;
    }

    public void setDateCreated (Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateUpdated () {
        return dateUpdated;
    }

    public void setDateUpdated (Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Platform getPlatformType () {
        return platformType;
    }

    public void setPlatformType (Platform platformType) {
        this.platformType = platformType;
    }

    public Date getDeletedAt () {
        return deletedAt;
    }

    public void setDeletedAt (Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getAppVersion () {
        return appVersion;
    }

    public void setAppVersion (String appVersion) {
        this.appVersion = appVersion;
    }

    public LocalDateTime getLastLoginTime () {
        return lastLoginTime;
    }

    public void setLastLoginTime (LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Role getUserRole () {
        return userRole;
    }

    public void setUserRole (Role userRole) {
        this.userRole = userRole;
    }

    public Date getAppUpdatedAt () {
        return appUpdatedAt;
    }

    public void setAppUpdatedAt (Date appUpdatedAt) {
        this.appUpdatedAt = appUpdatedAt;
    }

    public String getDeviceBrandName () {
        return deviceBrandName;
    }

    public void setDeviceBrandName (String deviceBrandName) {
        this.deviceBrandName = deviceBrandName;
    }

    public String getDeviceVersionCode () {
        return deviceVersionCode;
    }

    public void setDeviceVersionCode (String deviceVersionCode) {
        this.deviceVersionCode = deviceVersionCode;
    }

    public String getOsType () {
        return osType;
    }

    public void setOsType (String osType) {
        this.osType = osType;
    }

    public List<Transaction> getTransactions () {
        return transactions;
    }

    public void setTransactions (List<Transaction> transactions) {
        this.transactions = transactions;
    }
}