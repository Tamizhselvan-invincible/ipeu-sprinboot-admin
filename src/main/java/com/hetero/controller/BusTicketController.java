package com.hetero.controller;


import com.hetero.models.City;
import com.hetero.service.BusTicketBookingService;
import com.hetero.service.CityService;
import com.hetero.utils.OkHttpClientProvider;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/bus-booking/cities")
public class BusTicketController {

    @Autowired
    private CityService cityService;

    @Autowired
    private BusTicketBookingService busTicketBookingService;



    @PostMapping("/save")
    public String saveCities() throws IOException {
        String jsonResponse = busTicketBookingService.getSourceCities();
        cityService.saveCitiesFromJson(jsonResponse);
        return "Cities saved successfully!";
    }

    @GetMapping()
    public ResponseEntity<List<City>> getCities() {
        return ResponseEntity.ok(cityService.getCities());
    }



}
