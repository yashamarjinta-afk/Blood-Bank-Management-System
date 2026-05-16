package com.BloodBank.ServiceInterface;

public interface EmailServiceinterface {

	public String sendemail(String email,byte[] pdf);
	
	public String rejectemail(String email);
}
