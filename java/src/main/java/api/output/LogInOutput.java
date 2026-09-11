package api.output;

import api.*;
import status.Status;

public record LogInOutput(
	Status status,
	String accountID,
	String sessionToken
	)
	implements APIOutput {}
