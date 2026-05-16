package com.BloodBank.DTO;

public class BloodrequestResponseDTO {

	  private Long id;
	    private String hospitalname;
	    private String bloodGroup;
	    private int units;
	    private String city;
	    private String status;
	    private String centerName;
	    private String address;
	    private String contact;
	    private String message;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
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
		public int getUnits() {
			return units;
		}
		public void setUnits(int units) {
			this.units = units;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getCenterName() {
			return centerName;
		}
		public void setCenterName(String centerName) {
			this.centerName = centerName;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getContact() {
			return contact;
		}
		public void setContact(String contact) {
			this.contact = contact;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}	
	    
}
