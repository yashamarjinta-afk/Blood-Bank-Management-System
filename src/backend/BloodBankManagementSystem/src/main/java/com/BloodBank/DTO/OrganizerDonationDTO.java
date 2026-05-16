package com.BloodBank.DTO;

public class OrganizerDonationDTO {
	 private Long donationId;

	    private String donorName;
	    private String donorEmail;

	    private String bloodGroup;
	    private Integer units;

	    private String campName;
	    private String campLocation;

	    private String status;

		public OrganizerDonationDTO(Long donationId, String donorName, String donorEmail, String bloodGroup,
				Integer units, String campName, String campLocation, String status) {
			super();
			this.donationId = donationId;
			this.donorName = donorName;
			this.donorEmail = donorEmail;
			this.bloodGroup = bloodGroup;
			this.units = units;
			this.campName = campName;
			this.campLocation = campLocation;
			this.status = status;
		}
		
		
	    
}
