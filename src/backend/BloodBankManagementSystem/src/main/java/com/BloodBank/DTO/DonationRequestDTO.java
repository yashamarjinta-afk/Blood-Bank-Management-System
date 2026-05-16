package com.BloodBank.DTO;

import java.time.LocalDate;

import jakarta.validation.constraints.*;

public class DonationRequestDTO {

    @NotNull(message = "Camp ID is required")
    private Long campId;

    @NotBlank(message = "Location is required")
    private String location;

    

    @NotNull(message = "Units are required")
    @Min(value = 1, message = "Minimum 1 unit required")
    @Max(value = 8, message = "Maximum 8 units allowed")
    private Integer units;

    // getters and setters

    public Long getCampId() {
        return campId;
    }

    public void setCampId(Long campId) {
        this.campId = campId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

   

    public Integer getUnits() {
        return units;
    }

    public void setUnits(Integer units) {
        this.units = units;
    }
}