package com.BloodBank.DTO;

import java.time.LocalDate;

public class CampListResponseDTO {

	 private Long id;
	    private String campName;
	    private String location;
	    private LocalDate campDate;
	    private String organizerName;
	    private String address;
	    private String status;
	    
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
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
		public String getOrganizerName() {
			return organizerName;
		}
		public void setOrganizerName(String organizerName) {
			this.organizerName = organizerName;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
	    
}
