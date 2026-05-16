package com.BloodBank.DTO;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DonationHistoryDTO {

	 private Long id;
	    private String bloodGroup;
	    @JsonFormat(pattern = "yyyy-MM-dd")
	    private LocalDate donationDate;
	    private String status;
	    
	    
		public DonationHistoryDTO(Long id, String bloodGroup, LocalDate donationDate, String status) {
			this.id = id;
			this.bloodGroup = bloodGroup;
			this.donationDate = donationDate;
			this.status = status;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getBloodGroup() {
			return bloodGroup;
		}
		public void setBloodGroup(String bloodGroup) {
			this.bloodGroup = bloodGroup;
		}
		public LocalDate getDonationDate() {
			return donationDate;
		}
		public void setDonationDate(LocalDate donationDate) {
			this.donationDate = donationDate;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}

}
