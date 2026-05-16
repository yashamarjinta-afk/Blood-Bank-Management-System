package com.BloodBank.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.BloodBank.Entity.User;
import com.BloodBank.Entity.User.Role;
import java.util.List;


@Repository
public interface Userrepository extends JpaRepository<User, Long> {
     
	Optional<User> findByEmail(String email);
	
	List<User> findByRolesContaining(Role role);
}
