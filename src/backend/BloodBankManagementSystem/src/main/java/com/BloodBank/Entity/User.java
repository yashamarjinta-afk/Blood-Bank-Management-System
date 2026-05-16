package com.BloodBank.Entity;

import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    
    private Integer otp;
    
    private Long generationtime;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String bloodGroup;

    private String city;

    @Lob
    private String image; // Base64 or URL
    
    public enum Role{
    	HOSPITAL,
    	ORGANIZER,
    	DONOR,
    	ADMIN
    };

    // 🔥 Roles (PATIENT, DONOR, ORGANIZER, ADMIN)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private List<Role> roles = new ArrayList<>();
    
   

    // 🔗 One user → many donations
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Donation> donations = new ArrayList<>();

    // ================= GETTERS & SETTERS =================

    
    
    public Long getId() {
        return id;
    }

   

	



	



	public Integer getOtp() {
		return otp;
	}











	public void setOtp(Integer otp) {
		this.otp = otp;
	}











	public Long getGenerationtime() {
		return generationtime;
	}



	public void setGenerationtime(Long generationtime) {
		this.generationtime = generationtime;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Donation> getDonations() {
        return donations;
    }

    public void setDonations(List<Donation> donations) {
        this.donations = donations;
    }
}