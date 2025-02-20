package com.hetero.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hetero.models.City;
import com.hetero.repository.CityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    @Autowired
    CityDao cityDao;

    public void saveCitiesFromJson(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode citiesNode = rootNode.path("data").path("cities");

            List<City> cityList = new ArrayList<>();

            for (JsonNode cityNode : citiesNode) {
                City city = new City();
                city.setId(cityNode.get("id").asText());
                city.setName(cityNode.get("name").asText());
                city.setLatitude(cityNode.get("latitude").asText());
                city.setLongitude(cityNode.get("longitude").asText());
                city.setLocationType(cityNode.get("locationType").asText());
                city.setState(cityNode.get("state").asText());
                city.setStateId(cityNode.get("stateId").asText());

                cityList.add(city);
            }

            cityDao.saveAll(cityList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<City> getCities () {
        List<City> cities = cityDao.findAll();
        return cities;
    }
}
