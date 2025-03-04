package com.hetero.models.bus;

import jakarta.persistence.*;

@Entity
@Table(name = "cities")
public class City {
    
    @Id
    private String id;
    
    private String name;
    private String latitude;
    private String longitude;
    private String locationType;
    private String state;
    private String stateId;

    public City () {
    }

    public City (String id, String name, String latitude, String longitude, String locationType, String state, String stateId) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationType = locationType;
        this.state = state;
        this.stateId = stateId;
    }

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getLatitude () {
        return latitude;
    }

    public void setLatitude (String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude () {
        return longitude;
    }

    public void setLongitude (String longitude) {
        this.longitude = longitude;
    }

    public String getLocationType () {
        return locationType;
    }

    public void setLocationType (String locationType) {
        this.locationType = locationType;
    }

    public String getState () {
        return state;
    }

    public void setState (String state) {
        this.state = state;
    }

    public String getStateId () {
        return stateId;
    }

    public void setStateId (String stateId) {
        this.stateId = stateId;
    }
}
