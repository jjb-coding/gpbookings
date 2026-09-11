package api.input;

import api.*;

@Output(api.output.DownloadPracticeOutput.class)
public record DownloadPractice (
	) implements APIInput<api.output.DownloadPracticeOutput> {}
