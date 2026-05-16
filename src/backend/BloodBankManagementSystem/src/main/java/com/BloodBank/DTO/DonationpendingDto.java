package com.BloodBank.DTO;

public class DonationpendingDto {

	

	    private Long id;
	    private String name;
	    private String bloodGroup;
	    private Integer units;
	    private String status;

	    public DonationpendingDto(Long id, String name, String bloodGroup, Integer units, String status) {
	        this.id = id;
	        this.name = name;
	        this.bloodGroup = bloodGroup;
	        this.units = units;
	        this.status = status;
	    }
	
		public DonationpendingDto() {
			super();
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
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

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
	    
	    
}
