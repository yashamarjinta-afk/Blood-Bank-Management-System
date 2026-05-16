package com.BloodBank.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class BloodRequest {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String HospitalName;
	    
	    private String email;

	    private String bloodGroup;

	    private int units;

	    private String city;

	    @Enumerated(EnumType.STRING)
	    private Status status;
	    
	    @ManyToOne
	    @JoinColumn(name = "user_id")
	    private User hospital;

	    // 🔗 Assigned after admin approval
	    @ManyToOne
	    @JoinColumn(name = "storage_center_id")
	    private StorageCenter assignedCenter;

	    public enum Status {
	        PENDING,
	        APPROVED,
	        REJECTED
	    }
	    
	    

		public User getHospital() {
			return hospital;
		}

		public void setHospital(User hospital) {
			this.hospital = hospital;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		

		public String getHospitalName() {
			return HospitalName;
		}

		public void setHospitalName(String hospitalName) {
			HospitalName = hospitalName;
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

		public Status getStatus() {
			return status;
		}

		public void setStatus(Status status) {
			this.status = status;
		}

		public StorageCenter getAssignedCenter() {
			return assignedCenter;
		}

		public void setAssignedCenter(StorageCenter assignedCenter) {
			this.assignedCenter = assignedCenter;
		}
	    
	    
	
}
