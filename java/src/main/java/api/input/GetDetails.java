package api.input;

import api.*;

@Output(api.output.GetDetailsOutput.class)
@Authenticated
public record GetDetails(
	
	) implements APIInput<api.output.GetDetailsOutput> {} 
