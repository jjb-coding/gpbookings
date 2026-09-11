package api.input;

import api.*;

@Output(api.output.ListBookingsOutput.class)
@Authenticated
public record ListBookings(
	Integer month,
	Integer year
	) implements APIInput<api.output.ListBookingsOutput> {} 
