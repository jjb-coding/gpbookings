package services.practiceProvider;

import ui.object.Identifier;

/**
 * Immutable data object. Hosts a Doctor instance.
 */
public class DoctorData extends Identifier<String> {
	// UUID. Creates a symbolic link between the local
	// representation and the server representation.
	String uuid;
	
	// Details
	String firstName;
	String surname;
	String specialism;
	
	/**
	 * Constructs a new data object instance of Doctor type.
	 * @param firstName		The UUID.
	 * @param firstName		Their first name.
	 * @param surname		Their surname.
	 * @param specialism	Their specialism.
	 */
	public DoctorData(String uuid, String firstName, String surname, String specialism) {
		this.uuid = uuid;
		this.firstName = firstName;
		this.surname = surname;
		this.specialism = specialism;
	}
	
	/**
	 * Generates the formatted version of the doctor's name.
	 * @return The name
	 */
	public String getFormattedName() {
		return "Dr. " + firstName + " " + surname;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSpecialism() {
		return specialism;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDescription() {
		return "<html>" + getFormattedName() + ":<br>" + specialism + "</html>";
	}
		
	/**
	 * Returns the UUID.
	 */
	@Override
	public String id() {
		return uuid;
	}
	
	/**
	 * Returns the formatted name.
	 */
	@Override
	public String toString() {
		return getDescription();
	}
}
