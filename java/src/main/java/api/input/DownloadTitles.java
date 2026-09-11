package api.input;

import api.*;

@Output(api.output.DownloadTitlesOutput.class)
public record DownloadTitles(
	) implements APIInput<api.output.DownloadTitlesOutput> {}
