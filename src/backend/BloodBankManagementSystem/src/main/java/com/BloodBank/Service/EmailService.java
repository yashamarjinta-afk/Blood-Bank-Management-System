package com.BloodBank.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.BloodBank.Entity.User;
import com.BloodBank.Repository.Userrepository;
import com.BloodBank.ServiceInterface.EmailServiceinterface;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService implements EmailServiceinterface {

    @Autowired
    private Userrepository repo;

    @Autowired
    private JavaMailSender mailSender;

    // 🔹 Normalize email
    private String normalizeEmail(String email) {
        return email.toLowerCase().trim();
    }

    // ================= CERTIFICATE EMAIL =================
    @Override
    public String sendemail(String email, byte[] pdf) {

        try {
            String normalizedEmail = normalizeEmail(email);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(normalizedEmail);
            helper.setSubject("Blood Donation Certificate 🩸");
            helper.setText("Thank you for your donation. Please find your certificate attached.");

            helper.addAttachment("Certificate.pdf", new ByteArrayResource(pdf));

            mailSender.send(message);

            return "Email sent successfully";

        } catch (Exception e) {
            throw new RuntimeException("Email sending failed: " + e.getMessage());
        }
    }

    // ================= REJECT EMAIL =================
    @Override
    public String rejectemail(String email) {

        try {
            String normalizedEmail = normalizeEmail(email);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(normalizedEmail);
            helper.setSubject("Blood Donation Rejected ❌");
            helper.setText("Sorry, your donation request was not approved.");

            mailSender.send(message);

            return "Email sent successfully";

        } catch (Exception e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    // ================= BLOOD REQUEST APPROVAL EMAIL =================
    public void sendApprovalMail(String to, String name,
                                 String centerName,
                                 String address,
                                 String contact) {

        try {
            String normalizedEmail = normalizeEmail(to);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(normalizedEmail);
            helper.setSubject("Blood Request Approved 🩸");

            String html = buildApprovalEmail(name, centerName, address, contact);

            helper.setText(html, true); // HTML enabled

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Email sending failed: " + e.getMessage());
        }
    }

    // ================= HTML TEMPLATE =================
    private String buildApprovalEmail(String name, String centerName,
                                      String address, String contact) {

        return """
        <!DOCTYPE html>
        <html>
        <body style='margin:0;padding:0;background:#f4f6f8;font-family:Arial;'>

        <table width='100%' cellpadding='0' cellspacing='0'>
        <tr><td align='center'>

        <table width='600' style='background:#ffffff;margin-top:30px;border-radius:12px;overflow:hidden;'>

        <tr>
        <td style='background:#d32f2f;padding:20px;text-align:center;'>
        <h2 style='color:white;'>Blood Bank System</h2>
        </td>
        </tr>

        <tr><td style='padding:30px;'>

        <h3>Hello %s,</h3>

        <p>Your blood request has been <b style='color:green;'>approved</b>.</p>

        <div style='background:#f9f9f9;padding:15px;border-radius:8px;'>
        <h4>Collection Details</h4>

        <p><b>Center:</b> %s</p>
        <p><b>Address:</b> %s</p>
        <p><b>Contact:</b> %s</p>
        </div>

        <p style='margin-top:20px;'>Please visit with valid ID.</p>

        <p style='color:#777;'>Thank you ❤️</p>

        </td></tr>

        <tr>
        <td style='background:#eeeeee;text-align:center;padding:15px;font-size:12px;color:#777;'>
        © 2026 Blood Bank System
        </td>
        </tr>

        </table>

        </td></tr>
        </table>

        </body>
        </html>
        """.formatted(name, centerName, address, contact);
    }

    // ================= OTP EMAIL =================
    public void sendto(String email, int otp) {

        try {
            String normalizedEmail = normalizeEmail(email);

            Optional<User> optionalUser = repo.findByEmail(normalizedEmail);

            if (optionalUser.isEmpty()) {
                throw new RuntimeException("User not found");
            }

            User user = optionalUser.get();

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(normalizedEmail);
            helper.setSubject("Password Reset OTP 🔐");

            String html = """
                    <div style='font-family:Arial'>
                        <h2>Hello %s</h2>
                        <p>Your OTP is:</p>
                        <h1 style='color:blue;'>%d</h1>
                        <p>Do not share this OTP with anyone.</p>
                    </div>
                    """.formatted(user.getName(), otp);

            helper.setText(html, true);

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage());
        }
    }
}