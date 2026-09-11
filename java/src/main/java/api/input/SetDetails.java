package api.input;

import api.*;

@Authenticated
@Output(api.output.SetDetailsOutput.class)
public record SetDetails(
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
	) implements APIInput<api.output.SetDetailsOutput> {}
