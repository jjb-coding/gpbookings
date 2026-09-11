package api.input;

import api.*;

@Output(api.output.DownloadDataOutput.class)
public record DownloadData(
	Integer clientVersion
	) implements APIInput<api.output.DownloadDataOutput> {}
