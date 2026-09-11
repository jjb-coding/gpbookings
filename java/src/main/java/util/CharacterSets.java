package util;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;

/**
 * Character set provider, used by most validated
 * fields. Has utility functions.
 */
public enum CharacterSets {
	// Upper case characters
	UPPER(Arrays.asList(
		'A','B','C','D','E','F','G','H','I','J','K','L',
		'M','N','O','P','Q','R','S','T','U','V','W','X',
		'Y','Z'
	)),
	
	// Lower case characters
	LOWER(Arrays.asList(
		'a','b','c','d','e','f','g','h','i','j','k','l',
		'm','n','o','p','q','r','s','t','u','v','w','x',
		'y','z'
	)),
	
	// Digits
	DIGIT(Arrays.asList(
		'0','1','2','3','4','5','6','7','8','9'
	)),
	
	// Other special characters
	EXTRA(Arrays.asList(
		'&','$','.','-'
	)),
	
	// Characters allowed in certain parts of an email address, wanted in passwords
	SPECIAL(Arrays.asList(
		'-','!','#','$','%','&','\'','*','+','/','=','?',
		'^','_','`','{','|','}','~',']','.'
	));
	
	// The set
	private HashSet<Character> set;
	
	/**
	 * Test if a character is in this set.
	 * @param c The character to test
	 * @return Whether it is in this set
	 */
	public boolean has(char c) {
		return set.contains(c);
	}
	
	/**
	 * Constructs a CharacterSets instance.
	 * @param characters The characters it contains.
	 */
	CharacterSets(List<Character> characters) {
		set = new HashSet<Character>(characters);
	}
	
	/**
	 * Determines if the character appears in a union of the provided sets.
	 * @param sets The array of sets.
	 * @param c The character to be tested.
	 * @return Whether it appears or not.
	 */
	public static boolean inAny(EnumSet<CharacterSets> css, char c) {
		for (CharacterSets cs : css) {
			if (cs.has(c)) return true;
		}
		return false;
	}
}
