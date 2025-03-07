package com.hetero.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hetero.models.bus.City;
import com.hetero.repository.CityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    @Autowired
    CityDao cityDao;

    public boolean saveCitiesFromJson(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode citiesNode = rootNode.path("data").path("data").path("cities");

            if (citiesNode.isMissingNode()) {
                System.out.println("Cities data not found!");
                return false;
            }
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

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<City> getCities () {
        List<City> cities = cityDao.findAll();
        return cities;
    }
}
