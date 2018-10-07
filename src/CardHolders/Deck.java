package CardHolders;
import java.util.ArrayList;
import java.util.Random;

import Cards.Card;
import Cards.DrawFour;
import Cards.DrawTwo;
import Cards.NumberCard;
import Cards.Reverse;
import Cards.Skip;
import Cards.WildCard;

public class Deck {
	
	private ArrayList<Card> deck;

	public Deck()
	{
		deck = new ArrayList<Card>();
		buildDeck();
	}
	
	/**
	 * Picks a random integer and draws the card at that index from the deck
	 * Returns that card and removes it from deck
	 */
	public Card drawCard()
	{
		if (deck.size() < 1) {
			buildDeck(); // If no cards left refill the deck
		}
		Random num = new Random();
		int drawn = num.nextInt(deck.size());//Picks integer up to one less than deck size
		Card temp = deck.get(drawn);//Saves card to temporary card at that generated index
		deck.remove(drawn);//Removes card from deck
		return temp;
	}
	
	/**
	 * Prints all cards available in deck at that time
	 */
	public void printDeck()
	{
		for(Card x: deck)
		{
			System.out.println(x);
		}
	}
	
	/**
	 * Generates all cards needed for a full deck
	 */
	public void buildDeck()
	{
		//Two cards of 0 - 9 for every color
		for(int i = 0; i < 10; i++)
		{
			deck.add(new NumberCard(i,"Green"));
			deck.add(new NumberCard(i,"Green"));
		}
		for(int i = 0; i < 10; i++)
		{
			deck.add(new NumberCard(i,"Blue"));
			deck.add(new NumberCard(i,"Blue"));
		}
		for(int i = 0; i < 10; i++)
		{
			deck.add(new NumberCard(i,"Yellow"));
			deck.add(new NumberCard(i,"Yellow"));
		}
		for(int i = 0; i < 10; i++)
		{
			deck.add(new NumberCard(i,"Red"));
			deck.add(new NumberCard(i,"Red"));
		}
		//Two Draw Two cards; two Skip cards; and two Reverse cards.
		deck.add(new DrawTwo("Green"));
		deck.add(new DrawTwo("Green"));
		deck.add(new DrawTwo("Blue"));
		deck.add(new DrawTwo("Blue"));
		deck.add(new DrawTwo("Yellow"));
		deck.add(new DrawTwo("Yellow"));
		deck.add(new DrawTwo("Red"));
		deck.add(new DrawTwo("Red"));
		deck.add(new Reverse("Yellow"));
		deck.add(new Reverse("Yellow"));
		deck.add(new Reverse("Green"));
		deck.add(new Reverse("Green"));
		deck.add(new Reverse("Blue"));
		deck.add(new Reverse("Blue"));
		deck.add(new Skip("Green"));
		deck.add(new Skip("Green"));
		deck.add(new Skip("Red"));
		deck.add(new Skip("Red"));
		deck.add(new Skip("Yellow"));
		deck.add(new Skip("Yellow"));
		deck.add(new Skip("Blue"));
		deck.add(new Skip("Blue"));
		//In addition there are four Wild cards and four Wild Draw Four cards.
		deck.add(new WildCard());
		deck.add(new WildCard());
		deck.add(new WildCard());
		deck.add(new WildCard());
		deck.add(new DrawFour());
		deck.add(new DrawFour());
		deck.add(new DrawFour());
		deck.add(new DrawFour());
	}
}
