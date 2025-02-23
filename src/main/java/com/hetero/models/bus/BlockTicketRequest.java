package com.hetero.models.bus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.Map;

@Data
public class BlockTicketRequest {
    @Min(value = 1, message = "Available Trip ID is required and must be greater than 0")
    private Long availableTripId;

    @Min(value = 1, message = "Boarding Point ID is required and must be greater than 0")
    private Long boardingPointId;

    @NotNull(message = "Inventory Items cannot be null")
    @Valid
    private Map<String, InventoryItem> inventoryItems;

    @NotBlank(message = "Source is required")
    private String source;

    @NotBlank(message = "Dropping Point ID is required")
    private String droppingPointId;

    @NotBlank(message = "Destination is required")
    private String destination;

    @NotBlank(message = "Booking Type is required")
    private String bookingType;

    @NotBlank(message = "Payment Mode is required")
    private String paymentMode;

    @NotBlank(message = "Service Charge is required")
    private String serviceCharge;
}
