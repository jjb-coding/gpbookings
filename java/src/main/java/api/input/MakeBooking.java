package api.input;

import api.*;
import java.sql.*;

@Authenticated
@Output(api.output.MakeBookingOutput.class)
public record MakeBooking(
	Date date,
	Integer index
	) implements APIInput<api.output.MakeBookingOutput> {} 
