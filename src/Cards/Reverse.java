package Cards;

public class Reverse extends Card{

	public Reverse(String color)
	{
		super(color);
	}
	
	public String toString()
	{
		return "A " + super.getColor() + " Reverse card";
	}
}
