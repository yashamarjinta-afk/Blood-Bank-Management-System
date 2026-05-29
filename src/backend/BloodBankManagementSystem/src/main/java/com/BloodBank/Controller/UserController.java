package com.BloodBank.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.BloodBank.DTO.LoginRequestDTO;
import com.BloodBank.DTO.LoginResponseDTO;
import com.BloodBank.DTO.RegistrationRequestDTO;
import com.BloodBank.DTO.RegistrationResponseDTO;
import com.BloodBank.Service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService ser;

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponseDTO> register(
           @Valid @ModelAttribute RegistrationRequestDTO request) {

        RegistrationResponseDTO res = ser.register(request);
        return ResponseEntity.ok(res);
    }

    // ================= LOGIN =================
  @PostMapping(value = "/login", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<LoginResponseDTO> login(
        @RequestParam("email") String email,
        @RequestParam("password") String password) {

    LoginRequestDTO request = new LoginRequestDTO();
    request.setEmail(email);
    request.setPassword(password);

    LoginResponseDTO res = ser.login(request);

    return ResponseEntity.ok(res);
}

    // ================= SEND OTP =================
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {

        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body("Email is required");
        }

        String res = ser.sendemail(email.trim().toLowerCase());
        return ResponseEntity.ok(res);
    }

    // ================= VERIFY OTP =================
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(
            @RequestParam String email,
            @RequestParam Integer otp) {

        if (email == null || email.isBlank() || otp == null) {
            return ResponseEntity.badRequest().body("Email and OTP are required");
        }

        String res = ser.verifyOTP(email.trim().toLowerCase(), otp);
        return ResponseEntity.ok(res);
    }

    // ================= RESET PASSWORD =================
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String email,
            @RequestParam String newpassword) {

        if (email == null || email.isBlank() || newpassword == null || newpassword.isBlank()) {
            return ResponseEntity.badRequest().body("Email and password are required");
        }

        String res = ser.resetpassword(email.trim().toLowerCase(), newpassword);
        return ResponseEntity.ok(res);
    }
}