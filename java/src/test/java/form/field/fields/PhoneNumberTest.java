package form.field.fields;

import javax.swing.JTextField;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import status.Status;
import ui.component.components.XLabelStatus;

public class PhoneNumberTest {
	/**
	 * Detects validation sensitivity of phone numbers
	 * that are not of any valid length, national or 
	 * international.
	 */
	@Test
	public void ValidatesCorrectLength() {
		JTextField partner = new JTextField();
		XLabelStatus status = new XLabelStatus(null);
		PhoneNumber pc = new PhoneNumber(partner, status);

		partner.setText("07900000000");
		pc.ApplyLogic();
		Assertions.assertTrue(
				pc.getError() != Status.FIELD_PHONE_NUMBER_INVALID_LENGTH
				);
		
		partner.setText("44790000");
		pc.ApplyLogic();
		Assertions.assertTrue(
				pc.getError() == Status.FIELD_PHONE_NUMBER_INVALID_LENGTH
				);
	}
	
	/**
	 * Detects validation sensitivity of a phone numbers that
	 * are of international length, but don't begin with a
	 * plus symbol.
	 */
	@Test
	public void ValidatesBeginsWithPlus() {
		JTextField partner = new JTextField();
		XLabelStatus status = new XLabelStatus(null);
		PhoneNumber pc = new PhoneNumber(partner, status);
		
		partner.setText("+447900000000");
		pc.ApplyLogic();
		Assertions.assertTrue(
				pc.getError() != Status.FIELD_PHONE_NUMBER_NEEDS_PLUS
				);
		
		partner.setText("-447900000000");
		pc.ApplyLogic();
		Assertions.assertTrue(
				pc.getError() == Status.FIELD_PHONE_NUMBER_NEEDS_PLUS
				);
	}
	
	/**
	 * Detects validation sensitivity of phone numbers containing
	 * non-numeric characters.
	 */
	@Test
	public void ValidatesNeedsNumber() {
		JTextField partner = new JTextField();
		XLabelStatus status = new XLabelStatus(null);
		PhoneNumber pc = new PhoneNumber(partner, status);
		
		partner.setText("070000000-0");
		pc.ApplyLogic();
		Assertions.assertTrue(
				pc.getError() == Status.FIELD_PHONE_NUMBER_INVALID_CHARACTERS
				);
		
		partner.setText("07000000!00");
		pc.ApplyLogic();
		Assertions.assertTrue(
				pc.getError() == Status.FIELD_PHONE_NUMBER_INVALID_CHARACTERS
				);
		
		partner.setText("070.0q00xa0");
		pc.ApplyLogic();
		Assertions.assertTrue(
				pc.getError() == Status.FIELD_PHONE_NUMBER_INVALID_CHARACTERS
				);
	}
}
