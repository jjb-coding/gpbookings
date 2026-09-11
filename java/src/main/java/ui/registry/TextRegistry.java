package ui.registry;

import java.util.HashMap;
import java.util.Map;

import services.registryService.*;

public enum TextRegistry {
	// -----------------------------
	FIELD_EMAIL_INVALID,
	FIELD_GENERAL_COMBO_BOX_UNSELECTED,
	FIELD_TEXT_TOO_LONG,
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
	MULTIFIELD_MATCHES_INVALID,
	SPECIALISE_PASSWORDS,
	FIELD_INVALID,
	
	BUTTON_NEXT,
	BUTTON_BACK,
	BUTTON_CANCEL,
	BUTTON_LOG_IN,
	BUTTON_SUBMIT,
	
	DASHBOARD_WELCOME_MESSAGE,
	DASHBOARD_BUTTON_VIEW_BOOKINGS,
	DASHBOARD_BUTTON_NEW_BOOKING,
	DASHBOARD_BUTTON_DETAILS,
	DASHBOARD_BUTTON_MESSAGES,
	
	ACCOUNT_BAR_BUTTON_LOG_OUT,

	LOG_IN_INSTRUCTION,
	NEW_INSTRUCTION,
	MAKE_BOOKING_INSTRUCTION,
	RESCHEDULE_BOOKING_INSTRUCTION,
	
	EMAIL_LABEL,
	PASSWORD_LABEL,
	CONFIRM_PASSWORD_LABEL,
	TITLE_LABEL,
	FIRST_NAME_LABEL,
	SURNAME_LABEL,
	CONTACT_PHONE_NUMBER_LABEL,
	STREET_1_LABEL,
	STREET_2_LABEL,
	CITY_LABEL,
	COUNTY_LABEL,
	POSTCODE_LABEL,
	DOCTOR_LABEL,
	DATE_LABEL,
	SLOT_LABEL,
	
	LOG_IN_SUCCESS,
	UPDATE_DETAILS_SUCCESS,
	RESCHEDULE_BOOKING_SUCCESS,
	MAKE_BOOKING_SUCCESS,
	NEW_SUCCESS,
	
	LOG_IN_READY,
	UDPATE_DETAILS_READY,
	RESCHEDULE_BOOKING_READY,
	MAKE_BOOKING_READY,
	NEW_READY,
	
	API_VALIDATION_ERROR,
	API_INVALID_CREDENTIALS,
	API_SESSION_TIMEOUT,
	API_ALREADY_EXISTS,
	API_BAD_BOOKING,
	API_BOOKED_ALREADY,
	API_BAD_LOGIN,
	API_MALFEASANCE,
	API_NO_API,	
	
	PARAMETRISED
	;

	// -----------------------------
	// The palette data
	String string;
	
	/**
	 * Constructs a palette item.
	 * @param string	The string.
	 */
	TextRegistry() {
		// Get Registry service & Text entry
		RegistryService registryService = RegistryServiceSingleton.INSTANCE.get();
		string = registryService.getMapLanguage().get(name());
		if (string == null)
			throw new RuntimeException("TextRegistry:init: Couldn't find " + this.name());
	}
	
	/**
	 * Returns the string associated with this item.
	 * @return 			The string.
	 */
	public String getString() {
		return string;
	}
	

	// Maps each enum instance's name to the instance.
	static Map<String, TextRegistry> map = new HashMap<>();
	
	/**
	 * Links a name to a definitive TextRegistry item.
	 * @param name	The name of the item. Not case sensitive.
	 * @return		The item.
	 */
	public static TextRegistry fromName(String name) {
		TextRegistry ret = map.get(name.toLowerCase());
		if (ret == null)
			throw new RuntimeException("TextRegistry:runtime: Failed to get " + name);
		return ret;
	}
	
	/**
	 * Constructs the map.
	 */
	static {
		for (TextRegistry text : values())
			map.put(text.name().toLowerCase(), text);
	}
}
