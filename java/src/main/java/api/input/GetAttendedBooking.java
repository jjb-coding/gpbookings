package api.input;

import api.*;

@Output(api.output.GetAttendedBookingOutput.class)
@Authenticated
public record GetAttendedBooking(
	String bookingUUID
	) implements APIInput<api.output.GetAttendedBookingOutput> {}