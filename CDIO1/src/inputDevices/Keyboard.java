package inputDevices;

import java.util.Scanner;

public class Keyboard {

	private Scanner keyb;
	
	public Keyboard()
	{
		keyb = new Scanner(System.in);
	}
	
	public int nextInt()
	{
		String input  = keyb.nextLine();
		int result = -1;
		
			try
			{
				result = Integer.parseInt(input);
					
			}
			catch(NumberFormatException e)
			{
				System.out.println("Not a number");
			}
		return result;
	}
	
	public String nextString()
	{
		return keyb.nextLine();
	}
	
	public void close()
	{
		keyb.close();
	}

}
