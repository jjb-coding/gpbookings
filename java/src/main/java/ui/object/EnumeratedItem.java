package ui.object;

public class EnumeratedItem extends Identifier<Integer> {
	final int number;
	final String name;
	
	/**
	 * Constructs a EnumeratedItem instance.
	 * @param number
	 */
	public EnumeratedItem(int number, String name) {
		this.number = number;
		this.name = name;
	}
	
	@Override
	public Integer id() {
		return number;
	}

	@Override
	public String toString() {
		return name;
	}
}

