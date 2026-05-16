package com.BloodBank.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.BloodBank.DTO.BloodRequestrequestDTO;
import com.BloodBank.DTO.BloodrequestResponseDTO;
import com.BloodBank.DTO.RequestPendingDTO;
import com.BloodBank.Service.BloodRequestService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/blood-request")
public class BloodRequestController {

    @Autowired
    private BloodRequestService ser;

    // ✅ Create Blood Request
    @PostMapping("/create")
    public ResponseEntity<BloodrequestResponseDTO> create(
           @Valid @RequestBody BloodRequestrequestDTO request) {

        BloodrequestResponseDTO res = ser.createbloodrequest(request);
        return ResponseEntity.ok(res);
    }
    
    @GetMapping("/pending")
    public List<RequestPendingDTO> getPendingRequests() {
        return ser.getPendingRequests();
    }
}