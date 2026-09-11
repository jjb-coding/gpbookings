package ui.object;

public class SequenceItem extends Identifier<Integer> {
	final int number;
	final String name;
	
	/**
	 * Constructs a SequenceItem instance.
	 * @param number
	 */
	public SequenceItem(int number) {		
		this.number = number;
		name = util.StringUtil.getSequence(number);
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