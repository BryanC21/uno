package CardHolders;
import java.util.ArrayList;
import Cards.Card;

public class Hand {
	
	private String name;
	
	private ArrayList<Card> pHand;
	
	/**
	 * This is a hand
	 */
	public Hand(String name)
	{
		this.name = name;
		pHand = new ArrayList<Card>();
	}
	
	/**Return the name of this hand's owner*/
	public String getName()
	{
		return name;
	}
	
	/**
	 * Shows the card at that index in your hand without removing it
	 */
	public Card getCard(int index)
	{
		return pHand.get(index);
	}
	
	/**
	 * Adds a card to the array that is your hand
	 */
	public void addToHand(Card q)
	{
		pHand.add(q);
	}
	
	/**
	 * Removes card from the array that is your hand
	 */
	public void removeFromHand(int q)
	{
		pHand.remove(q);
	}
	
	public int getSize()
	{
		return pHand.size();
	}
	
	/**
	 * Prints what your hand contains at that time
	 */
	public String toString()
	{
		String returned = "Your hand contains: \n";
		int counter = 1;
		
		for(Card x: pHand)
		{
			if(counter == pHand.size())
			{
				returned += "[" + counter + "]" + x;
			}
			else
			{
				returned += "[" + counter + "]" + x + "\n";
				counter++;
			}
		}
		
		return returned;
	}
}
