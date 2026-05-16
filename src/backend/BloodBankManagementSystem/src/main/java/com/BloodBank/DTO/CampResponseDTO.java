package com.BloodBank.DTO;

import java.time.LocalDate;

public class CampResponseDTO {

	  private String campName;
	    private String location;
	    private LocalDate campDate;
	    private String description;
	    private String contactNumber;
	    private Long organizerId;
	    private String organizerName;
	    private Long id;
	    private String status;
	    private String message;
	    private String address;
	    
	    
		public CampResponseDTO(String campName, String location, LocalDate campDate, String description, Long id,
				String status, String address) {
			super();
			this.campName = campName;
			this.location = location;
			this.campDate = campDate;
			this.description = description;
			this.id = id;
			this.status = status;
			this.address = address;
		}
		
		public CampResponseDTO() {
			
		}

		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getOrganizerName() {
			return organizerName;
		}
		public void setOrganizerName(String organizerName) {
			this.organizerName = organizerName;
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
	    
}
