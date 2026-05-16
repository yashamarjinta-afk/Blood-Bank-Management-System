package com.BloodBank.DTO;

import jakarta.validation.constraints.*;

public class BloodRequestrequestDTO {

    @NotBlank(message = "Hospital name is required")
    private String hospitalname;

    @NotBlank(message = "Blood group is required")
    private String bloodGroup;

    @NotNull(message = "Units are required")
    @Min(value = 1, message = "Minimum 1 unit required")
    @Max(value = 8, message = "Maximum 8 units allowed")
    private Integer units;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    // getters and setters

    public String getHospitalname() {
        return hospitalname;
    }

    public void setHospitalname(String hospitalname) {
        this.hospitalname = hospitalname;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public Integer getUnits() {
        return units;
    }

    public void setUnits(Integer units) {
        this.units = units;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}