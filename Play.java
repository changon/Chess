import java.util.Scanner;

public class Play
{
	public static Board board;
	public static void main(String [] args)
	{
		Scanner scan = new Scanner(System.in);
		System.out.println("Time to play Chess! One player or Two player? (1, 2)");
		int value = Integer.parseInt(scan.next());
		if (value ==1) board = new Board(1);
		else if (value ==2 ) board = new Board(2); 
		else {System.out.println("type a valid option, 1 or 2");}	
	}
}
