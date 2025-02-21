package com.hetero.controller;


import com.hetero.models.City;
import com.hetero.models.bus.TripRequest;
import com.hetero.service.BusTicketBookingService;
import com.hetero.service.CityService;
import org.apache.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;


@RestController
@RequestMapping("/bus-booking")
public class BusTicketController {

    @Autowired
    private CityService cityService;

    @Autowired
    private BusTicketBookingService busTicketBookingService;



    @PostMapping("/cities/save")
    public String saveCities() throws IOException {
        String jsonResponse = busTicketBookingService.getSourceCities();
        cityService.saveCitiesFromJson(jsonResponse);
        return "Cities saved successfully!";
    }

    @GetMapping("/cities")
    public ResponseEntity<List<City>> getCities() {
        return ResponseEntity.ok(cityService.getCities());
    }

    @PostMapping("/available-trips") // Define the POST mapping
    public ResponseEntity<String> getAvailableTripsFromPaySpringAPI(
            @RequestBody TripRequest tripRequest ) throws IOException, ParseException {

        int sourceId = tripRequest.getSourceId();

        int destinationId = tripRequest.getDestinationId();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(tripRequest.getDateOfJourney());

        return ResponseEntity.ok(busTicketBookingService.getAvailableTrips(sourceId, destinationId, formattedDate));

    }






}
