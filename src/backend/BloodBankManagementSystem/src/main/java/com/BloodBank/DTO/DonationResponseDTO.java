package com.BloodBank.DTO;

import java.time.LocalDate;

import com.BloodBank.Entity.Donation.DonationStatus;

public class DonationResponseDTO {

	
	    private String bloodGroup;
	    private String location;
	    private LocalDate date;
	    private Integer units;
	    private Long campId;
	    private DonationStatus status;
	    private String certificateId;
	    private String message;
	    
		
		public Long getCampId() {
			return campId;
		}
		public void setCampId(Long campId) {
			this.campId = campId;
		}
		public DonationStatus getStatus() {
			return status;
		}
		public void setStatus(DonationStatus status) {
			this.status = status;
		}
		public String getBloodGroup() {
			return bloodGroup;
		}
		public void setBloodGroup(String bloodGroup) {
			this.bloodGroup = bloodGroup;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public LocalDate getDate() {
			return date;
		}
		public void setDate(LocalDate date) {
			this.date = date;
		}
		
		
		public Integer getUnits() {
			return units;
		}
		public void setUnits(Integer units) {
			this.units = units;
		}
		public String getCertificateId() {
			return certificateId;
		}
		public void setCertificateId(String certificateId) {
			this.certificateId = certificateId;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
}
