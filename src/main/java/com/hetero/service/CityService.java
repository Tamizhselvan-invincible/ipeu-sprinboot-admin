package com.hetero.service;

import com.hetero.models.City;

import java.util.List;

public interface CityService {

    void saveCitiesFromJson(String jsonResponse);

    List<City> getCities();
}
