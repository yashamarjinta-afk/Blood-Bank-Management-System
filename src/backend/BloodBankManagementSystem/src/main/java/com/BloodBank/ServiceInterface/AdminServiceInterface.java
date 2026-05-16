package com.BloodBank.ServiceInterface;

import java.util.List;

import com.BloodBank.DTO.CampListResponseDTO;
import com.BloodBank.DTO.CampRequestDTO;
import com.BloodBank.DTO.CampResponseDTO;
import com.BloodBank.DTO.DeleteCampResponseDTO;
import com.BloodBank.DTO.OrganizerRequestDTO;
import com.BloodBank.DTO.OrganizerResponseDTO;
import com.BloodBank.DTO.StorageCenterRequestDTO;
import com.BloodBank.DTO.StorageCenterResponseDTO;

public interface AdminServiceInterface {

	public OrganizerResponseDTO create(OrganizerRequestDTO request);
	
	public CampResponseDTO createcamp(CampRequestDTO request);
	
	public List<CampListResponseDTO> findbylocation(String location);
	
	public CampResponseDTO findbyid(Long id);
	
	public DeleteCampResponseDTO deletebyid(Long id);
	
	public StorageCenterResponseDTO create(StorageCenterRequestDTO request);
}
