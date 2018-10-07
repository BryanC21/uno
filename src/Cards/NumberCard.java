package Cards;

public class NumberCard extends Card{
	
	private int number;
	
	public NumberCard(int number, String color)
	{
		super(color);
		this.number = number;
	}
	
	/**
	 * Gets the number on this card
	 */
	public int getNumber()
	{
		return number;
	}
	
	public String toString()
	{
		return "A " + super.getColor() + " " + number;
	}
}
