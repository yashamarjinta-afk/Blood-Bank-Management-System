package com.BloodBank.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.BloodBank.DTO.DonationHistoryDTO;
import com.BloodBank.DTO.DonationRequestDTO;
import com.BloodBank.DTO.DonationResponseDTO;
import com.BloodBank.DTO.DonationpendingDto;
import com.BloodBank.DTO.OrganizerDonationDTO;
import com.BloodBank.DTO.pendingResponseDTO;
import com.BloodBank.Entity.Donation;
import com.BloodBank.Service.Donationservice;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/donation")
public class DonationController {

    @Autowired
    private Donationservice ser;

    // ================= DONATE =================
    @PostMapping("/donate")
    public ResponseEntity<DonationResponseDTO> donate(
           @Valid @RequestBody DonationRequestDTO request,
           @Valid  @RequestParam String email) {

        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        DonationResponseDTO res = ser.donate(request, email.trim().toLowerCase());
        return ResponseEntity.ok(res);
    }

    // ================= GET PENDING DONATIONS =================
    @GetMapping("/pending")
    public ResponseEntity<List<pendingResponseDTO>> getPending() {

        List<pendingResponseDTO> res = ser.getpending();
        return ResponseEntity.ok(res);
    }

    // ================= DONATION HISTORY =================
    @GetMapping("/history")
    public ResponseEntity<List<DonationHistoryDTO>> history(
            @RequestParam String email) {

        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        List<DonationHistoryDTO> res = ser.history(email.trim().toLowerCase());
        return ResponseEntity.ok(res);
    }
    @GetMapping("/organizer/donors/pending/{campId}")
    public ResponseEntity<List<OrganizerDonationDTO>> getPendingDonations(
            @PathVariable Long campId) {

        return ResponseEntity.ok(
                ser.getPendingDonationsByCamp(campId)
        );
    }
    @GetMapping("/status")
    public ResponseEntity<List<DonationpendingDto>> getByStatus(
            ) {

        return ResponseEntity.ok(
                ser.getpendingdonation()
        );
    }
}