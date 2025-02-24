package com.hetero.service;

/**

 This is Integration Pay Sprint API Service.
 All the Services are used the Pay Sprint Bus Ticket Booking Raw APIs

 For Reference Use this URL:
 https://pay-sprint.readme.io/reference/get-source-city

 For Documentation
 https://pay-sprint.readme.io

 */



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hetero.models.bus.BlockTicketRequest;
import com.hetero.security.PaySprintJWTGenerator;
import com.hetero.utils.OkHttpClientProvider;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class BusTicketBookingService {

    private static final Logger log = LogManager.getLogger(BusTicketBookingService.class);

    @Autowired
    private PaySprintJWTGenerator paySprintJWTGenerator;
    @Autowired
    private OkHttpClientProvider okHttpClientProvider;

    @Value("${application.pay-sprint.base-url}")
    private String baseUrl;

    @Value("${application.pay-sprint.authorized-key}")
    private String authorizedKey;

    /**
     * Generic method to make API calls
     */
    private String makeApiCall(String endpoint, String jsonBody) {
        String token = paySprintJWTGenerator.getToken(); // Generate token on each call
        OkHttpClient client = okHttpClientProvider.getClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = jsonBody != null ? RequestBody.create(mediaType, jsonBody) : RequestBody.create("", mediaType);

        Request request = new Request.Builder()
                .url(baseUrl + endpoint)
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("Token", token)
                .addHeader("Authorisedkey", authorizedKey)
                .addHeader("content-type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("Error: {} - {}", response.code(), response.message());
                return String.format("{\"error_code\": %d, \"error_message\": \"%s\"}", response.code(), response.message());
            }
            return response.body().string();
        } catch (Exception e) {
            log.error("Exception in API call: ", e);
            return String.format("{\"error_code\": 500, \"error_message\": \"%s\"}", e.getMessage());
        }
    }

    /**
     * Get list of source cities
     */
    public String getSourceCities() {
        return makeApiCall("/service-api/api/v1/service/bus/ticket/source", null);
    }

    /**
     * Get available trips based on source, destination, and date
     */
    public String getAvailableTrips(int sourceId, int destinationId, String date) {
        String jsonBody = String.format("{\"source_id\":%d,\"destination_id\":%d,\"date_of_journey\":\"%s\"}",
                sourceId, destinationId, date);
        return makeApiCall("/service-api/api/v1/service/bus/ticket/availabletrips", jsonBody);
    }

    /**
     * Get trip details
     */
    public String getCurrentTripDetails(Long tripId) {
        String jsonBody = String.format("{\"trip_id\":%d}", tripId);
        return makeApiCall("/service-api/api/v1/service/bus/ticket/tripdetails", jsonBody);
    }

    /**
     * Get boarding point details
     */
    public String getBoardingPointDetails(long bpId, long tripId) {
        String jsonBody = String.format("{\"bpId\":%d,\"trip_id\":%d}", bpId, tripId);
        return makeApiCall("/service-api/api/v1/service/bus/ticket/boardingPoint", jsonBody);
    }

    /**
     * Block a ticket for a user
     */
    public String blockTicket(BlockTicketRequest blockTicketRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(blockTicketRequest);
        return makeApiCall("/service-api/api/v1/service/bus/ticket/blockticket", jsonRequest);
    }

    /**
     * Book a ticket using reference ID and amount
     */
    public String bookTicket(Long refId, Long amount) {
        String jsonBody = String.format("{\"ref_id\":%d,\"amount\":%d}", refId, amount);
        return makeApiCall("/service-api/api/v1/service/bus/ticket/bookticket", jsonBody);
    }

    /**
     * Check the status of a booked ticket
     */
    public String checkBookedTicket(Long refId) {
        String jsonBody = String.format("{\"ref_id\":%d}", refId);
        return makeApiCall("/service-api/api/v1/service/bus/ticket/check_booked_ticket", jsonBody);
    }

    /**
     * Get the status of a booked ticket
     */
    public String getBookedTicket(Long refId) {
        String jsonBody = String.format("{\"ref_id\":%d}", refId);
        return makeApiCall("service-api/api/v1/service/bus/ticket/get_ticket", jsonBody);
    }

    /**
     * Get the status of a Cancellation ticket Data
     */
    public String getCancellationTicketData(Long refId) {
        String jsonBody = String.format("{\"ref_id\":%d}", refId);
        return makeApiCall("service-api/api/v1/service/bus/ticket/get_cancellation_data", jsonBody);
    }

    /**
     * Cancel the Ticket
     */
    public String cancelTicket(Long refId, Map<Integer, String> seatsToCancel) {
        ObjectMapper objectMapper = new ObjectMapper();
        String seatJson;

        try {
            seatJson = objectMapper.writeValueAsString(seatsToCancel);
        } catch (JsonProcessingException e) {
            log.error("Error processing seat list JSON: ", e);
            return "{\"error_code\": 400, \"error_message\": \"Invalid seat format\"}";
        }

        // Construct the request JSON with Map format
        String jsonBody = String.format("{\"refid\":%d,\"seatsToCancel\":%s}", refId, seatJson);

        return makeApiCall("/service-api/api/v1/service/bus/ticket/cancel_ticket", jsonBody);
    }





}

