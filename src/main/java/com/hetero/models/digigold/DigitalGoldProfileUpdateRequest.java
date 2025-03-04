package com.hetero.models.digigold;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@AllArgsConstructor
public class DigitalGoldProfileUpdateRequest {

    @JsonProperty("customer_id")
    private String customerId;

    private long mobile;
    private String name;
    private String email;

    @JsonProperty("pan_number")
    private String panNumber;

    @JsonProperty("billing_address_id")
    private String billingAddressId;

    @JsonProperty("billing_address_line1")
    private String billingAddressLine1;
    
    @JsonProperty("billing_address_line2")
    private String billingAddressLine2;
    
    @JsonProperty("billing_state")
    private String billingState;
    
    @JsonProperty("billing_city")
    private String billingCity;
    
    @JsonProperty("billing_zip")
    private String billingZip;
    
    @JsonProperty("billing_country")
    private String billingCountry;
    
    @JsonProperty("billing_statecode")
    private String billingStateCode;
    
    @JsonProperty("billing_mobile")
    private long billingMobile;

    @JsonProperty("delivery_mobile")
    private long deliveryMobile;
    
    @JsonProperty("delivery_country")
    private String deliveryCountry;
    
    @JsonProperty("delivery_zip")
    private String deliveryZip;
    
    @JsonProperty("delivery_state")
    private String deliveryState;
    
    @JsonProperty("delivery_city")
    private String deliveryCity;

    @JsonProperty("delivery_address_id")
    private long deliveryAddressId;
    
    @JsonProperty("delivery_address_line1")
    private String deliveryAddressLine1;
    
    @JsonProperty("delivery_address_line2")
    private String deliveryAddressLine2;
    
    @JsonProperty("delivery_statecode")
    private String deliveryStateCode;
}
