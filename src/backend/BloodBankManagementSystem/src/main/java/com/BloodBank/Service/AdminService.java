package com.BloodBank.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.BloodBank.DTO.*;
import com.BloodBank.Entity.*;
import com.BloodBank.Entity.Donation.DonationStatus;
import com.BloodBank.Entity.Donation.OrganizerStatus;
import com.BloodBank.Entity.User.Role;
import com.BloodBank.Repository.*;
import com.BloodBank.ServiceInterface.AdminServiceInterface;

@Service
public class AdminService implements AdminServiceInterface {

    @Autowired
    private Userrepository repo;

    @Autowired
    private CampRepository camprepo;

    @Autowired
    private DonationRepository donationrepo;

    @Autowired
    private StorageCenterRepository storagerepo;

    @Autowired
    private BloodStockRepository stockRepo;

    @Autowired
    private BloodRequestRepository requestRepo;

    @Autowired
    private EmailService emailser;

    @Autowired
    private CertificateService certificateser;

    @Autowired
    private BCryptPasswordEncoder encoder;

    // 🔹 Helper method
    private String normalizeEmail(String email) {
        return email.toLowerCase().trim();
    }

    // ================= ORGANIZER =================
    @Override
    public OrganizerResponseDTO create(OrganizerRequestDTO request) {

        String email = normalizeEmail(request.getEmail());

        Optional<User> existingUser = repo.findByEmail(email);

        if (existingUser.isPresent()) {
            User user = existingUser.get();

            if (user.getRoles().contains(Role.ORGANIZER)) {
                throw new RuntimeException("Organizer already exists");
            }

            throw new RuntimeException("User already exists with this email");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(email);
        user.setPassword(encoder.encode(request.getPassword()));
        user.setCity(request.getCity());
        user.setBloodGroup(request.getBloodGroup());

        user.getRoles().add(Role.ORGANIZER);

        repo.save(user);

        OrganizerResponseDTO res = new OrganizerResponseDTO();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setCity(user.getCity());
        res.setMessage("Organizer created successfully");

        return res;
    }

    // ================= CAMP =================
    @Override
    public CampResponseDTO createcamp(CampRequestDTO request) {

        User organizer = repo.findById(request.getOrganizerId())
                .orElseThrow(() -> new RuntimeException("Organizer not found"));

        if (!organizer.getRoles().contains(Role.ORGANIZER)) {
            throw new RuntimeException("User is not an organizer");
        }

        String campName = request.getCampName().trim();
        String address = request.getAddress().trim();

        if (camprepo.existsByCampNameIgnoreCaseAndAddressIgnoreCase(campName, address)) {
            throw new RuntimeException("Camp already exists at this location");
        }

        LocalDate today = LocalDate.now();
        LocalDate campDate = request.getCampDate();

        Camp.Status status;

        if (campDate.isAfter(today)) {
            status = Camp.Status.UPCOMING;
        } else if (campDate.isEqual(today)) {
            status = Camp.Status.ONGOING;
        } else {
            status = Camp.Status.COMPLETED;
        }

        

        Camp camp = new Camp();
        camp.setCampName(campName);
        camp.setLocation(request.getLocation().trim());
        camp.setCampDate(request.getCampDate());
        camp.setDescription(request.getDescription());
        camp.setContactNumber(request.getContactNumber());
        camp.setAddress(address);
        camp.setStatus(status);
        camp.setOrganizer(organizer);

        camprepo.save(camp);

        CampResponseDTO res = new CampResponseDTO();
        res.setId(camp.getId());
        res.setCampName(camp.getCampName());
        res.setLocation(camp.getLocation());
        res.setOrganizerName(organizer.getName());
        res.setStatus(getStatus(camp.getCampDate()).name());
        res.setCampDate(camp.getCampDate());
        res.setDescription(camp.getDescription());
        res.setContactNumber(camp.getContactNumber());
        res.setOrganizerId(organizer.getId());
        res.setAddress(camp.getAddress());
        res.setMessage("Camp created successfully");

        return res;
    }

    // ================= VERIFY DONATION =================
    public String verifyDonation(Long donationId, String organizerEmail) {

        String email = normalizeEmail(organizerEmail);

        Donation donation = donationrepo.findById(donationId)
                .orElseThrow(() -> new RuntimeException("Donation not found"));

        if (!donation.getCamp().getOrganizer().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized");
        }

        if (donation.getOrganizerStatus() == OrganizerStatus.VERIFIED) {
            return "Already verified";
        }

        donation.setOrganizerStatus(OrganizerStatus.VERIFIED);
        donation.setStatus(DonationStatus.PENDING);

        donationrepo.save(donation);

        return "Donation verified";
    }

    // ================= APPROVE DONATION =================
    public approveResponseDTO approved(approveRequestDTO request) {

        Donation donation = donationrepo.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Donation not found"));

        if (donation.getStatus() != DonationStatus.PENDING) {
            throw new RuntimeException("Already processed");
        }

        if (donation.getOrganizerStatus() != OrganizerStatus.VERIFIED) {
            throw new RuntimeException("Not verified by organizer");
        }

        StorageCenter center = storagerepo.findById(request.getStorageCenterId())
                .orElseThrow(() -> new RuntimeException("Center not found"));

        donation.setStatus(DonationStatus.APPROVED);
        donationrepo.save(donation);

        if (stockRepo.existsByDonation(donation)) {
            throw new RuntimeException("Stock already created");
        }

        BloodStock stock = new BloodStock();
        stock.setBloodGroup(donation.getBloodGroup().toUpperCase().trim());
        stock.setUnits(donation.getUnits());
        stock.setStoredDate(LocalDate.now());
        stock.setExpiryDate(LocalDate.now().plusDays(42));
        stock.setDonation(donation);
        stock.setStorageCenter(center);

        stockRepo.save(stock);

        try {
            byte[] pdf = certificateser.generateCertificate(
                    donation.getUser().getName(),
                    donation.getUser().getBloodGroup(),
                    donation.getDate()
            );

            emailser.sendemail(donation.getUser().getEmail(), pdf);
        } catch (Exception e) {
            System.out.println("Email failed");
        }

        approveResponseDTO res = new approveResponseDTO();
        res.setId(donation.getId());
        res.setMessage("Approved and stored at " + center.getName());

        return res;
    }
    
    public String rejectDonation(Long donationId, String organizerEmail) {

        String email = normalizeEmail(organizerEmail);

        Donation donation = donationrepo.findById(donationId)
                .orElseThrow(() -> new RuntimeException("Donation not found"));

        // ✅ Check ownership
        if (!donation.getCamp().getOrganizer().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized");
        }

        // ❗ Already rejected
        if (donation.getOrganizerStatus() == OrganizerStatus.REJECTED) {
            return "Already rejected";
        }

        // ❗ Already verified → cannot reject
        if (donation.getOrganizerStatus() == OrganizerStatus.VERIFIED) {
            throw new RuntimeException("Already verified. Cannot reject");
        }

        // ✅ Reject
        donation.setOrganizerStatus(OrganizerStatus.REJECTED);
        donation.setStatus(DonationStatus.REJECTED);

        donationrepo.save(donation);

        return "Donation rejected successfully";
    }

    // ================= APPROVE BLOOD REQUEST =================
    @Transactional
    public BloodrequestResponseDTO approve(ApproveBloodRequestDTO dto) {

        BloodRequest req = requestRepo.findById(dto.getRequestId())
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (req.getStatus() != BloodRequest.Status.PENDING) {
            throw new RuntimeException("Already processed");
        }

        StorageCenter center = storagerepo.findById(dto.getCenterId())
                .orElseThrow(() -> new RuntimeException("Center not found"));

        // 🔥 Get stock ONLY from selected center
        List<BloodStock> stockList = stockRepo
                .findByBloodGroupAndStorageCenter_IdAndExpiryDateAfter(
                        req.getBloodGroup(),
                        center.getId(),
                        LocalDate.now()
                );

        if (stockList.isEmpty()) {
            throw new RuntimeException("No stock available in selected center");
        }

        // FIFO
        stockList.sort((a, b) -> a.getExpiryDate().compareTo(b.getExpiryDate()));

        int required = req.getUnits();

        for (BloodStock stock : stockList) {

            if (required <= 0) break;

            int available = stock.getUnits();

            if (available <= required) {
                required -= available;
                stock.setUnits(0);
            } else {
                stock.setUnits(available - required);
                required = 0;
            }

            stockRepo.save(stock);
        }

        if (required > 0) {
            throw new RuntimeException("Not enough stock in selected center");
        }

        // ✅ ASSIGN SELECTED CENTER
        req.setAssignedCenter(center);

        // ✅ UPDATE STATUS
        req.setStatus(BloodRequest.Status.APPROVED);

        requestRepo.save(req);

        // ✅ RESPONSE
        BloodrequestResponseDTO res = new BloodrequestResponseDTO();
        res.setId(req.getId());
        res.setHospitalname(req.getHospital().getName());
        res.setStatus("APPROVED");
        res.setCenterName(center.getName());
        res.setAddress(center.getAddress());
        res.setContact(center.getContact());
        res.setMessage("Approved & Center Assigned");

        return res;
    }

    @Override
    public List<CampListResponseDTO> findbylocation(String location) {

        String loc = location.trim();

        List<Camp> camps = camprepo.findByLocationIgnoreCase(loc);

        if (camps.isEmpty()) {
            throw new RuntimeException("No camps found in this location");
        }

        if (location == null || location.trim().isEmpty()) {
            throw new RuntimeException("Location is required");
        }
        List<CampListResponseDTO> responseList = new ArrayList<>();

        for (Camp camp : camps) {

            CampListResponseDTO dto = new CampListResponseDTO();

            dto.setId(camp.getId());
            dto.setCampName(camp.getCampName());
            dto.setLocation(camp.getLocation());
            dto.setCampDate(camp.getCampDate());
            dto.setAddress(camp.getAddress());
            dto.setOrganizerName(camp.getOrganizer().getName());
            dto.setStatus(getStatus(camp.getCampDate()).name());

            responseList.add(dto);
        }

        return responseList;
    }

    @Override
    public CampResponseDTO findbyid(Long id) {

        Camp camp = camprepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Camp not found"));

        CampResponseDTO response = new CampResponseDTO();

        response.setId(camp.getId());
        response.setCampName(camp.getCampName());
        response.setLocation(camp.getLocation());
        response.setCampDate(camp.getCampDate());
        response.setAddress(camp.getAddress());
        response.setDescription(camp.getDescription());
        response.setContactNumber(camp.getContactNumber());
        response.setStatus(getStatus(camp.getCampDate()).name());

        if (camp.getOrganizer() != null) {
            response.setOrganizerName(camp.getOrganizer().getName());
            response.setOrganizerId(camp.getOrganizer().getId());
        }

        response.setMessage("Camp fetched successfully");

        return response;
    }

    @Override
    public DeleteCampResponseDTO deletebyid(Long id) {

        Camp camp = camprepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Camp not found"));

        // ❗ Prevent deletion if donations exist
        if (camp.getDonations() != null && !camp.getDonations().isEmpty()) {
            throw new RuntimeException("Cannot delete camp with existing donations");
        }

        camprepo.delete(camp);

        DeleteCampResponseDTO response = new DeleteCampResponseDTO();
        response.setMessage("Camp deleted successfully");

        return response;
    }

    @Override
    public StorageCenterResponseDTO create(StorageCenterRequestDTO request) {

        String name = request.getName().trim();
        String address = request.getAddress().trim();

        // ❗ Duplicate check
        if (storagerepo.existsByNameIgnoreCaseAndAddressIgnoreCase(name, address)) {
            throw new RuntimeException("Storage center already exists at this address");
        }

        StorageCenter center = new StorageCenter();

        center.setName(name);
        center.setCity(request.getCity().trim());
        center.setAddress(address);
        center.setContact(request.getContact());

        storagerepo.save(center);

        StorageCenterResponseDTO res = new StorageCenterResponseDTO();
        res.setId(center.getId());
        res.setName(center.getName());
        res.setCity(center.getCity());
        res.setAddress(center.getAddress());
        res.setContact(center.getContact());
        res.setMessage("Storage Center Created Successfully");

        return res;
    }
    
    public List<StorageCenterResponseDTO> getAll() {

        return storagerepo.findAll().stream().map(c -> {
            StorageCenterResponseDTO dto = new StorageCenterResponseDTO();
            dto.setId(c.getId());
            dto.setName(c.getName());
            dto.setCity(c.getCity());
            dto.setAddress(c.getAddress());
            dto.setContact(c.getContact());
            return dto;
        }).toList();
    }
    
    public BloodrequestResponseDTO reject(Long id) {

        BloodRequest req = requestRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (req.getStatus() != BloodRequest.Status.PENDING) {
            throw new RuntimeException("Already processed");
        }

        req.setStatus(BloodRequest.Status.REJECTED);
        requestRepo.save(req);

        BloodrequestResponseDTO res = new BloodrequestResponseDTO();
        res.setId(req.getId());
        res.setStatus("REJECTED");
        res.setMessage("Request rejected");

        return res;
    }
    
    public List<BloodrequestResponseDTO> getByHospital(String email) {

        String normalizedEmail = normalizeEmail(email);

        List<BloodRequest> list = requestRepo.findByHospital_Email(normalizedEmail);

        return list.stream().map(req -> {
            BloodrequestResponseDTO dto = new BloodrequestResponseDTO();

            dto.setId(req.getId());
            dto.setHospitalname(req.getHospital().getName());
            dto.setBloodGroup(req.getBloodGroup());
            dto.setUnits(req.getUnits());
            dto.setCity(req.getCity());
            dto.setStatus(req.getStatus().name());

            return dto;
        }).toList();
    }
    
    public List<FetchUserResponseDTO> getAllUsers() {

        List<User> users = repo.findAll();

        return users.stream().map(user -> {

            FetchUserResponseDTO dto = new FetchUserResponseDTO();

            dto.setId(user.getId());
            dto.setName(user.getName());
            dto.setEmail(user.getEmail());
            dto.setBloodGroup(user.getBloodGroup());
            dto.setCity(user.getCity());

            dto.setRoles(
                    user.getRoles()
                            .stream()
                            .map(role -> role.name())
                            .toList()
            );

            return dto;

        }).toList();
    }
    
    public List<FetchUserResponseDTO> getAllOrganizers() {

        List<User> users = repo.findByRolesContaining(Role.ORGANIZER);

        return users.stream().map(user -> {
            FetchUserResponseDTO dto = new FetchUserResponseDTO();
            dto.setId(user.getId());
            dto.setName(user.getName());
            dto.setEmail(user.getEmail());
            dto.setCity(user.getCity());
            dto.setBloodGroup(user.getBloodGroup());
            dto.setRoles(
                    user.getRoles().stream().map(Enum::name).toList()
            );
            return dto;
        }).toList();
    }
    
    public List<CampResponseDTO> getAllCamps() {

        List<Camp> camps = camprepo.findAll();

        return camps.stream().map(camp -> {
            CampResponseDTO dto = new CampResponseDTO();
            
            

            dto.setId(camp.getId());
            dto.setCampName(camp.getCampName());
            dto.setLocation(camp.getLocation());
            dto.setAddress(camp.getAddress());
            dto.setCampDate(camp.getCampDate());

            // 🔥 THIS IS WHAT YOU MISSED
            dto.setDescription(camp.getDescription());
            dto.setContactNumber(camp.getContactNumber());

            dto.setStatus(getStatus(camp.getCampDate()).name());

            dto.setOrganizerId(camp.getOrganizer().getId());
            dto.setOrganizerName(camp.getOrganizer().getName());

            return dto;
        }).toList();
    }
    
    private Camp.Status getStatus(LocalDate campDate) {
        LocalDate today = LocalDate.now();

        if (campDate.isAfter(today)) return Camp.Status.UPCOMING;
        if (campDate.isEqual(today)) return Camp.Status.ONGOING;
        return Camp.Status.COMPLETED;
    }
    
    public List<CampResponseDTO> getCampsByOrganizerEmail(String email) {

        String organizerEmail = normalizeEmail(email);

        List<Camp> camps = camprepo.findByOrganizerIsNotNullAndOrganizer_EmailIgnoreCase(organizerEmail);

        return camps.stream()
                .map(c -> new CampResponseDTO(
                        c.getCampName(),
                        c.getLocation(),
                        c.getCampDate(),
                        c.getDescription(),
                        c.getId(),
                        c.getStatus().name(),
                        c.getAddress()
                ))
                .toList();
    }
    
    
    
}