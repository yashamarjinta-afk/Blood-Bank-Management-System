package com.BloodBank.DTO;

import java.time.LocalDate;

import jakarta.validation.constraints.*;

public class CampRequestDTO {

    @NotBlank(message = "Camp name is required")
    private String campName;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Camp date is required")
    private LocalDate campDate;

    @NotBlank(message = "Description is required")
    @Size(min = 10, message = "Description must be at least 10 characters")
    private String description;

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Contact number must be 10 digits")
    private String contactNumber;

    @NotNull(message = "Organizer ID is required")
    private Long organizerId;

    // Optional fields (you can set default in backend)
    
    private String address;

    // getters and setters

    public String getCampName() {
        return campName;
    }

    public void setCampName(String campName) {
        this.campName = campName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getCampDate() {
        return campDate;
    }

    public void setCampDate(LocalDate campDate) {
        this.campDate = campDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Long getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(Long organizerId) {
        this.organizerId = organizerId;
    }

   

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}