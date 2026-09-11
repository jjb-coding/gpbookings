package api.input;

import api.*;
import java.sql.*;

@Authenticated
@Output(api.output.RescheduleBookingOutput.class)
public record RescheduleBooking(
	String bookingUUID,
	Date date,
	Integer index
	) implements APIInput<api.output.RescheduleBookingOutput> {}