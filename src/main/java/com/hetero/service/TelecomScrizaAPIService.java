package com.hetero.service;

import com.hetero.utils.OkHttpClientProvider;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.json.JSONObject;

import java.util.Map;


@Service
public class TelecomScrizaAPIService {

    @Autowired
    private OkHttpClientProvider okHttpClientProvider;

    private static final Logger log = LogManager.getLogger(TelecomScrizaAPIService.class);
    @Value("${application.scriza.base-url}")
    String baseURL;

    @Value("${application.scriza.api-token}")
    String apiKey;

    @Autowired
    PaymentVerificationService paymentVerificationService;

    String environment = "UAT";

    // TODO: Need To Modify the redundant Code

    // * Mobile Recharge Plans
    public String rechargePayment(String mobileNo, String amount, String providerId, String clientId) {
        HttpUrl url = buildUrl("api/telecom/v1/payment", Map.of(
                "api_token", apiKey,
                "number", mobileNo,
                "amount", amount,
                "provider_id", providerId,
                "client_id", clientId,
                "environment", environment
        ));

        String responseString = executeGetRequest(url);
        if (responseString == null) {
            return "{\"error_code\": 500, \"error_message\": \"Failed to process request\"}";
        }


        JSONObject jsonResponse = new JSONObject(responseString);
        JSONObject transactionDetails = jsonResponse.optJSONObject("transaction_details"); // Extract nested JSON

        String payId = transactionDetails != null ? transactionDetails.optString("payid", "") : "";
        String operatorRef = transactionDetails != null ? transactionDetails.optString("operator_ref", "") : "";

        String status = "success".equals(jsonResponse.optString("status")) ? "success" : "failure";

       String callBackURL =  callCallbackURL(payId, operatorRef, mobileNo, providerId, clientId, amount, status);

//        return responseString;
        return callBackURL;
    }


    // ! This Method Gives Error Check the Documentation
    // ! Need to Change
    /*
     ? Final assessment is revels this is works like a Recharge API.
     ? This Requires the amount Field which is the documentation doesn't mention
     ? I don't know this is the flow or not. currently I'm use the same "/payment"
     ? Endpoint for both API calls.
    */
    private String callCallbackURL(
            String payId,
            String operatorRef,
            String mobileNo,
            String providerId,
            String clientId,
            String amount,
            String status) {
        HttpUrl callbackUrl = buildUrl("api/telecom/v1/payment", Map.of(
                "api_token", apiKey,
                "payid", payId,
                "status", status,
                "operator_ref", operatorRef,
                "client_id", clientId,
                "number", mobileNo,
                "amount", amount,
                "provider_id", providerId,
                "wallet_type", "1",
                "environment", environment
        ));

//        System.out.println("Before Execute callbackUrl: " + callbackUrl);
        String callBackResponse = executeGetRequest(callbackUrl);
//        System.out.println("After Execute callbackUrl: " + callBackResponse);


        return callBackResponse;
    }

