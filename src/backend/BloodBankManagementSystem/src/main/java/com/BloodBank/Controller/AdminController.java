package com.BloodBank.Controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.BloodBank.DTO.*;
import com.BloodBank.Entity.Camp;
import com.BloodBank.Entity.StorageCenter;
import com.BloodBank.Entity.User;
import com.BloodBank.Repository.CampRepository;
import com.BloodBank.Repository.StorageCenterRepository;
import com.BloodBank.Repository.Userrepository;
import com.BloodBank.Service.AdminService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService ser;
    
    @Autowired
    private Userrepository repo;
    
    @Autowired
    private CampRepository camprepo;
    
    @Autowired
    private StorageCenterRepository storerepo;

    // ================= ORGANIZER =================
    @PostMapping("/organizers")
    public ResponseEntity<OrganizerResponseDTO> createOrganizer(@Valid @RequestBody OrganizerRequestDTO request) {
        return ResponseEntity.ok(ser.create(request));
    }

    // ================= CAMP =================
    @PostMapping("/camps")
    public ResponseEntity<CampResponseDTO> createCamp(@Valid @RequestBody CampRequestDTO request) {
        return ResponseEntity.ok(ser.createcamp(request));
    }

    @GetMapping("/camps")
    public ResponseEntity<List<CampListResponseDTO>> getCampsByLocation(@RequestParam String location) {
        return ResponseEntity.ok(ser.findbylocation(location));
    }

    @GetMapping("/camps/{id}")
    public ResponseEntity<CampResponseDTO> getCampById(@PathVariable Long id) {
        return ResponseEntity.ok(ser.findbyid(id));
    }

    @DeleteMapping("/camps/{id}")
    public ResponseEntity<DeleteCampResponseDTO> deleteCamp(@PathVariable Long id) {
        return ResponseEntity.ok(ser.deletebyid(id));
    }

    // ================= DONATION =================
    @PutMapping("/donations/{id}/verify")
    public ResponseEntity<String> verifyDonation(
            @PathVariable Long id,
            @RequestParam String email) {

        return ResponseEntity.ok(ser.verifyDonation(id, email));
    }

    @PutMapping("/donations/{id}/reject")
    public ResponseEntity<String> rejectDonation(
            @PathVariable Long id,
            @RequestParam String email) {

        return ResponseEntity.ok(ser.rejectDonation(id, email));
    }

    @PutMapping("/donations/approve")
    public ResponseEntity<approveResponseDTO> approveDonation(
           @Valid @RequestBody approveRequestDTO request) {

        return ResponseEntity.ok(ser.approved(request));
    }

    // ================= STORAGE =================
    @PostMapping("/storage-centers")
    public ResponseEntity<StorageCenterResponseDTO> createStorage(
           @Valid @RequestBody StorageCenterRequestDTO request) {

        return ResponseEntity.ok(ser.create(request));
    }

    @GetMapping("/storage-centers")
    public ResponseEntity<List<StorageCenterResponseDTO>> getAllStorage() {
        return ResponseEntity.ok(ser.getAll());
    }

    // ================= BLOOD REQUEST =================
    @PutMapping("/blood-requests/approve")
    public ResponseEntity<BloodrequestResponseDTO> approveBloodRequest(
            @RequestBody ApproveBloodRequestDTO dto) {

        return ResponseEntity.ok(ser.approve(dto));
    }

    @PutMapping("/blood-requests/{id}/reject")
    public ResponseEntity<BloodrequestResponseDTO> rejectBloodRequest(
            @PathVariable Long id) {

        return ResponseEntity.ok(ser.reject(id));
    }

    @GetMapping("/blood-requests")
    public ResponseEntity<List<BloodrequestResponseDTO>> getByHospital(
            @RequestParam String email) {

        return ResponseEntity.ok(ser.getByHospital(email));
    }

    // ================= USERS =================
    @GetMapping("/users")
    public ResponseEntity<List<FetchUserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(ser.getAllUsers());
    }
    
    @GetMapping("/getallorganizers")
    public ResponseEntity<List<FetchUserResponseDTO>> getAllOrganizers() {
        return ResponseEntity.ok(ser.getAllOrganizers());
    }
    
    @GetMapping("/getallcamps")
    public ResponseEntity<List<CampResponseDTO>> getAllCamps() {
        return ResponseEntity.ok(ser.getAllCamps());
    }
    
    @PutMapping("/updateorganizers/{id}")
    public ResponseEntity<?> updateOrganizer(@PathVariable Long id,
                                             @RequestBody OrganizerRequestDTO dto) {

        User user = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setCity(dto.getCity());
        user.setBloodGroup(dto.getBloodGroup());

        repo.save(user);

        return ResponseEntity.ok("Updated successfully");
    }
    
    @DeleteMapping("/deleteorganizers/{id}")
    public ResponseEntity<?> deleteOrganizer(@PathVariable Long id) {

        repo.deleteById(id);

        return ResponseEntity.ok("Deleted successfully");
    }
    
    @PutMapping("/updatecamps/{id}")
    public ResponseEntity<?> updateCamp(@PathVariable Long id,
                                        @RequestBody CampRequestDTO dto) {

        Camp camp = camprepo.findById(id).orElseThrow();
        User organizer = repo.findById(dto.getOrganizerId())
                .orElseThrow(() -> new RuntimeException("Organizer not found"));
        LocalDate today = LocalDate.now();
        LocalDate campDate = dto.getCampDate();

        Camp.Status status;

        if (campDate.isAfter(today)) {
            status = Camp.Status.UPCOMING;
        } else if (campDate.isEqual(today)) {
            status = Camp.Status.ONGOING;
        } else {
            status = Camp.Status.COMPLETED;
        }

        camp.setStatus(status);
        camp.setCampName(dto.getCampName());
        camp.setLocation(dto.getLocation());
        camp.setAddress(dto.getAddress());
        camp.setCampDate(dto.getCampDate());
        camp.setOrganizer(organizer);
        camprepo.save(camp);

        return ResponseEntity.ok("Updated");
    }
    
    @PutMapping("/updatestoragecenter/{id}")
    public ResponseEntity<?> updateStorage(@PathVariable Long id,
                                           @Valid @RequestBody StorageCenterRequestDTO dto) {

        StorageCenter s = storerepo.findById(id).orElseThrow();

        s.setName(dto.getName());
        s.setCity(dto.getCity());
        s.setAddress(dto.getAddress());
        s.setContact(dto.getContact());

        storerepo.save(s);

        return ResponseEntity.ok("Updated");
    }
    
    @DeleteMapping("/deletestoragecenter/{id}")
    public ResponseEntity<?> deleteStorage(@PathVariable Long id) {
        storerepo.deleteById(id);
        return ResponseEntity.ok("Deleted");
    }
   
    @GetMapping("/camps/search")
    public ResponseEntity<List<CampListResponseDTO>> searchByLocation(
            @RequestParam String location) {

        return ResponseEntity.ok(ser.findbylocation(location));
    }
    
    @GetMapping("/organizer/camps")
    public ResponseEntity<List<CampResponseDTO>> getOrganizerCamps(
            @RequestParam String email) {

        return ResponseEntity.ok(
                ser.getCampsByOrganizerEmail(email)
        );
    }
}