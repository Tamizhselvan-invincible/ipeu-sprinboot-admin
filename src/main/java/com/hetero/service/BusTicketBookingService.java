package com.hetero.service;

/*
 This is a Integration Pay Sprint API Service.

 All of the Services are used the Pay Sprint Bus Ticket Booking Raw APIs

 For Reference Use this URL:
 https://pay-sprint.readme.io/reference/get-source-city

 For Documentation
 https://pay-sprint.readme.io

 */


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hetero.models.bus.BlockTicketRequest;
import com.hetero.repository.TokenDao;
import com.hetero.security.PaySprintJWTGenerator;
import com.hetero.utils.OkHttpClientProvider;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class BusTicketBookingService {


    private static final Logger log = LogManager.getLogger(BusTicketBookingService.class);
    @Autowired
    private PaySprintJWTGenerator paySprintJWTGenerator;

    @Value("${application.pay-sprint.base-url}")
    private String baseUrl;

    @Value("${application.pay-sprint.authorized-key}")
    private String authorizedKey;

    @Autowired
    private OkHttpClientProvider okHttpClientProvider;
    @Autowired
    private TokenDao tokenDao;

    public String getSourceCities() throws IOException {

        String Token = paySprintJWTGenerator.getToken();

        OkHttpClient client = okHttpClientProvider.getClient();

        RequestBody emptyBody = RequestBody.create("", MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(baseUrl+"/service-api/api/v1/service/bus/ticket/source")
                .post(emptyBody)
                .addHeader("accept", "application/json")
                .addHeader("Token", Token)
                .addHeader("Authorisedkey", authorizedKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code: " + response);
            }
            return response.body().string();
        } catch (Exception e) {
            log.error("Error fetching cities: ", e);
            return "{}"; // Return empty JSON to prevent null errors
        }

    }

    public String getAvailableTrips(int sourceId, int destinationId, String date) throws IOException {

        String Token = paySprintJWTGenerator.getToken();
        OkHttpClient client = okHttpClientProvider.getClient();

        MediaType mediaType = MediaType.parse("application/json");

        String jsonBody = String.format("{\"source_id\":%d,\"destination_id\":%d,\"date_of_journey\":\"%s\"}",
                sourceId, destinationId, date);

        RequestBody requestBody = RequestBody.create(mediaType, jsonBody);

        Request request = new Request.Builder()
                .url(baseUrl+"/service-api/api/v1/service/bus/ticket/availabletrips")
                .post(requestBody)
                .addHeader("accept", "application/json")
                .addHeader("Token", Token)
                .addHeader("Authorisedkey", authorizedKey)
                .addHeader("content-type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("Unexpected code: " + response);
                throw new Exception("Unexpected code: " + response);
            }
            return response.body().string();
        } catch (Exception e) {
            log.error("Error fetching available Trips: ", e);
            return e.getMessage(); // Return empty JSON to prevent null errors
        }
    }


    public String getCurrentTripDetails(Long tripId) throws IOException {

        String Token = paySprintJWTGenerator.getToken();
        OkHttpClient client = okHttpClientProvider.getClient();

        MediaType mediaType = MediaType.parse("application/json");

        String jsonBody = String.format("{\"trip_id\":%d}", tripId);
        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url(baseUrl+"/service-api/api/v1/service/bus/ticket/tripdetails")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("Token", Token)
                .addHeader("Authorisedkey", authorizedKey)
                .addHeader("content-type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            log.error("Unexpected code: " + response);
            return null;
        }else {
            return response.body().string();
        }
    }


    public String getBoardingPointDetails(long bpId, long tripId) throws IOException {
        String Token = paySprintJWTGenerator.getToken();
        OkHttpClient client = okHttpClientProvider.getClient();
        MediaType mediaType = MediaType.parse("application/json");

        String jsonBody = String.format("{\"bpId\":%d,\"trip_id\":%d}",bpId, tripId);
        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url(baseUrl+"/service-api/api/v1/service/bus/ticket/boardingPoint")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("Token", Token)
                .addHeader("Authorisedkey", authorizedKey)
                .addHeader("content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            log.error("Unexpected code: " + response);
            return null;
        }else {
            return response.body().string();
        }
    }

    public String blockTicket(BlockTicketRequest blockTicketRequest) throws IOException {
        String Token = paySprintJWTGenerator.getToken();
        OkHttpClient client = okHttpClientProvider.getClient();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(blockTicketRequest);

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonRequest);
//        RequestBody body = RequestBody.create(mediaType, "{\"availableTripId\":3,\"boardingPointId\":6,\"inventoryItems\":{\"0\":{\"seatName\":\"A15\",\"fare\":102.3,\"serviceTax\":4.6,\"operatorServiceCharge\":4.6,\"ladiesSeat\":\"false\",\"passenger\":{\"name\":\"Passenger name\",\"mobile\":9999988888,\"title\":\"Mr\",\"email\":\"xyz@gmail.com\",\"age\":25,\"gender\":\"MALE\",\"address\":\"Dummy Address\",\"idType\":\"Pancard\",\"idNumber\":\"QWERT1234Y\",\"primary\":\"1\"}}}}");
        Request request = new Request.Builder()
                .url(baseUrl+"/service-api/api/v1/service/bus/ticket/blockticket")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("Token", Token)
                .addHeader("Authorisedkey", authorizedKey)
                .addHeader("content-type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            log.error("Unexpected code: " + response);
            return null;
        }  else {
            return response.body().string();
        }
    }

    public String bookTicket(Long refId, Long amount) throws IOException {
        String Token = paySprintJWTGenerator.getToken();
        OkHttpClient client = okHttpClientProvider.getClient();

        MediaType mediaType = MediaType.parse("application/json");
        String jsonBody = String.format("{\"ref_id\":%d,\"amount\":%d}", refId, amount);

        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url(baseUrl+"/service-api/api/v1/service/bus/ticket/bookticket")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("Token", Token)
                .addHeader("Authorisedkey", authorizedKey)
                .addHeader("content-type", "application/json")
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            log.error("Unexpected code: " + response);
            return null;
        } else {
            return response.body().string();
        }
    }

    public String checkBookedTicket(Long refId) throws IOException {
        String Token = paySprintJWTGenerator.getToken();
        OkHttpClient client = okHttpClientProvider.getClient();

        String jsonBody = String.format("{\"ref_id\":%d}", refId);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url(baseUrl+"/service-api/api/v1/service/bus/ticket/check_booked_ticket")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("Token", Token)
                .addHeader("Authorisedkey", authorizedKey)
                .addHeader("content-type", "application/json")
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            log.error("Unexpected code: " + response);
            return null;
        } else {
            return response.body().string();
        }
    }

}
