package com.BloodBank.ServiceInterface;

import java.util.List;

import com.BloodBank.DTO.DonationHistoryDTO;
import com.BloodBank.DTO.DonationRequestDTO;
import com.BloodBank.DTO.DonationResponseDTO;
import com.BloodBank.DTO.approveRequestDTO;
import com.BloodBank.DTO.approveResponseDTO;
import com.BloodBank.DTO.pendingResponseDTO;
import com.BloodBank.DTO.rejectRequestDTO;
import com.BloodBank.DTO.rejectResponseDTO;
import com.BloodBank.Entity.Donation;

public interface DonationServiceinterface {

	public DonationResponseDTO donate(DonationRequestDTO request,String email);
	
	public List<pendingResponseDTO> getpending();
	
	
	
	public List<DonationHistoryDTO> history(String email);
	
	
}
