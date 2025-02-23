package com.hetero.models.bus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InventoryItem {

    @NotBlank(message = "Seat name is required")
    private String seatName;

    @Min(value = 0, message = "Fare must be 0 or greater")
    private double fare;

    @Min(value = 0, message = "Service tax must be 0 or greater")
    private double serviceTax;

    @Min(value = 0, message = "Operator service charge must be 0 or greater")
    private double operatorServiceCharge;

    private boolean ladiesSeat;

    @Valid
    private Passenger passenger;
}