    // Generic Method to Execute API Calls
    private String executeGetRequest(HttpUrl url) {
        OkHttpClient client = okHttpClientProvider.getClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("API Error: {} - {}", response.code(), response.message());
                return null;
            }
            return response.body() != null ? response.body().string() : null;
        } catch (Exception e) {
            log.error("Exception in API call: ", e);
            return null;
        }
    }

    // Generic Method to Build URLs
    private HttpUrl buildUrl(String path, Map<String, String> params) {
        HttpUrl.Builder urlBuilder = new HttpUrl.Builder()
                .scheme("https")
                .host(baseURL.replace("https://", "").replace("http://", ""))
                .addPathSegments(path);

        params.forEach(urlBuilder::addQueryParameter);
        return urlBuilder.build();
    }


    public  String getBalanceAmount(){

        String url = baseURL+"/api/telecom/v1/check-balance?api_token="+apiKey;

        OkHttpClient client = okHttpClientProvider.getClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
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

    // * Recharge Plan Services
    // TODO: Need Modify this Also
    public String getPlansService1(String providerId,String stateId) {


        OkHttpClient client = okHttpClientProvider.getClient();

        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("api_token", apiKey)
                .addFormDataPart("provider_id", providerId)
                .addFormDataPart("state_id", stateId)
                .build();

        Request request = new Request.Builder()
                .url(baseURL+"/api/plan/v1/prepaid-plan")
                .post(formBody)
                .addHeader("environment", environment)
                .addHeader("Accept", "application/json") // Optional header
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

    public String getPlansService2(String providerId,String stateId) {

        OkHttpClient client = okHttpClientProvider.getClient();

        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("api_token", apiKey)
                .addFormDataPart("provider_id", providerId)
                .addFormDataPart("state_id", stateId)
                .build();

        Request request = new Request.Builder()
                .url(baseURL+"/api/plan/v1/prepaid-plan2")
                .post(formBody)
                .addHeader("environment", environment)
                .addHeader("Accept", "application/json") // Optional header
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

public String getRofferPlan(String providerId, String mobileNo) {
    OkHttpClient client = okHttpClientProvider.getClient();

    RequestBody requestBody = new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("api_token", apiKey)
            .addFormDataPart("provider_id", providerId)
            .addFormDataPart("mobile_number", mobileNo)
            .build();

    // Creating the request
    Request request = new Request.Builder()
            .url(baseURL+"/api/plan/v1/r-offer")
            .post(requestBody)
            .addHeader("environment", environment)
            .build();


    try (Response response = client.newCall(request).execute()) {
        if (!response.isSuccessful()) {
            log.error("Error: {} - {}", response.code(), response.message());
            log.error("Response Body: {}", response.body().string());
            return String.format("{\"error_code\": %d, \"error_message\": \"%s\"}", response.code(), response.message());
        }
        return response.body().string();
    } catch (Exception e) {
        log.error("Exception in API call: ", e);
        return String.format("{\"error_code\": 500, \"error_message\": \"%s\"}", e.getMessage());
    }
}

    public String getDTHPlans(String providerId) {
        OkHttpClient client = okHttpClientProvider.getClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("api_token", apiKey)
                .addFormDataPart("provider_id", providerId)
                .build();

        Request request = new Request.Builder()
                .url(baseURL+"/api/plan/v1/dth-plans")
                .post(requestBody)
                .addHeader("environment", environment)
                .build();


        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("Error: {} - {}", response.code(), response.message());
                log.error("Response Body: {}", response.body().string());
                return String.format("{\"error_code\": %d, \"error_message\": \"%s\"}", response.code(), response.message());
            }
            return response.body().string();
        } catch (Exception e) {
            log.error("Exception in API call: ", e);
            return String.format("{\"error_code\": 500, \"error_message\": \"%s\"}", e.getMessage());
        }
    }


    // ! This Method Gives Error
    // ! Even Curl also Gives Error
    // ? Might be the Server Side Problem. Need to be Monitor

    public String findOperator(String mobileNo) {
        OkHttpClient client = okHttpClientProvider.getClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("api_token", apiKey)
                .addFormDataPart("mobile_number", mobileNo)
                .build();

        Request request = new Request.Builder()
                .url(baseURL+"/api/plan/v1/find-operator")
                .post(requestBody)
                .addHeader("environment", environment)
                .build();


        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("Error: {} - {}", response.code(), response.message());
                log.error("Response Body: {}", response.body().string());
                return String.format("{\"error_code\": %d, \"error_message\": \"%s\"}", response.code(), response.message());
            }
            return response.body().string();
        } catch (Exception e) {
            log.error("Exception in API call: ", e);
            return String.format("{\"error_code\": 500, \"error_message\": \"%s\"}", e.getMessage());
        }
    }

    public String getStateList() {
        OkHttpClient client = okHttpClientProvider.getClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("api_token", apiKey)
                .build();

        Request request = new Request.Builder()
                .url(baseURL+"/api/application/v1/state-list")
                .post(requestBody)
                .addHeader("environment", environment)
                .build();


        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("Error: {} - {}", response.code(), response.message());
                log.error("Response Body: {}", response.body().string());
                return String.format("{\"error_code\": %d, \"error_message\": \"%s\"}", response.code(), response.message());
            }
            return response.body().string();
        } catch (Exception e) {
            log.error("Exception in API call: ", e);
            return String.format("{\"error_code\": 500, \"error_message\": \"%s\"}", e.getMessage());
        }
    }

    // * Bill Payment Services

    public String getProvidersList() {
        OkHttpClient client = okHttpClientProvider.getClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("api_token", apiKey)
                .build();

        Request request = new Request.Builder()
                .url(baseURL+"/api/application/v1/get-provider")
                .post(requestBody)
                .addHeader("environment", environment)
                .build();


        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("Error: {} - {}", response.code(), response.message());
                log.error("Response Body: {}", response.body().string());
                return String.format("{\"error_code\": %d, \"error_message\": \"%s\"}", response.code(), response.message());
            }
            return response.body().string();
        } catch (Exception e) {
            log.error("Exception in API call: ", e);
            return String.format("{\"error_code\": 500, \"error_message\": \"%s\"}", e.getMessage());
        }
    }

}





