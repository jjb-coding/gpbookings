package api.outputResult;

import java.sql.*;
import api.*;

public record DownloadPracticePracticeResult(
		Time GracePeriod,
		Time SlotLength,
		Long MondayID,
		Long TuesdayID,
		Long WednesdayID,
		Long ThursdayID,
		Long FridayID,
		Long SaturdayID,
		Long SundayID		
		) implements APIOutputResult {}