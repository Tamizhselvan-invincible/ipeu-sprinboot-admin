package com.hetero.models.bus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import jakarta.validation.constraints.*;


@Data
public class Passenger {

    @NotBlank(message = "Name is required")
    private String name;

    @Min(value = 6000000000L, message = "Enter a valid 10-digit mobile number")
    @Max(value = 9999999999L, message = "Enter a valid 10-digit mobile number")
    private Long mobile;

    @NotBlank(message = "Title is required")
    private String title;

    @Email(message = "Enter a valid email")
    private String email;

    @Min(value = 1, message = "Age must be greater than 0")
    @Max(value = 120, message = "Age must be realistic")
    private int age;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "ID Type is required")
    private String idType;

    @NotBlank(message = "ID Number is required")
    private String idNumber;

    @NotBlank(message = "Primary field is required")
    private String primary;
}

