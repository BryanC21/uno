package GameTypes;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import CardHolders.Deck;
import CardHolders.Hand;
import Cards.*;

/**
 * This class will run the game of UNO
 * 	Enforce the rules
 * 	Take care of player hands
 * 	Control computers
 * Player 1 is user, player 2 is comp1 and so on clockwise for 4 total players
 * Game Ends if no input for 60 seconds of user turn
 * @author Bryan Caldera
 */
public class GameNoUI {
	
	//input;
	BufferedReader in2;
	/**Hand of each player*/
	private Hand user, comp1, comp2, comp3;
	/**The deck we draw from (Not infinite)*/
	private Deck deck1;
	private Card centerCard;
	/**Keeps track if anyone won*/
	private boolean hasWon = false;
	/**Keeps track of the direction*/
	private boolean clockWise = true; 
	/**The player to play next: 1 is user going clockwise*/
	private int upNext = 1;
	/**Keeps track of who won*/
	private String winner;
	
	public GameNoUI()
	{
		deck1 = new Deck();
		comp1 = new Hand("Computer 1");
		comp2 = new Hand("Computer 2");
		comp3 = new Hand("Computer 3");
		user = new Hand("The user");
		//input = new Scanner(System.in);
		in2 = new BufferedReader(new InputStreamReader(System.in));
		
		setupGame();
		runGame();
	}

