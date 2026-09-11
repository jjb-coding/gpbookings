package api.outputResult;

import api.*;

public record DownloadDoctorsResult(
		String UUID,
		String FirstName,
		String Surname,
		String Specialism
		) implements APIOutputResult {}
