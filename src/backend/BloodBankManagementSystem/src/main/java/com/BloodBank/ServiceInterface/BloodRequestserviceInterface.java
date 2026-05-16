package com.BloodBank.ServiceInterface;

import com.BloodBank.DTO.BloodRequestrequestDTO;
import com.BloodBank.DTO.BloodrequestResponseDTO;

public interface BloodRequestserviceInterface {

	public BloodrequestResponseDTO createbloodrequest(BloodRequestrequestDTO request);
}
