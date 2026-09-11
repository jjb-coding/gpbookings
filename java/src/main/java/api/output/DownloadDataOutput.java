package api.output;

import api.*;

public record DownloadDataOutput(
	String blob
	) implements APIOutput {}
