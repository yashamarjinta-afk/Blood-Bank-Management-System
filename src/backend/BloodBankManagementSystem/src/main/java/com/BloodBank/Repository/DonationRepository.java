package com.BloodBank.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.BloodBank.Entity.Donation;
import com.BloodBank.Entity.Donation.DonationStatus;
import com.BloodBank.Entity.Donation.OrganizerStatus;
import com.BloodBank.Entity.Camp;
import com.BloodBank.Entity.Camp.Status;
import com.BloodBank.Entity.User;


@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {

	 List<Donation> findByStatus(Donation.DonationStatus status);
	 
	 List<Donation> findByUserEmailOrderByDateDesc(String email);

	    List<Donation> findByUserEmail(String email);
	    
	    boolean existsByUserAndCamp(User user, Camp camp);

	    Optional<Donation> findTopByUserEmailOrderByDateDesc(String email);
	    
	    @Query("""
	    		SELECT d FROM Donation d
	    		WHERE d.camp.id IN (
	    		    SELECT c.id FROM Camp c WHERE c.organizer.email = :email
	    		)
	    		AND d.status = :status
	    		""")
	    List<Donation> findByCamp_Organizer_EmailAndStatus(String email, DonationStatus status);
	    
	    List<Donation> findByCampInAndStatus(List<Camp> camps, DonationStatus status);
	    
	    List<Donation> findByCampIdAndStatus(Long campId, DonationStatus status);
	    
	    List<Donation> findByStatusAndOrganizerStatus(
	            DonationStatus status,
	            OrganizerStatus organizerStatus
	    );
	    
	    
}
