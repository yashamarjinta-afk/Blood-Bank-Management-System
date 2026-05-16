package com.BloodBank.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.BloodBank.DTO.BloodStockResponseDTO;
import com.BloodBank.Entity.BloodStock;
import com.BloodBank.Entity.Donation;


@Repository
public interface BloodStockRepository extends JpaRepository<BloodStock, Long> {

	 List<BloodStock> findByBloodGroupAndStorageCenter_CityAndExpiryDateAfter(
	            String bloodGroup,
	            String city,
	            LocalDate date
	    );
	 
	 List<BloodStock> findByBloodGroup(String bloodGroup);
	 
	 boolean existsByDonation(Donation donation);

	 List<BloodStock> findByBloodGroupAndExpiryDateAfter(String bloodGroup, LocalDate date);
	 
	 List<BloodStock> findByStorageCenterId(Long id);
	 
	 List<BloodStock> findByBloodGroupAndStorageCenter_IdAndExpiryDateAfter(
		        String bloodGroup,
		        Long centerId,
		        LocalDate date
		);
	
}

