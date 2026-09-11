package api.input;

import api.*;

@Authenticated
@Output(api.output.LogOutOutput.class)
public record LogOut(
	) implements APIInput<api.output.LogOutOutput> {} 
