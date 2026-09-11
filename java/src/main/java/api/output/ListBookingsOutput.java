package api.output;

import java.util.ArrayList;

import api.*;
import api.outputResult.ListBookingsResult;
import status.Status;

public record ListBookingsOutput(
	Status status,
	@ResultSet
	ArrayList<ListBookingsResult> bookings
	)
	implements APIOutput {}
