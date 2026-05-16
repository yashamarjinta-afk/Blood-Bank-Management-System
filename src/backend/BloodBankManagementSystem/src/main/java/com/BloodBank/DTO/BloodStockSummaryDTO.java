package com.BloodBank.DTO;

public class BloodStockSummaryDTO {

	 private String bloodGroup;
	    private Integer units;
	    
	    
		public BloodStockSummaryDTO(String bloodGroup, Integer units) {
			super();
			this.bloodGroup = bloodGroup;
			this.units = units;
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

	   
}
