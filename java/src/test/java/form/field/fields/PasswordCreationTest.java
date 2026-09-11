package form.field.fields;

import javax.swing.JPasswordField;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import status.Status;
import ui.component.components.XLabelStatus;

public class PasswordCreationTest {
	/**
	 * Detects validation sensitivity of a lack
	 * of uppercase characters.
	 */
	@Test
	public void ValidatesNeedsUppercase() {
		JPasswordField partner = new JPasswordField();
		XLabelStatus status = new XLabelStatus(null);
		PasswordCreation pc = new PasswordCreation(partner, status);
		
		partner.setText("password1!");
		pc.ApplyLogic();
		Assertions.assertTrue(
				pc.getError() == Status.FIELD_PASSWORD_NEEDS_UPPERCASE
				);
	}
	
	/**
	 * Detects validation sensitivity of a lack
	 * of lowercase characters.
	 */
	@Test
	public void ValidatesNeedsLowercase() {
		JPasswordField partner = new JPasswordField();
		XLabelStatus status = new XLabelStatus(null);
		PasswordCreation pc = new PasswordCreation(partner, status);
		
		partner.setText("PASSWORD1!");
		pc.ApplyLogic();
		Assertions.assertTrue(
				pc.getError() == Status.FIELD_PASSWORD_NEEDS_LOWERCASE
				);
	}
	
	/**
	 * Detects validation sensitivity of a lack
	 * of digits.
	 */
	@Test
	public void ValidatesNeedsNumber() {
		JPasswordField partner = new JPasswordField();
		XLabelStatus status = new XLabelStatus(null);
		PasswordCreation pc = new PasswordCreation(partner, status);
		
		partner.setText("Password!");
		pc.ApplyLogic();
		Assertions.assertTrue(
				pc.getError() == Status.FIELD_PASSWORD_NEEDS_DIGIT
				);
	}
	
	/**
	 * Detects validation sensitivity of a lack
	 * of special characters.
	 */
	@Test
	public void ValidatesNeedsSpecial() {
		JPasswordField partner = new JPasswordField();
		XLabelStatus status = new XLabelStatus(null);
		PasswordCreation pc = new PasswordCreation(partner, status);
		
		partner.setText("Password1");
		pc.ApplyLogic();
		Assertions.assertTrue(
				pc.getError() == Status.FIELD_PASSWORD_NEEDS_SPECIAL
				);
	}
}
