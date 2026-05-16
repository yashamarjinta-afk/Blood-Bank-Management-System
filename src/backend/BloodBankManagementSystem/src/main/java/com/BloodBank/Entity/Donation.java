package com.BloodBank.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "donations")
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String donorEmail;

    private String bloodGroup;

    private String location;
   @Column(nullable = false)
    private LocalDate date;

    private Integer units; // ✅ NEW

    private String certificateId; // ✅ NEW
    
    @Enumerated(EnumType.STRING)
    private OrganizerStatus organizerStatus;

    public enum OrganizerStatus {
        PENDING,
        VERIFIED,
        REJECTED
    }
    
    public enum DonationStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
    
    @ManyToOne
    @JoinColumn(name = "camp_id")
    private Camp camp;
    
    @Enumerated(EnumType.STRING)
    private DonationStatus status;
    
   

	@ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // ✅ RELATION
    

    // Getters & Setters

   

	public User getUser() {
		return user;
	}

	
	


	public OrganizerStatus getOrganizerStatus() {
		return organizerStatus;
	}





	public void setOrganizerStatus(OrganizerStatus organizerStatus) {
		this.organizerStatus = organizerStatus;
	}





	public Camp getCamp() {
		return camp;
	}


	public void setCamp(Camp camp) {
		this.camp = camp;
	}


	public void setStatus(DonationStatus status) {
		this.status = status;
	}


	public DonationStatus getStatus() {
		return status;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getId() { return id; }

    public String getDonorEmail() { return donorEmail; }

    public void setDonorEmail(String donorEmail) {
        this.donorEmail = donorEmail;
    }

    public String getBloodGroup() { return bloodGroup; }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getLocation() { return location; }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDate() { return date; }

    public void setDate(LocalDate date) {
        this.date = date;
    }

   

    public Integer getUnits() {
		return units;
	}





	public void setUnits(Integer units) {
		this.units = units;
	}





	public String getCertificateId() { return certificateId; }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public void setId(Long id) { this.id = id; }
}