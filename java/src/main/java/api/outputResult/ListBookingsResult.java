package api.outputResult;

import java.sql.Timestamp;

import api.APIOutputResult;

public record ListBookingsResult(
	String BookingUUID,
	String DoctorUUID,
	Timestamp BookingTimestamp,
	Boolean WasAttended
	) implements APIOutputResult {}
