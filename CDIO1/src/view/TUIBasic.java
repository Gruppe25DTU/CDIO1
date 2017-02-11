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
//		System.out.println("Enter the User ID for the user you wish to update."
//				+ " \n or type \"help\" to see a list of all users.");
//		UserDTO user;
//		boolean changing = true;
//		while(changing)
//		{
//			String input = keyb.nextString();
//			int userID;
//			if(input.equals("help"))
//				listUsers();
//			else
//			{
//				try
//				{
//					userID = Integer.parseInt(input);
//					user = userController.getUser(userID);
//					if(user != null)
//					{
//						System.out.println("\n Updating the user: \n"+user);
//						String commands = "[1] - change name \n"
//										+ "[2] - change initials \n"
//										+ "[3] - change roles \n"
//										+ "[4] - done \n";
//						
//						while(changing)
//						{
//							System.out.println(commands);
//							input = keyb.nextString();
//							switch(input)
//							{
//							case "1" : user.setUserName(chooseName());
//							break;
//							case "2" : user.setIni(chooseInitials());
//							break;
//							case "3" : user.setRoles(chooseRoles());
//							break;
//							case "4" : changing = false;
//							break;
//							default : System.out.println("ERROR: Unknown command");
//							}
//						}
//					}
//					else
//						System.out.println("No user exists with that ID");
//					
//					
//				}
//				catch (NumberFormatException e)
//				{
//					System.out.println("The User ID has to be a number");
//				}
//				catch(DALException e)
//				{
//					System.out.println("Error trying to get user");
//				}
//			}
//
//
//		}


	}

//	public void deleteUser()
//	{
//		System.out.println("Enter the ID of the person you want to delete \n"
//						 + " or type \"help\" to see a list of all users");
//		boolean deleting = true;
//		while(deleting)
//		{
//			String input = keyb.nextString();
//			if(input.equals("help"))
//			{
//				listUsers();
//			}
//			else
//			{
//				int userID = keyb.nextInt();
//				if(userID!=-1)
//				{
//					try {
//						System.out.println("Deleting..."+userController.getUser(userID));
//						userController.deleteUser(userID);
//						deleting = false;
//					} catch (DALException e) {
//						System.out.println(e.getMessage());
//					}
//				}
//				else
//					System.out.println("ERROR: Unknown command");
//				
//				
//			}
//			
//		}
//	}

//	public void quit()
//	{
//		running = false;
//		keyb.close();
//	}
//	
//
//	@Override
//	public void displayMessage(String msg, Object...args) {
//		System.out.println(String.format(msg, args));
//	}
//
//	@Override
//	public String getResponse(String msg, Object...args) {
//		System.out.println(String.format(msg, args));
//		return keyb.nextString();
//	}

}
