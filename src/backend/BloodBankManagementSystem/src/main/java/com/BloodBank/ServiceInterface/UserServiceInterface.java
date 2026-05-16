package com.BloodBank.ServiceInterface;

import com.BloodBank.DTO.LoginRequestDTO;
import com.BloodBank.DTO.LoginResponseDTO;
import com.BloodBank.DTO.RegistrationRequestDTO;
import com.BloodBank.DTO.RegistrationResponseDTO;

public interface UserServiceInterface {

	public RegistrationResponseDTO register(RegistrationRequestDTO request);
	
	public LoginResponseDTO login(LoginRequestDTO request);
	
	String sendemail(String email);
	   
	   String verifyOTP(String email,Integer otp);
	   
	   public String resetpassword(String email, String newpassword);
}
