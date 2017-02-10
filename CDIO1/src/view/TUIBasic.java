package view;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import dal.IUserDAO;
import dal.IUserDAO.DALException;
import dto.UserDTO;

public class TUIBasic implements IUserInterface {

	private IUserDAO userController;
	Scanner keyb;
	private boolean running;

	public TUIBasic(IUserDAO uCtrl) {
		this.userController = uCtrl;
		keyb = new Scanner(System.in);
		running = true;
	}

	public void menu()
	{
		String commands = 
				  "[1] : Create user \n"
				+ "[2] : List users \n"
				+ "[3] : Change user details \n"
				+ "[4] : Delete user \n"
				+ "[5] : Close program \n";

		System.out.println("Welcome to the User Management System™ \n You have the following options:");

		while(running)
		{
			System.out.println(commands);
			String input = keyb.nextLine();
			switch(input)
			{
			case "1": createUser();
			break;
			case "2": listUsers();
			break;
			case "3": updateUser();
			break;
			case "4": deleteUser();
			break;
			case "5": quit();
			break;
			default : System.out.println("ERROR: Unknown command");
			break;
			}
		}

	}

	public void listUsers()
	{
		System.out.println("Listing users...");
		try 
		{
			List<UserDTO> users = userController.getUserList();
			for(int i = 0; i<users.size();i++)
			{
				System.out.println("... "+users.get(i));
			}
		} 
		catch (DALException e) 
		{
			e.printStackTrace();
		}

	}

	public void createUser()
	{
		
		String name, ini, cpr;
		name = chooseName();
		ini = chooseInitials();
		List<String> chosenRoles = chooseRoles();
		cpr = chooseCPR();
		//String password = userController.generatePassword();
		//userController.createUser(Insert stuff here);
	}

	public void updateUser()
	{
		System.out.println("Enter the User ID for the user you wish to update."
				+ " \n or type \"help\" to see a list of all users.");
		UserDTO user;
		boolean changing = true;
		while(changing)
		{
			String input = keyb.nextLine();
			int userID;
			if(input.equals("help"))
				listUsers();
			else
			{
				try
				{
					userID = Integer.parseInt(input);
					user = userController.getUser(userID);
					if(user != null)
					{
						System.out.println("\n Updating the user: \n"+user);
						String commands = "[1] - change name \n"
										+ "[2] - change initials \n"
										+ "[3] - change roles \n"
										+ "[4] - done \n";
						
						while(changing)
						{
							System.out.println(commands);
							input = keyb.nextLine();
							switch(input)
							{
							case "1" : user.setUserName(chooseName());
							break;
							case "2" : user.setIni(chooseInitials());
							break;
							case "3" : user.setRoles(chooseRoles());
							break;
							case "4" : changing = false;
							break;
							default : System.out.println("ERROR: Unknown command");
							}
						}
					}
					else
						System.out.println("No user exists with that ID");
					
					
				}
				catch (NumberFormatException e)
				{
					System.out.println("The User ID has to be a number");
				}
				catch(DALException e)
				{
					System.out.println("Error trying to get user");
				}
			}


		}


	}

	public void deleteUser()
	{
		
	}

	public void quit()
	{
		running = false;
		keyb.close();
	}
	
	private String chooseName()
	{
		System.out.println("Enter the name of the user: ");
		return keyb.nextLine();
	}
	
	private String chooseInitials()
	{
		System.out.println("Enter their initials: ");
		return keyb.nextLine();
	}
	
	private String chooseCPR()
	{
		System.out.println("Please enter the users CPR-number");
		return keyb.nextLine();
	}
	
	private List<String> chooseRoles()
	{
		List<String> roles = new ArrayList<String>();
		List<String> chosenRoles = new ArrayList<String>();
		roles.add("Admin");
		roles.add("Pharmacist");
		roles.add("Foreman");
		roles.add("Operator");
		System.out.println("Choose the user's roles");
		while(roles.size()>0)
		{
			System.out.print("Current chosen roles: ");
			for(int i = 0; i<chosenRoles.size();i++)
			{
				
			}
			System.out.println();
			
			//Prints a list of all the available roles
			System.out.println("Available roles: ");
			for(int i = 0; i<roles.size();i++)
			{
				System.out.println(" --["+(i+1+"] - "+roles.get(i)));
			}
			System.out.println(" --["+(roles.size()+1)+"] - Done choosing");


			String input = keyb.nextLine();
			if(input.equals(""+(roles.size()+1)))
			{
				roles.clear();
			}
			else if(input.equals("1") || input.equals("2") || input.equals("3") || input.equals("4"))
			{
				chosenRoles.add(roles.get(Integer.parseInt(input)-1));
				//Removes the roles from the options list
				if(roles.get(Integer.parseInt(input)-1).equals("Pharmacist") || 
						roles.get(Integer.parseInt(input)-1).equals("Foreman"))
				{
					//Makes sure that these two roles are mutually exclusive
					roles.remove("Pharmacist");
					roles.remove("Foreman");
				}
				else
					roles.remove(Integer.parseInt(input)-1);
			}
			else
				System.out.println("ERROR: Unknown command");
		}
		return chosenRoles;
	}

}
