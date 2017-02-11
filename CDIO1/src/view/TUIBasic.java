package view;

import java.util.ArrayList;
import java.util.List;

import dal.IUserDAO;
import dal.IUserDAO.DALException;
import dto.UserDTO;
import inputDevices.Keyboard;

public class TUIBasic implements IUserInterface {

	private Keyboard keyb;

	public TUIBasic() {
		keyb = new Keyboard();
		
	}

	public void menu()
	{

	}

	public void listUsers()
	{

	}

	public void createUser()
	{
		
	}

	public void updateUser()
	{

	}


	public void deleteUser()
	{
	}

	public void quit()
	{
		keyb.close();
	}

	@Override
	public void displayMessage(String msg, Object...args) {
		System.out.println(String.format(msg, args));
	}

	@Override
	public String getResponse(String msg, Object...args) {
		System.out.println(String.format(msg, args));
		return keyb.nextString();
	}

	@Override
	public int getInt(String msg, Object... args) {
		System.out.println(String.format(msg, args));
		return keyb.nextInt();
	}

	@Override
	public String getLastInput() {
		return keyb.getLastInput();
	}

	@Override
	public boolean confirmInput() {
		System.out.println("Are you sure? (y/n)");
		String input = keyb.nextString();
		if(input.length() == 1 && input.equalsIgnoreCase("y"))
			return true;
		return false;
	}
	
	

}
