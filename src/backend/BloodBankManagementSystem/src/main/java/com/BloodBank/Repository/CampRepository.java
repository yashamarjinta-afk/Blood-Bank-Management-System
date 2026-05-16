package com.BloodBank.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.BloodBank.Entity.Camp;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import com.BloodBank.Entity.User;



@Repository
public interface CampRepository extends JpaRepository<Camp, Long>{
	
	 Optional<Camp> findById(Long id);
	 
	 boolean existsByCampNameIgnoreCaseAndAddressIgnoreCase(
		        String campName,
		        String address
		);
	 
	 List<Camp> findByLocationIgnoreCase(String location);
	 
	  
	 
	 boolean existsByOrganizerId(Long organizerId);
	 
	 List<Camp> findByOrganizerIsNotNullAndOrganizer_EmailIgnoreCase( String email);
	 
	 List<Camp> findByOrganizer_Email(String email);

}
