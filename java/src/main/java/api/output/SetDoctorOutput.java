package api.output;

import api.*;
import status.Status;

public record SetDoctorOutput(
	Status status
	) implements APIOutput {}
