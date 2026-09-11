package api.output;

import api.*;
import status.Status;

public record LogOutOutput(
	Status status
	)
	implements APIOutput {}
