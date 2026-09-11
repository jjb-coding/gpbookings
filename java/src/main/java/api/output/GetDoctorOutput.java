package api.output;

import api.*;
import status.Status;

public record GetDoctorOutput(
	Status status,
	String doctorUUID
	)
	implements APIOutput {}
