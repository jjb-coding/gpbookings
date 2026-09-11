package api.input;

import api.*;

@Output(api.output.NewOutput.class)
public record New(
	String securityEmail,
	byte[] password,
	String doctorUUID,
	String firstName,
	String surname,
	String title,
	String contactPhoneNumber,
	String street1,
	String street2,
	String city,
	String county,
	String postCode
	) implements APIInput<api.output.NewOutput> {} 
