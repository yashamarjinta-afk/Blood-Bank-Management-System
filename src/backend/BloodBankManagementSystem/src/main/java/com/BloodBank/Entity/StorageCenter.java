package com.BloodBank.Entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class StorageCenter {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String name;     // Hospital / Blood Bank name
	    private String city;
	    private String address;
	    private String contact;

	    // 🔗 One center → many stock entries
	    @OneToMany(mappedBy = "storageCenter", cascade = CascadeType.ALL)
	    private List<BloodStock> stocks = new ArrayList<>();

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

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
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

		public List<BloodStock> getStocks() {
			return stocks;
		}

		public void setStocks(List<BloodStock> stocks) {
			this.stocks = stocks;
		}
	    
}
