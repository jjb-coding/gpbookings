package api.input;

import api.*;

@Authenticated
@Output(api.output.SetDoctorOutput.class)
public record SetDoctor(
	String doctorUUID
	) implements APIInput<api.output.SetDoctorOutput> {}
