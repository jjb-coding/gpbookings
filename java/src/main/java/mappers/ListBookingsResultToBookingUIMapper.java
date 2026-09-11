package mappers;

import java.time.LocalDateTime;

import api.outputResult.ListBookingsResult;
import services.mapperService.ClassPair;
import services.mapperService.Mapper;
import services.practiceProvider.PracticeProvider;
import services.practiceProvider.PracticeProviderSingleton;
import ui.object.objects.BookingUI;
import ui.object.objects.WasAttendedEnumUI;

import util.StringUtil;

public class ListBookingsResultToBookingUIMapper extends Mapper<ListBookingsResult, BookingUI> {
	
	public BookingUI invoke(ListBookingsResult in) {
		PracticeProvider _practiceProvider = PracticeProviderSingleton.INSTANCE.get();
		LocalDateTime dateTime = in.BookingTimestamp().toLocalDateTime();
		return new BookingUI(
			in.BookingUUID(),
			_practiceProvider.getDoctorByID(in.DoctorUUID()).getFormattedName(),
			StringUtil.getSequence(dateTime.getDayOfMonth()),
			StringUtil.getFormattedTime(dateTime.toLocalTime()),
			_practiceProvider.isBookingOver(in.BookingTimestamp()) ? (in.WasAttended() ? WasAttendedEnumUI.YES : WasAttendedEnumUI.NO) : (WasAttendedEnumUI.NA),
			_practiceProvider.canBookingBeRescheduled(in.BookingTimestamp())
			);
	}
	
	@Override
	public ClassPair describe() {
		return new ClassPair(ListBookingsResult.class, BookingUI.class);
	}
}
