package api.input;

import api.APIInput;
import api.Output;

@Output(api.output.DownloadDoctorsOutput.class)
public record DownloadDoctors(
	) implements APIInput<api.output.DownloadDoctorsOutput> {}
