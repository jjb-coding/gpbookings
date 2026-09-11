package api.output;

import api.*;
import status.Status;

public record GetDetailsOutput(
	Status status,
	String firstName,
	String surname,
	String title,
	String contactEmail,
	String contactPhoneNumber,
	String street1,
	String street2,
	String city,
	String county,
	String postCode
	) 
	implements APIOutput {}
