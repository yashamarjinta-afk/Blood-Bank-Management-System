package com.BloodBank.DTO;

import jakarta.validation.constraints.NotNull;

public class approveRequestDTO {

    @NotNull(message = "Donation ID is required")
    private Long id;

    @NotNull(message = "Storage Center ID is required")
    private Long storageCenterId;

    // getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStorageCenterId() {
        return storageCenterId;
    }

    public void setStorageCenterId(Long storageCenterId) {
        this.storageCenterId = storageCenterId;
    }
}