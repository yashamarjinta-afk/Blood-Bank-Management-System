package com.BloodBank.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.BloodBank.DTO.BloodRequestrequestDTO;
import com.BloodBank.DTO.BloodrequestResponseDTO;
import com.BloodBank.DTO.RequestPendingDTO;
import com.BloodBank.Entity.BloodRequest;
import com.BloodBank.Entity.BloodRequest.Status;
import com.BloodBank.Entity.User;
import com.BloodBank.Repository.BloodRequestRepository;
import com.BloodBank.Repository.Userrepository;
import com.BloodBank.ServiceInterface.BloodRequestserviceInterface;

@Service
public class BloodRequestService implements BloodRequestserviceInterface {

    @Autowired
    private BloodRequestRepository requestRepo;

    @Autowired
    private Userrepository userRepo;

    // ✅ Email normalization (important for consistency)
    private String normalizeEmail(String email) {
        return email.toLowerCase().trim();
    }

    @Override
    public BloodrequestResponseDTO createbloodrequest(BloodRequestrequestDTO dto) {

        String email = normalizeEmail(dto.getEmail());

        User hospital = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ❗ Role validation
        if (!hospital.getRoles().contains(User.Role.HOSPITAL)) {
            throw new RuntimeException("Only hospitals can request blood");
        }

        // ❗ Basic validation
        if (dto.getUnits() <= 0) {
            throw new RuntimeException("Units must be greater than 0");
        }

        if (dto.getBloodGroup() == null || dto.getBloodGroup().isBlank()) {
            throw new RuntimeException("Blood group is required");
        }

        if (dto.getCity() == null || dto.getCity().isBlank()) {
            throw new RuntimeException("City is required");
        }

        // ✅ Create request
        BloodRequest req = new BloodRequest();

        req.setBloodGroup(dto.getBloodGroup().toUpperCase().trim());
        req.setUnits(dto.getUnits());
        req.setCity(dto.getCity().trim());
        req.setStatus(BloodRequest.Status.PENDING);
        req.setHospital(hospital);

        requestRepo.save(req);

        // ✅ Response
        BloodrequestResponseDTO res = new BloodrequestResponseDTO();
        res.setId(req.getId());
        res.setHospitalname(hospital.getName());
        res.setBloodGroup(req.getBloodGroup());
        res.setUnits(req.getUnits());
        res.setCity(req.getCity());
        res.setStatus("PENDING");
        res.setMessage("Blood request submitted successfully");

        return res;
    }
    
    public List<RequestPendingDTO> getPendingRequests() {

        List<BloodRequest> requests = requestRepo.findByStatusAllIgnoreCase(Status.PENDING);

        return requests.stream()
                .map(r -> new RequestPendingDTO(
                		r.getId(),
                        r.getBloodGroup(),
                        r.getUnits(),
                        r.getStatus().name()
                ))
                .toList();
    }
}