package api.output;

import java.util.ArrayList;

import api.*;

public record DownloadTitlesOutput(
		@ResultSet
		ArrayList<api.outputResult.DownloadTitlesTitleResult> titles
		) implements APIOutput {}
