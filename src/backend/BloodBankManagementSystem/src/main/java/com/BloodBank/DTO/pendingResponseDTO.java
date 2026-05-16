package com.BloodBank.DTO;

import java.time.LocalDate;

public class pendingResponseDTO {

    private Long id;
    private String donorName;
    private String bloodGroup;
    private LocalDate donationDate;
   

   
    
    public pendingResponseDTO() {
		
	}

	public pendingResponseDTO(Long id, String donorName, String bloodGroup, LocalDate donationDate) {
		
		this.id = id;
		this.donorName = donorName;
		this.bloodGroup = bloodGroup;
		this.donationDate = donationDate;
	}

	public Long getId() {
        return id;
    }

    public String getDonorName() {
        return donorName;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public LocalDate getDonationDate() {
        return donationDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public void setDonationDate(LocalDate donationDate) {
        this.donationDate = donationDate;
    }
}

