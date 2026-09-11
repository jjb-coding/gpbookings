package api.output;

import java.util.ArrayList;

import api.*;

public record DownloadPracticeOutput(
		@ResultSet
		ArrayList<api.outputResult.DownloadPracticePracticeResult> practice,
		@ResultSet
		ArrayList<api.outputResult.DownloadPracticePracticeDayDescriptionResult> practiceDayDescriptions
		) implements APIOutput {}
