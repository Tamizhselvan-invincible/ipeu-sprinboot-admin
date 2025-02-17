package com.hetero.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "settings")
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "banner_name")
    private String bannerName;

    @Column(name = "app_name")
    private String appName;

    @Column(name = "app_logo")
    private String appLogo;

    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updatedAt;

    public Settings () {
    }

    public Settings (Long id, String bannerName, String appName, String appLogo, Date createdAt, Date updatedAt) {
        this.id = id;
        this.bannerName = bannerName;
        this.appName = appName;
        this.appLogo = appLogo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId () {
        return id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public String getBannerName () {
        return bannerName;
    }

    public void setBannerName (String bannerName) {
        this.bannerName = bannerName;
    }

    public String getAppName () {
        return appName;
    }

    public void setAppName (String appName) {
        this.appName = appName;
    }

    public String getAppLogo () {
        return appLogo;
    }

    public void setAppLogo (String appLogo) {
        this.appLogo = appLogo;
    }

    public Date getCreatedAt () {
        return createdAt;
    }

    public void setCreatedAt (Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt () {
        return updatedAt;
    }

    public void setUpdatedAt (Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}