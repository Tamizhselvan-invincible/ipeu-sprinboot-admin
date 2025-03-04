package com.hetero.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hetero.models.digigold.DigitalGoldProfileCreateRequest;
import com.hetero.models.digigold.DigitalGoldProfileUpdateRequest;
import com.hetero.service.PSDigitalGoldService;
import com.hetero.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/digitalgold")
public class DigitalGoldController {

    @Autowired
    private PSDigitalGoldService digitalGoldService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/profile")
    public ResponseEntity<?> getUserProfileFromService(@RequestParam  long mobileNo){

        String data = digitalGoldService.getProfile(mobileNo);
        JsonNode jsonData;
        try {
            jsonData = objectMapper.readTree(data);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "Error parsing JSON", null));
        }

        ApiResponse<JsonNode> apiResponse = new ApiResponse<>(200, "Profile Retrieved", jsonData);
        return ResponseEntity.ok().body(apiResponse);
    }

    @PostMapping("/profile/create")
    public ResponseEntity<?> createProfileForDigitalGoldFromService(@RequestBody DigitalGoldProfileCreateRequest requestDto){

        String data = digitalGoldService.createProfileForDigitalGold(requestDto);
        JsonNode jsonData;
        try {
            jsonData = objectMapper.readTree(data);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "Error parsing JSON", null));
        }

        ApiResponse<JsonNode> apiResponse = new ApiResponse<>(200, "Profile Created", jsonData);
        return ResponseEntity.ok().body(apiResponse);
    }

    @PostMapping("/balance")
    public ResponseEntity<?> getBalanceFromService(@RequestParam String customerId){

        String data = digitalGoldService.getBalance(customerId);
        JsonNode jsonData;
        try {
            jsonData = objectMapper.readTree(data);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "Error parsing JSON", null));
        }

        ApiResponse<JsonNode> apiResponse = new ApiResponse<>(200, "Balance Retrieved", jsonData);
        return ResponseEntity.ok().body(apiResponse);
    }

    @PostMapping("/activate")
    public ResponseEntity<?> activateAccFromService(@RequestParam String customerId){

        String data = digitalGoldService.activateUserAccount(customerId);
        JsonNode jsonData;
        try {
            jsonData = objectMapper.readTree(data);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "Error parsing JSON", null));
        }

        ApiResponse<JsonNode> apiResponse = new ApiResponse<>(200, "Account activated", jsonData);
        return ResponseEntity.ok().body(apiResponse);
    }



    @PostMapping("/deactivate")
    public ResponseEntity<?> deActivateAccFromService(@RequestParam String customerId){

        String data = digitalGoldService.deActivateUserAccount(customerId);
        JsonNode jsonData;
        try {
            jsonData = objectMapper.readTree(data);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "Error parsing JSON", null));
        }

        ApiResponse<JsonNode> apiResponse = new ApiResponse<>(200, "Account deactivated", jsonData);
        return ResponseEntity.ok().body(apiResponse);
    }


    @PostMapping("/profile/update")
    public ResponseEntity<?> updateProfileDetailsFromService(@RequestBody DigitalGoldProfileUpdateRequest requestDto){

        String data = digitalGoldService.updateProfileForDigitalGold(requestDto);
        JsonNode jsonData;
        try {
            jsonData = objectMapper.readTree(data);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "Error parsing JSON", null));
        }

        ApiResponse<JsonNode> apiResponse = new ApiResponse<>(200, "Profile Updated", jsonData);
        return ResponseEntity.ok().body(apiResponse);
    }

}
