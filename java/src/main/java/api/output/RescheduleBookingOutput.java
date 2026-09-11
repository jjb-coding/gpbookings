package api.output;

import api.*;
import status.Status;

public record RescheduleBookingOutput(
	Status status
	) implements APIOutput {}
