package com.BloodBank.DTO;

public class RequestPendingDTO {

	private Long id;
    private String bloodGroup;
    private Integer units;
    private String status;
	
	
	public RequestPendingDTO(Long id, String bloodGroup, Integer units, String status) {
		super();
		this.id = id;
		this.bloodGroup = bloodGroup;
		this.units = units;
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