//    public String rechargePayment(String mobileNo, String amount, String providerId, String clientId) {
//
//        HttpUrl url = new HttpUrl.Builder()
//                .scheme("https") // Use https or http
//                .host(baseURL.replace("https://", "").replace("http://", ""))
//                .addPathSegments("api/telecom/v1/payment")
//                .addQueryParameter("api_token", apiKey)
//                .addQueryParameter("number", mobileNo)
//                .addQueryParameter("amount", amount)
//                .addQueryParameter("provider_id", providerId)
//                .addQueryParameter("client_id", clientId)
//                .addQueryParameter("environment", environment)
//                .build();
//
//        OkHttpClient client = okHttpClientProvider.getClient();
//
//        // Build GET request
//        Request request = new Request.Builder()
//                .url(url)
//                .get()
//                .addHeader("Accept", "application/json") // Optional header
//                .build();
//
//        // Execute the request
//        try (Response response = client.newCall(request).execute()) {
//            if (!response.isSuccessful()) {
//                log.error("Error: {} - {}", response.code(), response.message());
//                return String.format("{\"error_code\": %d, \"error_message\": \"%s\"}", response.code(), response.message());
//            }
//            // Parse the JSON response
//
//            System.out.println("Before Call Back URL");
//            JSONObject jsonResponse = new JSONObject(response.body().string());
//            System.out.println(jsonResponse.toString());
//            if ("success".equals(jsonResponse.optString("status"))) {
//                System.out.println("At Call Back URL");
//                String payId = jsonResponse.optString("payid");
//                String operatorRef = jsonResponse.optString("operator_ref");
//                callCallbackURL(payId, operatorRef, mobileNo, providerId, clientId,amount);
//            }
//
//            return response.body().string();
//
//        } catch (Exception e) {
//            log.error("Exception in API call: ", e);
//            return String.format("{\"error_code\": 500, \"error_message\": \"%s\"}", e.getMessage());
//        }
//    }
//
//
//
//
//
//    // ! This Method Gives Error Check the Documentation
//    private void callCallbackURL(String payId, String operatorRef, String mobileNo, String providerId, String clientId,String amount) {
//        HttpUrl callbackUrl = new HttpUrl.Builder()
//                .scheme("https")
//                .host(baseURL.replace("https://", "").replace("http://", ""))
//                .addPathSegments("api/telecom/v1/payment")// Update this to the actual endpoint path
//                .addQueryParameter("api_token", apiKey)
//                .addQueryParameter("payid", payId)
//                .addQueryParameter("status", "success")
//                .addQueryParameter("operator_ref", operatorRef)
//                .addQueryParameter("client_id", clientId)
//                .addQueryParameter("number", mobileNo)
//                .addQueryParameter("amount", amount)
//                .addQueryParameter("provider_id", providerId)
//                .addQueryParameter("wallet_type", "1")// Assuming it's always 1
//                .addQueryParameter("environment", environment)
//                .build();
//
//        OkHttpClient client = okHttpClientProvider.getClient();
//
//        Request callbackRequest = new Request.Builder()
//                .url(callbackUrl)
//                .get()
//                .addHeader("Accept", "application/json")
//                .build();
//
//        try (Response response = client.newCall(callbackRequest).execute()) {
//            if (!response.isSuccessful()) {
//                log.error("Callback URL Error: {} - {}", response.code(), response.message());
//            }
//            System.out.println( response.body().string());
//        } catch (Exception e) {
//            log.error("Exception in Callback API call: ", e);
//        }
//    }