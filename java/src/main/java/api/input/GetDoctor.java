package api.input;

import api.*;

@Output(api.output.GetDoctorOutput.class)
@Authenticated
public record GetDoctor(
	
	) implements APIInput<api.output.GetDoctorOutput> {} 
