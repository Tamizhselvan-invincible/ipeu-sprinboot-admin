package com.hetero.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hetero.models.City;
import com.hetero.models.bus.BlockTicketRequest;
import com.hetero.models.bus.TripRequest;
import com.hetero.service.BusTicketBookingService;
import com.hetero.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;


@RestController
@RequestMapping("/bus")
public class BusTicketController {

    @Autowired
    private CityService cityService;
    @Autowired
    private BusTicketBookingService busTicketBookingService;


    @GetMapping("/cities")
    public ResponseEntity<List<City>> getCitiesFromService() {
        return ResponseEntity.ok(cityService.getCities());
    }

    @PostMapping("/cities/save")
    public ResponseEntity<?> saveCities() {
        String response = busTicketBookingService.getSourceCities();
        return handleApiResponse(response, () -> {
            cityService.saveCitiesFromJson(response);
            return ResponseEntity.ok("Cities saved successfully!");
        });
    }

    @PostMapping("/availabletrips")
    public ResponseEntity<?> getAvailableTrips(@RequestBody TripRequest tripRequest) {
        String response = busTicketBookingService.getAvailableTrips(
                tripRequest.getSourceId(), tripRequest.getDestinationId(),
                new SimpleDateFormat("yyyy-MM-dd").format(tripRequest.getDateOfJourney()));
        return handleApiResponse(response);
    }

    @PostMapping("/tripdetails")
    public ResponseEntity<?> getTripDetailsFromService(@RequestParam Long tripId) {
        String response = busTicketBookingService.getCurrentTripDetails(tripId);
        return handleApiResponse(response);
    }

    @PostMapping("/boardingDetails")
    public ResponseEntity<?> getBoardingDetailsFromService(@RequestParam Long boardingId, @RequestParam Long tripId) {
        String response = busTicketBookingService.getBoardingPointDetails(boardingId, tripId);
        return handleApiResponse(response);
    }

    @PostMapping("/block-ticket")
    public ResponseEntity<?> blockTicketFromService(@RequestBody BlockTicketRequest request) throws JsonProcessingException {
        String response = busTicketBookingService.blockTicket(request);
        return handleApiResponse(response);
    }

    @PostMapping("/book-ticket")
    public ResponseEntity<?> bookTicketFromService(@RequestParam Long refId, @RequestParam Long amount) {
        String response = busTicketBookingService.bookTicket(refId, amount);
        return handleApiResponse(response);
    }
    @PostMapping("/check-booked-ticket")
    public ResponseEntity<?> checkBookedTicketFromService(@RequestParam Long refId) {
        String response = busTicketBookingService.checkBookedTicket(refId);
        return handleApiResponse(response);
    }
    @PostMapping("/get-booked-ticket")
    public ResponseEntity<?> getBookedTicketFromService(@RequestParam Long refId) {
        String response = busTicketBookingService.getBookedTicket(refId);
        return handleApiResponse(response);
    }

    @PostMapping("/cancellation-ticket-data")
    public ResponseEntity<?> getCancellationTicketDataFromService(
            @RequestParam Long refId,
            @RequestBody Map<Integer, String> seatsToCancel) {

        String response = busTicketBookingService.getCancellationTicketData(refId);
        return handleApiResponse(response);
    }

    @PostMapping("/cancel-ticket")
    public ResponseEntity<?> cancelTicketFromService(
            @RequestParam Long refId,
            @RequestBody Map<Integer, String> seatsToCancel) {

        String response = busTicketBookingService.cancelTicket(refId, seatsToCancel);
        return handleApiResponse(response);
    }




    private ResponseEntity<?> handleApiResponse(String response) {
        return handleApiResponse(response, () -> ResponseEntity.ok(response));
    }

    private ResponseEntity<?> handleApiResponse(String response, Supplier<ResponseEntity<?>> successHandler) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);
            if (jsonNode.has("error_code")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(jsonNode.toString());
            }
            return successHandler.get();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error_code\": 500, \"error_message\": \"Invalid JSON response\"}");
        }
    }
}

//@RestController
//@RequestMapping("/bus")
//public class BusTicketController {
//
//    @Autowired
//    private CityService cityService;
//
//    @Autowired
//    private BusTicketBookingService busTicketBookingService;
//
//
//
//    @PostMapping("/cities/save")
//    public String saveCities() throws IOException {
//        String jsonResponse = busTicketBookingService.getSourceCities();
//        cityService.saveCitiesFromJson(jsonResponse);
//        return "Cities saved successfully!";
//    }
//
//    @GetMapping("/cities")
//    public ResponseEntity<List<City>> getCities() {
//        return ResponseEntity.ok(cityService.getCities());
//    }
//
//    @PostMapping("/availabletrips") // Define the POST mapping
//    public ResponseEntity<String> getAvailableTripsFromPaySpringAPI(
//            @RequestBody TripRequest tripRequest ) throws IOException, ParseException {
//
//        int sourceId = tripRequest.getSourceId();
//
//        int destinationId = tripRequest.getDestinationId();
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String formattedDate = dateFormat.format(tripRequest.getDateOfJourney());
//
//        return ResponseEntity.ok(busTicketBookingService.getAvailableTrips(sourceId, destinationId, formattedDate));
//
//    }
//
//
//    @PostMapping("/tripdetails")
//    public ResponseEntity<String> getTripDetails(@RequestParam String tripId) throws IOException {
//
//        Long tripID = Long.parseLong(tripId);
//        return ResponseEntity.ok(busTicketBookingService.getCurrentTripDetails(tripID));
//    }
//
//
//    @PostMapping("/boardingDetails")
//    public ResponseEntity<String> getBoardingDetails(
//            @RequestParam String boardingId,
//            @RequestParam String tripId
//       ) throws IOException {
//
//        Long boardingID = Long.parseLong(boardingId);
//        Long tripID = Long.parseLong(tripId);
//
//        return ResponseEntity.ok(busTicketBookingService.getBoardingPointDetails(boardingID, tripID));
//    }
//
//
//        @PostMapping("/block-ticket")
//        public ResponseEntity<?> blockTicketForUser(@RequestBody BlockTicketRequest request) throws IOException {
//            String response = busTicketBookingService.blockTicket(request);
//            try {
//                return ResponseEntity.ok(new ObjectMapper().readTree(response));
//            } catch (JsonProcessingException e) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid JSON response from service");
//            }
//        }
//
//
//        @PostMapping("/book-ticket")
//        public ResponseEntity<?> bookTicketForUser(@RequestParam Long refId,
//                                        @RequestParam Long amount) throws IOException {
//        return ResponseEntity.ok(busTicketBookingService.bookTicket(refId, amount));
//        }
//
//
//    @PostMapping("/cancel-ticket")
//    public ResponseEntity<?> cancelTicket(
//            @RequestParam Long refId,
//            @RequestBody Map<Integer, String> seatsToCancel) {
//
//        String response = busTicketBookingService.cancelTicket(refId, seatsToCancel);
////        return handleApiResponse(response);
//    }
//
//
//}
