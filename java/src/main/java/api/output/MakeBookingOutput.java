package api.output;

import api.*;
import status.Status;

public record MakeBookingOutput(
	Status status
	)
	implements APIOutput {}
