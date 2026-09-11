package ui.object;

public class NumberItem extends Identifier<Integer> {
	final int number;
	final String name;
	
	/**
	 * Constructs a NumberItem instance.
	 * @param number
	 */
	public NumberItem(int number) {
		this.number = number;
		this.name = String.valueOf(number);
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