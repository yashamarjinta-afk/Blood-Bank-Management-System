package com.BloodBank.DTO;

import java.time.LocalDate;

public class BloodStockResponseDTO {

	 private String bloodGroup;
	    private Integer units;
	    private LocalDate expiryDate;
	    private String centerName;
	    private String city;
	    
	    
		public BloodStockResponseDTO(String bloodGroup, Integer units) {
			super();
			this.bloodGroup = bloodGroup;
			this.units = units;
		}
		
		
		public BloodStockResponseDTO() {
			
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
		public LocalDate getExpiryDate() {
			return expiryDate;
		}
		public void setExpiryDate(LocalDate expiryDate) {
			this.expiryDate = expiryDate;
		}
		public String getCenterName() {
			return centerName;
		}
		public void setCenterName(String centerName) {
			this.centerName = centerName;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
	    
}
