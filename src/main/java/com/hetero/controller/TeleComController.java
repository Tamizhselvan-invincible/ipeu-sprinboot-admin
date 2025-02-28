package com.hetero.controller;

import com.hetero.service.TelecomScrizaAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/telecom")
public class TeleComController {

    @Autowired
    TelecomScrizaAPIService telecomService;


    /** * Mobile DTH End Points */

    @PostMapping("/balance")
    public ResponseEntity<String> getBalanceFromAPIService(){
        return ResponseEntity.ok(telecomService.getBalanceAmount());
    }

    @PostMapping("/payment")
    public ResponseEntity<String> processRecharge(
            @RequestParam String mobileNo,
            @RequestParam String amount,
            @RequestParam String providerId,
            @RequestParam String clientId) {

   return ResponseEntity.ok(telecomService.rechargePayment(mobileNo,amount,providerId,clientId));
    }

    /** Recharge Plan End Points */

    @PostMapping("/prepaid_plans")
    public ResponseEntity<String> getPrepaidPlansFromAPIService1(
            @RequestParam String providerId,
            @RequestParam String stateId) {

        return ResponseEntity.ok(telecomService.getPlansService1(providerId,stateId));
    }

    @PostMapping("/prepaid_plans2")
    public ResponseEntity<String> getPrepaidPlansFromAPIService2(
            @RequestParam String providerId,
            @RequestParam String stateId) {

        return ResponseEntity.ok(telecomService.getPlansService2(providerId,stateId));
    }

    @PostMapping("/r-offer")
    public ResponseEntity<String> getRofferPlanFromService(
            @RequestParam String providerId,
            @RequestParam String mobileNo) {
        return ResponseEntity.ok(telecomService.getRofferPlan(providerId,mobileNo));
    }

    @PostMapping("/dth-plans")
    public ResponseEntity<String> getDTHPlanFromService(
            @RequestParam String providerId) {
        return ResponseEntity.ok(telecomService.getDTHPlans(providerId));
    }

    @PostMapping("/find-operator")
    public ResponseEntity<String> getOperatorFromService(
            @RequestParam String mobileNo) {
        return ResponseEntity.ok(telecomService.findOperator(mobileNo));
    }

    @PostMapping("/state-list")
    public ResponseEntity<String> getStateListFromService() {
        return ResponseEntity.ok(telecomService.getStateList());
    }


    /* ** Bill Payment End Points */

    @PostMapping("/get-provider")
    public ResponseEntity<String> getProvidersListFormService() {
        return ResponseEntity.ok(telecomService.getProvidersList());
    }



}
