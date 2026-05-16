package com.BloodBank.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.BloodBank.DTO.DonationHistoryDTO;
import com.BloodBank.DTO.DonationRequestDTO;
import com.BloodBank.DTO.DonationResponseDTO;
import com.BloodBank.DTO.DonationpendingDto;
import com.BloodBank.DTO.OrganizerDonationDTO;
import com.BloodBank.DTO.pendingResponseDTO;
import com.BloodBank.Entity.Camp;
import com.BloodBank.Entity.Donation;
import com.BloodBank.Entity.Donation.DonationStatus;
import com.BloodBank.Entity.Donation.OrganizerStatus;
import com.BloodBank.Entity.User;
import com.BloodBank.Entity.User.Role;
import com.BloodBank.Repository.CampRepository;
import com.BloodBank.Repository.DonationRepository;
import com.BloodBank.Repository.Userrepository;
import com.BloodBank.ServiceInterface.DonationServiceinterface;

@Service
public class Donationservice implements DonationServiceinterface {

    @Autowired
    private DonationRepository donationrepo;

    @Autowired
    private Userrepository userrepo;

    @Autowired
    private CampRepository camprepo;

    // ================= HELPER =================
    private String normalizeEmail(String email) {
        return email.toLowerCase().trim();
    }

    // ================= DONATE =================
    @Override
    public DonationResponseDTO donate(DonationRequestDTO request, String email) {

        String userEmail = normalizeEmail(email);

        // ❗ Validate input
        if (request.getUnits() == null || request.getUnits() <= 0) {
            throw new RuntimeException("Units must be greater than 0");
        }

        if (request.getLocation() == null || request.getLocation().isBlank()) {
            throw new RuntimeException("Location is required");
        }

        // 🔹 Fetch user
        User user = userrepo.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔹 Validate blood group
        if (user.getBloodGroup() == null) {
            throw new RuntimeException("User blood group not set");
        }

        // 🔹 Fetch camp
        Camp camp = camprepo.findById(request.getCampId())
                .orElseThrow(() -> new RuntimeException("Camp not found"));

        // ✅ Check camp status
        if (camp.getStatus() != Camp.Status.UPCOMING &&
            camp.getStatus() != Camp.Status.ONGOING) {
            throw new RuntimeException("Cannot donate to this camp");
        }

        // ✅ Check role
        if (!user.getRoles().contains(Role.DONOR)) {
            throw new RuntimeException("Only donors can donate");
        }

        // ✅ Prevent duplicate donation in same camp
        if (donationrepo.existsByUserAndCamp(user, camp)) {
            throw new RuntimeException("Already donated in this camp");
        }

        // ================= 3-MONTH RULE =================
        LocalDate today = LocalDate.now();

        donationrepo.findTopByUserEmailOrderByDateDesc(userEmail)
                .ifPresent(last -> {
                    long days = ChronoUnit.DAYS.between(last.getDate(), today);

                    if (days < 90) {
                        LocalDate nextDate = last.getDate().plusMonths(3);

                        throw new RuntimeException(
                                "You can donate after: " + nextDate);
                    }
                });

        // ================= CREATE DONATION =================
        Donation donation = new Donation();
        donation.setUser(user);
        donation.setCamp(camp);
        donation.setBloodGroup(user.getBloodGroup().toUpperCase().trim());
        donation.setLocation(request.getLocation().trim());
        donation.setDate(today);
        donation.setUnits(request.getUnits());
        donation.setStatus(DonationStatus.PENDING);
        donation.setOrganizerStatus(OrganizerStatus.PENDING);

        donationrepo.save(donation);

        // ================= RESPONSE =================
        DonationResponseDTO res = new DonationResponseDTO();
        res.setBloodGroup(donation.getBloodGroup());
        res.setLocation(donation.getLocation());
        res.setDate(donation.getDate());
        res.setUnits(donation.getUnits());
        res.setStatus(donation.getStatus());
        res.setMessage("Donation request sent for approval");

        return res;
    }

    // ================= GET PENDING =================
    @Override
    public List<pendingResponseDTO> getpending() {

        return donationrepo.findByStatus(DonationStatus.PENDING)
                .stream()
                .map(d -> new pendingResponseDTO(
                        d.getId(),
                        d.getUser().getName(),
                        d.getUser().getBloodGroup(),
                        d.getDate()
                ))
                .toList();
    }

    // ================= HISTORY =================
    @Override
    public List<DonationHistoryDTO> history(String email) {

        String userEmail = normalizeEmail(email);

        List<Donation> donations =
                donationrepo.findByUserEmailOrderByDateDesc(userEmail);

        return donations.stream()
                .map(d -> new DonationHistoryDTO(
                        d.getId(),
                        d.getBloodGroup(),
                        d.getDate(),
                        d.getStatus().name()
                ))
                .toList();
    }
    
    public List<OrganizerDonationDTO> getPendingDonationsByCamp(Long campId) {

        List<Donation> donations =
                donationrepo.findByCampIdAndStatus(campId, DonationStatus.PENDING);

        return donations.stream()
                .map(d -> new OrganizerDonationDTO(
                        d.getId(),
                        d.getUser().getName(),
                        d.getUser().getEmail(),
                        d.getBloodGroup(),
                        d.getUnits(),
                        d.getCamp().getCampName(),
                        d.getCamp().getLocation(),
                        d.getStatus().name()
                ))
                .toList();
    }
    
    public List<DonationpendingDto> getpendingdonation(Donation.DonationStatus status){
    	
    	List<Donation> donations=donationrepo.findByStatus(status);

        return donations.stream()
                .map(d -> new DonationpendingDto(
                        d.getId(),
                        d.getUser().getName(),
                        d.getBloodGroup(),
                        d.getUnits(),
                        d.getStatus().name()
                ))
                .toList();
    }
    
    public List<DonationpendingDto> getpendingdonation() {

        List<Donation> donations =
                donationrepo.findByStatusAndOrganizerStatus(
                        DonationStatus.PENDING,
                        OrganizerStatus.PENDING
                );
       

        return donations.stream()
                .map(d -> new DonationpendingDto(
                        d.getId(),
                        d.getUser().getName(),
                        d.getBloodGroup(),
                        d.getUnits(),
                        d.getStatus().name()
                       
                ))
                .toList();
        
    }

}