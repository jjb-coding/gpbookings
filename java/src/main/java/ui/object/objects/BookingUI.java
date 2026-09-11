package ui.object.objects;

public record BookingUI(
		String bookingUUID,
		String doctorName,
		String dayOfMonth,
		String time,
		WasAttendedEnumUI wasAttended,
		boolean canBeRescheduled
) {
	public Object[] asObjects() {
		return new Object[] {
				dayOfMonth(),
				time(),
				doctorName(),
				wasAttended(),
				canBeRescheduled()
			};
	}
};