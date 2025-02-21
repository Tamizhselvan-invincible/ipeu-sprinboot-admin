package com.hetero.service;
/*
 This is a Integration Pay Sprint API Service.

 All of the Services are used the Pay Sprint Bus Ticket Booking Raw APIs

 For Reference Use this URL:
 https://pay-sprint.readme.io/reference/get-source-city

 For Documentation
 https://pay-sprint.readme.io

 */


import com.hetero.security.PaySprintJWTGenerator;
import com.hetero.utils.OkHttpClientProvider;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


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






}
