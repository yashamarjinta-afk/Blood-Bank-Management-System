package com.BloodBank.Service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.BloodBank.DTO.LoginRequestDTO;
import com.BloodBank.DTO.LoginResponseDTO;
import com.BloodBank.DTO.RegistrationRequestDTO;
import com.BloodBank.DTO.RegistrationResponseDTO;
import com.BloodBank.Entity.User;
import com.BloodBank.Entity.User.Role;
import com.BloodBank.Repository.Userrepository;
import com.BloodBank.ServiceInterface.UserServiceInterface;

@Service
public class UserService implements UserServiceInterface {

    @Autowired
    private Userrepository repo;

    @Autowired
    private EmailService ser;

    @Autowired
    private BCryptPasswordEncoder encoder;

    // ✅ Common email normalization
    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new RuntimeException("Email is required");
        }
        return email.toLowerCase().trim();
    }

    // ================= REGISTER =================
    @Override
    public RegistrationResponseDTO register(RegistrationRequestDTO request) {

        RegistrationResponseDTO response = new RegistrationResponseDTO();

        String email = normalizeEmail(request.getEmail());
        
       

        Optional<User> existingUser = repo.findByEmail(email);

        // ✅ Encode image
        String base64Image = null;
        try {
            if (request.getImage() != null && !request.getImage().isEmpty()) {
                base64Image = Base64.getEncoder()
                        .encodeToString(request.getImage().getBytes());
            }
        } catch (Exception e) {
            throw new RuntimeException("Image processing failed");
        }

        // ✅ Role validation
        Role role;
        try {
            role = Role.valueOf(request.getRole().toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Invalid role");
        }

        // ================= EXISTING USER =================
        if (existingUser.isPresent()) {

            User user = existingUser.get();

            // 🔐 Password match
            if (!encoder.matches(request.getPassword(), user.getPassword())) {
                throw new RuntimeException("Invalid credentials");
            }

            // Add role
            if (!user.getRoles().contains(role)) {
                user.getRoles().add(role);
                repo.save(user);
                response.setMessage("Role added successfully");
            } else {
                response.setMessage("User already has this role");
            }

            response.setName(user.getName());
            response.setEmail(user.getEmail());
            response.setBloodGroup(user.getBloodGroup());
            response.setCity(user.getCity());
            response.setRole(role.toString());

            return response;
        }

        // ================= NEW USER =================
        User user = new User();
        user.setName(request.getName());
        user.setEmail(email);
        user.setPassword(encoder.encode(request.getPassword()));

        if (request.getBloodGroup() != null) {
            user.setBloodGroup(request.getBloodGroup().toUpperCase().trim());
        }

        user.setCity(request.getCity());
        user.setImage(base64Image);
        user.getRoles().add(role);

        repo.save(user);

        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setBloodGroup(user.getBloodGroup());
        response.setCity(user.getCity());
        response.setRole(role.toString());
        response.setMessage("User registered successfully");

        return response;
    }

    // ================= LOGIN =================
    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {

        String email = normalizeEmail(request.getEmail());
        
        System.out.println(encoder.encode("sameer07"));

        User user = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        LoginResponseDTO response = new LoginResponseDTO();

        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setImage(user.getImage());
        response.setCity(user.getCity());

        List<String> roles = user.getRoles()
                .stream()
                .map(Enum::name)
                .toList();

        response.setRole(roles);
        response.setMessage("Login successful");

        return response;
    }

    // ================= SEND OTP =================
    @Override
    public String sendemail(String email) {

        String normalizedEmail = normalizeEmail(email);

        User user = repo.findByEmail(normalizedEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int otp = generateotp();

        user.setOtp(otp);
        user.setGenerationtime(System.currentTimeMillis());

        repo.save(user);

        ser.sendto(normalizedEmail, otp);

        return "OTP sent successfully";
    }

    // ================= GENERATE OTP =================
    public int generateotp() {
        Random random = new Random();
        return 100000 + random.nextInt(900000);
    }

    // ================= VERIFY OTP =================
    @Override
    public String verifyOTP(String email, Integer otp) {

        User user = repo.findByEmail(normalizeEmail(email))
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔴 Check OTP exists
        if (user.getOtp() == null) {
            return "OTP expired or not generated";
        }

        // 🔴 Check OTP match
        if (!user.getOtp().equals(otp)) {
            return "Invalid OTP";
        }

        // 🔴 Check time exists
        if (user.getGenerationtime() == null) {
            return "OTP expired";
        }

        long currentTime = System.currentTimeMillis();

        // 🔴 Check expiry (5 min)
        if ((currentTime - user.getGenerationtime()) > 300000) {
            return "OTP expired";
        }

        // ✅ Success → clear OTP
        user.setOtp(null);
        user.setGenerationtime(null);

        repo.save(user);

        return "OTP verified successfully";
    }
    // ================= RESET PASSWORD (NO OTP CHECK) =================
    @Override
    public String resetpassword(String email, String newpassword) {

        User user = repo.findByEmail(normalizeEmail(email))
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (newpassword == null || newpassword.length() < 6) {
            throw new RuntimeException("Password must be at least 6 characters");
        }

        user.setPassword(encoder.encode(newpassword));

        // Optional: clear OTP if exists
        user.setOtp(null);
        user.setGenerationtime(null);

        repo.save(user);

        return "Password updated successfully";
    }
}
