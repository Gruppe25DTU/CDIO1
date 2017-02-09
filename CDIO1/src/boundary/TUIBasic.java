package boundary;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import dal.IUserDAO;
import dal.IUserDAO.DALException;
import dto.UserDTO;

public class TUIBasic {
	
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
		String commands = "[1] : Create user \n"
				 + "[2] : List users \n"
				 + "[3] : Change user details \n"
				 + "[4] : Delete user \n"
				 + "[5] : Close program \n";
		
		System.out.println("Welcome to the User Management System™ \n You have the following options:");
		System.out.println(commands);
		
		while(running)
		{
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
				default : System.out.println("Unknown command - please see the command list");
				System.out.println(commands);
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
		List<String> roles = new ArrayList<String>();
		List<String> chosenRoles = new ArrayList<String>();
		roles.add("Admin");
		roles.add("Pharmacist");
		roles.add("Foreman");
		roles.add("Operator");
		System.out.println("Enter the name of the user: ");
		String name = keyb.nextLine();
		System.out.println("Enter their initials: ");
		String ini = keyb.nextLine();
		System.out.println("Choose the user's roles");
		while(roles.size()>0)
		{
			System.out.println("Available roles: ");
			for(int i = 0; i<roles.size();i++)
			{
				System.out.println(" --["+i+1+"] - "+roles.get(i));
			}
			System.out.println(" --["+roles.size()+1+"] - Done choosing");
			String input = keyb.nextLine();
			if(input.equals(""+roles.size()+1))
			{
				roles.clear();
			}
			
		}
	}
	
	public void updateUser()
	{
		
	}
	
	public void deleteUser()
	{
		
	}
	
	public void quit()
	{
		running = false;
		keyb.close();
	}

}