	/**This will print if someone wins. It will get the game started with user playing first.*/
	private void runGame()
	{
		try {
			userTurn();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(winner + " has won");
		System.out.println("END");
	}
	
	/**Lets the user go through their turn and checks if they win.
	 * At beginning of turn checks if player was affected by any special cards*/
	private void userTurn() throws IOException {
		if (centerCard instanceof DrawFour && !centerCard.getUsed()) {
			 user.addToHand(deck1.drawCard());
			 user.addToHand(deck1.drawCard());
			 user.addToHand(deck1.drawCard());
			 user.addToHand(deck1.drawCard());
			 centerCard.setUsed();
			 System.out.println("Next player drew 4 cards");
		}
		if (centerCard instanceof DrawTwo && !centerCard.getUsed()) {
			user.addToHand(deck1.drawCard());
			user.addToHand(deck1.drawCard());
			centerCard.setUsed();
			System.out.println("Next player drew 2 cards");
		}
		int choice = 0; //The index of the card the user picks
		try {
			choice = askForInput();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (choice == -1) { //User wants to draw a card
			user.addToHand(deck1.drawCard());
			System.out.println("You drew a card!");
			upNext = checkNextPlayer();
			computersPlay();
			
		} else if (choice == -2) { //User wants to quit
			winner = "No one";
			try {
				in2.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("You quit!");
			return;
			
		} else { //User wants to play a card
		
			if(!validInput(user,choice)) {
				userTurn();
			} else {
		
				centerCard = user.getCard(choice);
				user.removeFromHand(choice);
				System.out.println("You played: " + centerCard);
				
				boolean validColor = false;
				if (centerCard instanceof WildCard || centerCard instanceof DrawFour) {
					System.out.println("Pick a color from: Yellow, Green, Blue, Red");
					String newColor = "Yellow";
					//newColor = input.next();
					long startedInput = System.currentTimeMillis();
					while((System.currentTimeMillis()-startedInput) < 60*1000 && !in2.ready()) {
						try {
							Thread.sleep(500);/*loops 60 seconds*/
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if (in2.ready()) {
						newColor = in2.readLine();
					} else {
						System.out.println("Game Ended - Idle");
						in2.close();
						System.exit(0);
					}
					
					while (!validColor) { // Makes sure color input is valid
						if (newColor.equalsIgnoreCase("Red") || newColor.equalsIgnoreCase("Green") 
								|| newColor.equalsIgnoreCase("Blue") || newColor.equalsIgnoreCase("Yellow")){
							validColor = true;
							break;
						}
						System.out.println(newColor + " is not a valid color!\nInput a new color:");
						startedInput = System.currentTimeMillis();
						while((System.currentTimeMillis()-startedInput) < 60*1000 && !in2.ready()) {
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							/*loops 60 seconds*/}
						if (in2.ready()) {
							newColor = in2.readLine();
						} else {
							System.out.println("Game Ended - Idle");
							in2.close();
							System.exit(0);
						}
					}
					
					System.out.println("The color is now: " + newColor);
					centerCard.setColor(newColor);
				}
				
				// Special card effects
				if (centerCard instanceof Skip && !centerCard.getUsed()) {
					upNext++;
					centerCard.setUsed();
				}
				if (centerCard instanceof Reverse && !centerCard.getUsed()) {
					clockWise = !clockWise;
					centerCard.setUsed();
				}
								
				hasWon = checkForWinner(user);
				if(!hasWon) //If user has not won let the computers play
				{
					upNext = checkNextPlayer();
					computersPlay();
				}
				else
				{
					try {
						in2.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					winner = "The user";
				}
			
			}
		
		}
	}
	
	/**Checks if the card chosen can be played this turn.*/
	private boolean validInput(Hand player, int choice)
	{
		//Conditions:
		boolean sameNum = (player.getCard(choice).getNumber() == centerCard.getNumber()) && (centerCard.getNumber() != 999);
		boolean sameColor = (player.getCard(choice).getColor().equalsIgnoreCase(centerCard.getColor())) && !(centerCard.getColor().equals("none")) ;
		boolean wild = player.getCard(choice) instanceof WildCard || player.getCard(choice) instanceof DrawFour;
		//DrawTwo or Skip or Reverse - Different Color
		boolean sameType = (player.getCard(choice) instanceof DrawTwo && centerCard instanceof DrawTwo) || (player.getCard(choice) instanceof Skip && centerCard instanceof Skip) || (player.getCard(choice) instanceof Reverse && centerCard instanceof Reverse);
		if(sameNum || sameColor || wild || sameType)
		{
			return true;
		}
		//If this card couldn't be played ask for new input
		System.out.println("That card can't be played here! Pick a new card!");
		return false;
	}
	
	/**Asks the user to pick a card from their hand, draw a card, or quit the game.*/
	private int askForInput() throws IOException {
		System.out.println("\nIt is your turn\n" + user);
		System.out.println("***\nThe center card is: " + centerCard+"\n***");
		if (centerCard instanceof WildCard || centerCard instanceof DrawFour) {
			System.out.println("The color is " + centerCard.getColor());
		}
		System.out.println("Which card will you play?\nType number next to the card to play that card.\nType \"-1\" to draw a card.\nOr type \"-2\" to quit the game");
		long startedInput = System.currentTimeMillis();
		int index = 0;
		while((System.currentTimeMillis()-startedInput) < 60*1000 && !in2.ready()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			/*loops 60 seconds*/}
		if (in2.ready()) {
			index = Integer.parseInt(in2.readLine());
		} else {
			System.out.println("Game Ended - Idle");
			in2.close();
			System.exit(0);
		}
		System.out.println("Input: "+index);
		if(index <= user.getSize() && index > 0)
		{
			return index - 1;
		}
		else if (index == -1) {
			return -1;
		}
		else if (index == -2) {
			return -2;
		}
		//If that number was out of bounds ask for another input
		System.out.println("That was not a valid choice. You don't have a card at " + index);
		return askForInput();
	}
	
	/**Lets the computers play and checks if any of them win. Keeps track of which computer won*/
	private void computersPlay()
	{
		String compPlaying = null; //This is the computer playing at that moment
		//Chooses who gets to play next
		if(upNext == 2)
		{
			compPlaying = "Computer 1";
			ai(comp1);
			hasWon = checkForWinner(comp1);
		} else
		if(upNext == 3)
		{
			compPlaying = "Computer 2";
			ai(comp2);
			hasWon = checkForWinner(comp2);
		} else
		if(upNext == 4)
		{
			compPlaying = "Computer 3";
			ai(comp3);
			hasWon = checkForWinner(comp3);
		}
		//If we have no winner check if a computer or player is next else declare the winner
		if(!hasWon)
		{
			upNext = checkNextPlayer();
			if(upNext == 1)
			{
				try {
					userTurn();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else
			{
				computersPlay();
			}
		}
		else
		{
			try {
				in2.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			winner = compPlaying;
		}
		
	}
	
	/**Checks all conditions to see whose turn is next.*/
	private int checkNextPlayer()
	{
		int nextPlayer;
		if(upNext == 1 && !clockWise)
		{
			nextPlayer = 4;
		}
		else if(upNext == 4 && clockWise)
		{
			nextPlayer = 1;
		}
		else
		{
			if(clockWise)
			{
				nextPlayer = upNext+1;
			}
			else
			{
				nextPlayer = upNext-1;
			}
		}
		
		return nextPlayer;
	}
	
	/**Checks if any player has won or if they have UNO*/
	private boolean checkForWinner(Hand player)
	{
		if(player.getSize() == 1)
		{
			System.out.println(player.getName() + " has UNO!");;
		}
		
		return player.getSize() == 0;
	}
	
	/**Decides for each computer what card would best be played here and plays it*/
	private void ai(Hand player)
	{
		if (centerCard instanceof DrawFour && !centerCard.getUsed()) {
			 player.addToHand(deck1.drawCard());
			 player.addToHand(deck1.drawCard());
			 player.addToHand(deck1.drawCard());
			 player.addToHand(deck1.drawCard());
			 centerCard.setUsed();
			 System.out.println("Next player drew 4 cards");
		}
		if (centerCard instanceof DrawTwo && !centerCard.getUsed()) {
			player.addToHand(deck1.drawCard());
			player.addToHand(deck1.drawCard());
			centerCard.setUsed();
			System.out.println("Next player drew 2 cards");
		}

		System.out.println("\n"+"It is " + player.getName() + "'s turn!");
		/*
		 * Play any special cards
		 * Try to play a number card
		 * Wild card
		 * Draw
		 */
		boolean cardPicked = false; //Sees if a card can be played
		int bestIndex = 0; //The card we will play
		String bestColor = "Yellow";
		
		//Start off picking the best number card
		for(int i = 0; i < player.getSize(); i++)
		{
			if((player.getCard(i).getColor().equalsIgnoreCase(centerCard.getColor())
					&& !player.getCard(i).getColor().equals("none")) || (player.getCard(i).getNumber() ==
					centerCard.getNumber() && player.getCard(i).getNumber() != 999))
			{
				//Pick the card with the biggest number
				if(!cardPicked || player.getCard(i).getNumber() < centerCard.getNumber())
				{
					bestIndex = i;
					cardPicked = true;
				}
			}
		}
		//Go through and see if there is a better choice which is a special card
		for(int i = 0; i < player.getSize(); i++)
		{
			if(player.getCard(i).getColor().equalsIgnoreCase(centerCard.getColor()) && (player.getCard(i) instanceof DrawTwo
					|| player.getCard(i) instanceof Skip || player.getCard(i) instanceof Reverse))
			{
				bestIndex = i;
				cardPicked = true;
			}
		}	
		/*
		 * Check if a card got picked
		 * If one wasn't then check for wild cards
		 * Else draw a card
		 */
		if(!cardPicked)
		{
			//Check for wild card
			for(int i = 0; i < player.getSize(); i++)
			{
				if(player.getCard(i) instanceof WildCard || player.getCard(i) instanceof DrawFour)
				{
					bestIndex = i;
					cardPicked = true;
					if ((int) (Math.random() * 4) + 1 == 1) {
						bestColor = "Blue";
					}
					if ((int) (Math.random() * 4) + 1 == 2) {
						bestColor = "Yellow";
					}
					if ((int) (Math.random() * 4) + 1 == 3) {
						bestColor = "Green";
					}
					if ((int) (Math.random() * 4) + 1 == 4) {
						bestColor = "Red";
					}
				}
			}
			//Nothing can be played so draw a card
			if(!cardPicked)
			{
				player.addToHand(deck1.drawCard());
				System.out.println(player.getName() + " drew a card!");
			}
		}
		//Actually play the card
		if(cardPicked)
		{
			centerCard = player.getCard(bestIndex);
			player.removeFromHand(bestIndex);
			System.out.println(player.getName() + " played " + centerCard);
			if (centerCard instanceof Skip && !centerCard.getUsed()) {
				upNext = checkNextPlayer();
				centerCard.setUsed();
			}
			if (centerCard instanceof Reverse && !centerCard.getUsed()) {
				clockWise = !clockWise;
				centerCard.setUsed();
			}
			if (centerCard instanceof WildCard || centerCard instanceof DrawFour) {
				centerCard.setColor(bestColor);
				System.out.println("The color is now: " + bestColor);
			}
		}	
	}
	
	/**
	 * Draws all of the player's hands
	 * Picks the starting center card
	 */
	private void setupGame()
	{
		drawHand(user, deck1);
		drawHand(comp1, deck1);
		drawHand(comp2, deck1);
		drawHand(comp3, deck1);
		centerCard = deck1.drawCard();
		if (centerCard instanceof WildCard || centerCard instanceof DrawFour) {
			randomColor();
			centerCard.setUsed();
			System.out.println("The color is " + centerCard.getColor());
		}
		if (centerCard instanceof DrawTwo) {
			centerCard.setUsed();
		}
	}
	
	/**Picks a color randomly to set centercard to*/
	private void randomColor() {
		
		String bestColor = "Yellow";
		switch ((int) (Math.random() * 4) + 1) {
			case 1:
				bestColor = "Blue";
				break;
			case 2:
				bestColor = "Red";
				break;
			case 3:
				bestColor = "Green";
				break;
			case 4:
				bestColor = "Yellow";
				break;
		}
		
		centerCard.setColor(bestColor);
	}
	
	/**
	 * Draws a 7 card hand
	 * @param x user who gets the cards
	 * @param y deck the cards are drawn from
	 */
	private void drawHand(Hand x, Deck y)
	{
		for(int i = 0; i < 7; i++)
		{
			x.addToHand(y.drawCard());
		}
	}
	
	
}
