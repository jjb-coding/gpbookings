package api.output;

import api.*;
import status.Status;

import java.sql.*;

public record GetAttendedBookingOutput(
	Status status,
	Timestamp bookingTimestamp,
	Timestamp modifiedTimestamp,
	String DoctorUUID,
	String Summary,
	String PrescriptionsJSON
	) implements APIOutput {}
