package api.output;

import api.*;
import status.Status;

public record NewOutput(
	Status status
	)
	implements APIOutput {}
