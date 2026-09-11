package api.output;

import api.*;
import status.Status;

public record SetDetailsOutput(
	Status status
	) implements APIOutput {}
