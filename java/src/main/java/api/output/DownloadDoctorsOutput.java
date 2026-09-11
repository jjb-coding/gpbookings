package api.output;

import java.util.ArrayList;

import api.*;

public record DownloadDoctorsOutput(
	@ResultSet
	ArrayList<api.outputResult.DownloadDoctorsResult> doctors
	) implements APIOutput {}
