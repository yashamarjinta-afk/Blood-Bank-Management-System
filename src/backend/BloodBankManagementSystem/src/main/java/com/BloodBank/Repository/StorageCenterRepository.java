package com.BloodBank.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.BloodBank.Entity.StorageCenter;

@Repository
public interface StorageCenterRepository extends JpaRepository<StorageCenter, Long> {
  
	List<StorageCenter> findByCityIgnoreCase(String city);
	
	boolean existsByNameIgnoreCaseAndAddressIgnoreCase(String name,String address);
	
	Optional<StorageCenter> findById(Long id);
}
