package status;

import java.awt.Color;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import services.registryService.RegistryService;
import services.registryService.RegistryServiceSingleton;
import ui.registry.ColourRegistry;
import ui.registry.TextRegistry;

/**
 * Both a UI registry & a logical one.
 * Pull a StatusUI object out of any instance & then parametrise.
 * 
 * Transforms: i.e.
 * 	VALIDATION_EMAIL_BAD -> FAILURE : universal i.e. a prototype hierarchy
 * 	SUCCESS -> LOGIN_SUCCESS		: form-implemented
 *  SUCCESS -> GO_DASHBOARD			: contextual [?]
 *  
 * Only some statuses display [?]
 */
public enum Status {
	// ** PAGE
	EXIT,
	WELCOME,
	LOG_IN,
	NEW_FIRST,
	NEW_SECOND,
	DASHBOARD,
	VIEW_BOOKINGS,
	MAKE_BOOKING,
	VIEW_ATTENDED_BOOKING,
	RESCHEDULE_BOOKING,
	VIEW_MESSAGES,
	VIEW_MESSAGE,
	UPDATE_DETAILS,
	
	// ** ACTION
	SUCCESS,
	FAILURE,
	
	// ** DISPLAY SUCCESS
	LOG_IN_SUCCESS,
	UPDATE_DETAILS_SUCCESS,
	RESCHEDULE_BOOKING_SUCCESS,
	MAKE_BOOKING_SUCCESS,
	NEW_SUCCESS,
	
	// ** DISPLAY READY
	LOG_IN_READY,
	UDPATE_DETAILS_READY,
	RESCHEDULE_BOOKING_READY,
	MAKE_BOOKING_READY,
	NEW_READY,
	
	// ** FIELD
	FIELD_INVALID,
	FIELD_EMAIL_INVALID,
	FIELD_GENERAL_COMBO_BOX_UNSELECTED,
	FIELD_TEXT_TOO_LONG,
	FIELD_TEXT_EMPTY,
	FIELD_PASSWORD_EMPTY,
	FIELD_PASSWORD_CREATION_TOO_SHORT,
	FIELD_PASSWORD_CREATION_TOO_LONG,
	FIELD_PASSWORD_NEEDS_UPPERCASE,
	FIELD_PASSWORD_NEEDS_LOWERCASE,
	FIELD_PASSWORD_NEEDS_SPECIAL,
	FIELD_PASSWORD_NEEDS_DIGIT,
	FIELD_PERSONAL_NAME_TOO_SHORT,
	FIELD_PERSONAL_NAME_INVALID_CHARACTERS,
	FIELD_PHONE_NUMBER_INVALID_LENGTH,
	FIELD_PHONE_NUMBER_NEEDS_PLUS,
	FIELD_PHONE_NUMBER_INVALID_CHARACTERS,
	FIELD_POSTCODE_INVALID,

	// ** MULTIFIELD
	MULTIFIELD_INVALID,	// unused
	MULTIFIELD_MATCHES_INVALID,

	// ** LABEL
	LOGIN_LOGGING_IN,
	LOGIN_LOGIN_SUCCESS,
	
	// *** API
	// - SUCCESS
	VALIDATION_ERROR, // server-side validation in general inc. foreign key constraints
	
	// Auth
	INVALID_CREDENTIALS,
	SESSION_TIMEOUT,
	// - AUTHENTICATED, 
	
	// GetTimeForBookingSlot
	CLOSED_ON_WEEKDAY,
	BOOKING_SLOT_INVALID,
	TOO_CLOSE_FOR_NOW,
	DOCTOR_ALREADY_BOOKED,
	
	// GetDetails
	INCORRECT_ACCOUNT_TYPE,
	
	// SetDetails
	// INCORRECT_ACCOUNT_TYPE,
	
	// SetDoctor
	NOT_A_VALID_DOCTOR,
	
	// GetAttendedBooking
	BOOKING_UUID_INVALID,
	
	// Login
	LOGIN_DETAILS_INVALID,
	
	// New
	// VALIDATION_ERROR
	//NOT_A_VALID_DOCTOR,
	ALREADY_EXISTS,
	
	// Connection
	SQL_NO_CONNECTION,
	SQL_ERROR
	;
	
	// -----------------------------
	// *** FIELDS
	Color colour;
	String text;
	
	// *** CONSTRUCTORS
	/**
	 * Constructs a Status instance.
	 * @param colour
	 * @param text
	 */
	Status() {
		// Get Registry service & Status map entry
		RegistryService _registryService = RegistryServiceSingleton.INSTANCE.get();
		Map<String,String> map = _registryService.getMapStatus().get(name());
		
		// Null
		if (map == null) {
			colour = Color.BLACK;
			text = "";
			return;
		}
		
		// Decode
		String colourName = map.get("colour");
		ColourRegistry colourRegistry = ColourRegistry.fromName(colourName);
		if (colourRegistry == null)
			colour = null;
		else
			colour = colourRegistry.getColour();
		String textName = map.get("text");
		TextRegistry textRegistry = TextRegistry.fromName(textName);
		if (textRegistry == null)
			text = null;
		else
			text = textRegistry.getString();
	}
	
	// *** GETTERS
	public String getText() {
		return text;
	}
	public Color getColour() {
		return colour;
	}
	
	// *** STATIC
	// * HELPER
	public static Status map(EnumMap<Status,Status> map, Status source) {
		Status destination = map.get(source);
		if (destination == null)
			throw new RuntimeException("STATUS:runtime:Cast from " + source.name() + " failed.");
		return destination;
	}
	
	public static Status toAction(Status status) {
		return status == Status.SUCCESS ? Status.SUCCESS : Status.FAILURE;
	}
	
	// * NAME
	public static Status fromName(String name) {
		return map.get(name);
	}
	static Map<String, Status> map = new HashMap<>();
	static {
		for (Status status : values())
			map.put(status.name(), status);
	}
}