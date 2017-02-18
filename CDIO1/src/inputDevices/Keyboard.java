package inputDevices;

import java.util.Scanner;

public class Keyboard {

	private Scanner keyb;
	private String lastInput; //Saves the last input
	
	public Keyboard()
	{
		keyb = new Scanner(System.in);
		lastInput = null;
	}
	
	public int nextInt()
	{
		String input  = keyb.nextLine().trim();
		lastInput = input;
		int result = -1;
		
			try
			{
				result = Integer.parseInt(input);
					
			}
			catch(NumberFormatException e)
			{
			  e.printStackTrace();
			}
		return result;
	}
	
	public String nextString()
	{
		lastInput = keyb.nextLine().trim();
		return lastInput;
	}
	
	public void close()
	{
		keyb.close();
	}
	
	public String getLastInput()
	{
		return lastInput;
	}

}
