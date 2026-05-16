package com.BloodBank.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.BloodBank.Entity.BloodRequest;
import com.BloodBank.Entity.BloodRequest.Status;

import java.util.List;
import java.util.Optional;


@Repository
public interface BloodRequestRepository extends JpaRepository<BloodRequest, Long> {

	Optional<BloodRequest> findById(Long id);
	
	 List<BloodRequest> findByHospital_Email(String email);
	 
	 List<BloodRequest> findByStatusAllIgnoreCase(Status status);
}