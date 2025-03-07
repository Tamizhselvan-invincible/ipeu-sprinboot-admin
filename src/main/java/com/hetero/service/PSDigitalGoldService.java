package com.hetero.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hetero.models.digigold.DigitalGoldProfileCreateRequest;
import com.hetero.models.digigold.DigitalGoldProfileUpdateRequest;
import com.hetero.security.PaySprintJWTGenerator;
import com.hetero.utils.ApiErrorResponse;
import com.hetero.utils.ApiResponse;
import com.hetero.utils.OkHttpClientProvider;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PSDigitalGoldService {


    private static final Logger log = LogManager.getLogger(PSDigitalGoldService.class);

    @Autowired
    private PaySprintJWTGenerator paySprintJWTGenerator;
    @Autowired
    private OkHttpClientProvider okHttpClientProvider;

    @Value("${application.pay-sprint.base-url}")
    private String baseUrl;

    @Value("${application.pay-sprint.authorized-key}")
    private String authorizedKey;


    private final ObjectMapper objectMapper = new ObjectMapper();

    /*
     * Generic method to make API calls
     */
    private String makeApiCall(String endpoint, String jsonBody) {
        String token = paySprintJWTGenerator.getToken(); // Generate token on each call
        OkHttpClient client = okHttpClientProvider.getClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = jsonBody != null ? RequestBody.create(mediaType, jsonBody) : RequestBody.create("", mediaType);

        log.atInfo().log(body.toString());
        Request request = new Request.Builder()
                .url(baseUrl + endpoint)
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("Token", token)
                .addHeader("Authorisedkey", authorizedKey)
                .addHeader("content-type", "application/json")
                .addHeader("ENVIORMENT","UAT")
                .build();

        log.atInfo().log(request.toString());

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

    // * Get Profile Details
    public  String getProfile(long mobileNo){
        String jsonBody = String.format("{\"mobileNo\":%d}", mobileNo);
        return makeApiCall("/service-api/api/v1/service/digitalgold/customer/get_profile", jsonBody);
    }

    //  *  Create Profile for Digital Gold  Account Creation
    public ResponseEntity<?> createProfileForDigitalGold(DigitalGoldProfileCreateRequest requestDto) {
        try {
            // Convert DTO to JSON
            String jsonBody = objectMapper.writeValueAsString(requestDto);

            String token = paySprintJWTGenerator.getToken(); // Generate token on each call
            OkHttpClient client = okHttpClientProvider.getClient();

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = jsonBody != null ? RequestBody.create(mediaType, jsonBody) : RequestBody.create("", mediaType);

            log.atInfo().log(body.toString());
            Request request = new Request.Builder()
                    .url(baseUrl + "/service-api/api/v1/service/digitalgold/customer/create")
                    .post(body)
                    .addHeader("accept", "application/json")
                    .addHeader("Token", token)
                    .addHeader("Authorisedkey", authorizedKey)
                    .addHeader("content-type", "application/json")
                    .addHeader("ENVIORMENT","UAT")
                    .addHeader("environment","UAT")
                    .addHeader("ENVIRONMENT","UAT")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                int statusCode = response.code();
                String responseBody = response.body() != null ? response.body().string() : "";

                JsonNode jsonData;
                jsonData = objectMapper.readTree(responseBody);

                if (!response.isSuccessful()) {
                    log.error("Error: {} - {}", statusCode, response.message());
                    ApiResponse<JsonNode> errorResponse = new ApiResponse<>(statusCode, response.message(), jsonData);
                    return ResponseEntity.status(statusCode).body(errorResponse);
                }

                ApiResponse<JsonNode> successResponse = new ApiResponse<>(statusCode, "Success", jsonData);

                return ResponseEntity.status(statusCode).body(successResponse);
            } catch (Exception e) {
                log.error("Exception in API call: ", e);
                ApiErrorResponse<String> errorResponse = new ApiErrorResponse<>(500, "Internal Server Error", e.getMessage(),null);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }

        } catch (Exception e) {
            log.error("Exception in API call: ", e);
            ApiErrorResponse<String> errorResponse = new ApiErrorResponse<>(500, "Internal Server Error", e.getMessage(),null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // *  Get Balance
    public String getBalance(String customerId){
        String jsonBody = String.format("{\"customerId\":\"%s\"}", customerId);
        return makeApiCall("/service-api/api/v1/service/digitalgold/customer/get_balance",jsonBody);
    }

    // *  Activation Account
    public String activateUserAccount(String customerId){
        String jsonBody = String.format("{\"customerId\":\"%s\"}", customerId);
        return makeApiCall("/service-api/api/v1/service/digitalgold/customer/activate",jsonBody);
    }

    // *  Deactivation Account
    public String deActivateUserAccount(String customerId){
        String jsonBody = String.format("{\"customerId\":\"%s\"}", customerId);
        return makeApiCall("/service-api/api/v1/service/digitalgold/customer/deactivate",jsonBody);
    }

    // *  Update Profile for Digital Gold
    public String updateProfileForDigitalGold(DigitalGoldProfileUpdateRequest requestDto) {
        try {
            // Convert DTO to JSON
            String jsonBody = objectMapper.writeValueAsString(requestDto);
            // Call API
            return makeApiCall("/service-api/api/v1/service/digitalgold/customer/update", jsonBody);
        } catch (Exception e) {
            log.error("Error while converting DTO to JSON", e);
            return "{\"error_code\": 500, \"error_message\": \"Failed to serialize request\"}";
        }
    }

    /*
    *
    *
    * Buy Gold Silver Services
    *
    *
    */


    // * Get Quotations
    public String getQuotations(String customerId,String amount, String quantity){
        String jsonBody  = String.format("{\"calculation_type\":\"A/Q\",\"metal_type\":\"G/S\",\"customer_id\":\"%s\",\"amount\":\"%s\",\"quantity\":\"%s\"}",customerId,amount,quantity);
        return makeApiCall("/service-api/api/v1/service/digitalgold/trade/quotebuy",jsonBody);
    }

    // * Validate Quotations
    public String validateQuotations(String customerId,String billingAddressId, String quoteId){
        String jsonBody  = String.format("{\"customer_id\":\"%s\",\"billing_address_id\":\"%s\",\"quote_id\":\"%s\"}",customerId,billingAddressId,quoteId);
        return makeApiCall("/service-api/api/v1/service/digitalgold/trade/validate_quote",jsonBody);
    }

    //* Send OTP

    public String sendOTPToCustomer(String refId,String customerId,String billingAddressId,String quoteId){
        String jsonBody  = String.format("{\"refid\":\"%s\",\"customer_id\":\"%s\",\"billing_address_id\":\"%s\",\"quote_id\":\"%s\"}",refId,customerId,billingAddressId,quoteId);
        return makeApiCall("/service-api/api/v1/service/digitalgold/trade/send_otp",jsonBody);
    }

    //* Buy Execute

    public String buyExecute(String refId,String customerId,String billingAddressId,String quoteId,String stateResp,Double otp){
        String jsonBody  = String.format("{\"otp\":%s,\"stateresp\":\"%s\",\"quote_id\":\"%s\",\"customer_id\":\"%s\",\"billing_address_id\":\"%s\",\"refid\":\"%s\"}",otp, stateResp,quoteId,customerId,billingAddressId,refId);
        return makeApiCall("/service-api/api/v1/service/digitalgold/trade/buyexecute",jsonBody);
    }

    //* Transaction Status

    public String transactionStatus(String refId){
        String jsonBody  = String.format( "{\"refid\":\"%s\"}",refId);
        return makeApiCall("/service-api/api/v1/service/digitalgold/trade/status",jsonBody);
    }


}
