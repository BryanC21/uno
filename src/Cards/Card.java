package Cards;

public class Card {

	/**Color of the card*/
	private String color = "none";
	/**If the effect of the card was used*/
	private boolean wasUsed = false;
	
	public Card(String color)
	{
		this.color = color;
	}
	
	public Card(){}
	
	/**
	 * Sets the color of a card.
	 */
	public void setColor(String color)
	{
		this.color = color;
	}

	/**
	 * If card has a color this returns it.
	 * Returns "none" if no color.
	 */
	public String getColor()
	{
		return color;
	}
	
	/**Get if effect was used*/
	public boolean getUsed()
	{
		return wasUsed;
	}
	
	/**Set the effect to have been used*/
	public void setUsed()
	{
		wasUsed = true;
	}
	
	/**
	 * Returns the number of the card or 999 if it doesn't have one.
	 */
	public int getNumber() {
		return 999;
	}
}
