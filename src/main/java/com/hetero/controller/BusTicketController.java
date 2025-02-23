package com.hetero.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hetero.models.City;
import com.hetero.models.bus.BlockTicketRequest;
import com.hetero.models.bus.TripRequest;
import com.hetero.service.BusTicketBookingService;
import com.hetero.service.CityService;
import org.apache.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;


@RestController
@RequestMapping("/bus")
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

    @PostMapping("/availabletrips") // Define the POST mapping
    public ResponseEntity<String> getAvailableTripsFromPaySpringAPI(
            @RequestBody TripRequest tripRequest ) throws IOException, ParseException {

        int sourceId = tripRequest.getSourceId();

        int destinationId = tripRequest.getDestinationId();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(tripRequest.getDateOfJourney());

        return ResponseEntity.ok(busTicketBookingService.getAvailableTrips(sourceId, destinationId, formattedDate));

    }


    @PostMapping("/tripdetails")
    public ResponseEntity<String> getTripDetails(@RequestParam String tripId) throws IOException {

        Long tripID = Long.parseLong(tripId);
        return ResponseEntity.ok(busTicketBookingService.getCurrentTripDetails(tripID));
    }


    @PostMapping("/boardingDetails")
    public ResponseEntity<String> getBoardingDetails(
            @RequestParam String boardingId,
            @RequestParam String tripId
       ) throws IOException {

        Long boardingID = Long.parseLong(boardingId);
        Long tripID = Long.parseLong(tripId);

        return ResponseEntity.ok(busTicketBookingService.getBoardingPointDetails(boardingID, tripID));
    }


        @PostMapping("/block-ticket")
        public ResponseEntity<?> blockTicketForUser(@RequestBody BlockTicketRequest request) throws IOException {
            String response = busTicketBookingService.blockTicket(request);
            try {
                return ResponseEntity.ok(new ObjectMapper().readTree(response));
            } catch (JsonProcessingException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid JSON response from service");
            }
        }


        @PostMapping("/book-ticket")
        public ResponseEntity<?> bookTicketForUser(@RequestParam Long refId,
                                        @RequestParam Long amount) throws IOException {
        return ResponseEntity.ok(busTicketBookingService.bookTicket(refId, amount));
        }

}
