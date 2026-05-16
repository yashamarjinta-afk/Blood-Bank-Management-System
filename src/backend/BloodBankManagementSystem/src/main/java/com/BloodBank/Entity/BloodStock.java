package com.BloodBank.Entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class BloodStock {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String bloodGroup;

	    private Integer units;

	    private LocalDate storedDate;

	    private LocalDate expiryDate;

	    // 🔗 From which donation
	    @ManyToOne
	    @JoinColumn(name = "donation_id")
	    private Donation donation;

	    // 🔗 Stored in which center
	    @ManyToOne
	    @JoinColumn(name = "storage_center_id")
	    private StorageCenter storageCenter;

	    
	    
		

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

		public LocalDate getStoredDate() {
			return storedDate;
		}

		public void setStoredDate(LocalDate storedDate) {
			this.storedDate = storedDate;
		}

		public LocalDate getExpiryDate() {
			return expiryDate;
		}

		public void setExpiryDate(LocalDate expiryDate) {
			this.expiryDate = expiryDate;
		}

		public Donation getDonation() {
			return donation;
		}

		public void setDonation(Donation donation) {
			this.donation = donation;
		}

		public StorageCenter getStorageCenter() {
			return storageCenter;
		}

		public void setStorageCenter(StorageCenter storageCenter) {
			this.storageCenter = storageCenter;
		}
	    
	    
}
