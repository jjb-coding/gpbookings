package api.outputResult;

import java.sql.*;
import api.*;

public record DownloadPracticePracticeDayDescriptionResult(
		Long PracticeDayDescriptionID,
		String Identifier,
		Time OpenTime,
		Integer NumberOfSlots,
		Integer LunchStart,
		Integer LunchEnd
		) implements APIOutputResult {}