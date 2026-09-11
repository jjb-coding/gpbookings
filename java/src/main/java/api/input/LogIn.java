package api.input;

import api.*;

@Output(api.output.LogInOutput.class)
public record LogIn(
	String securityEmail,
	byte[] password
	) implements APIInput<api.output.LogInOutput> {} 
