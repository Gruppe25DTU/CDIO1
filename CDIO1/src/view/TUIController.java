package view;

import java.util.ArrayList;
import java.util.List;

import Stringbank.TUIBasic_Strings;
import dal.IUserDAO;
import dal.IUserDAO.DALException;
import dto.UserDTO;

public class TUIController {

	private IUserInterface ui;
	private IUserDAO f;
	private TUIBasic_Strings s;
	public TUIController(IUserInterface ui, IUserDAO f) {
		this.f = f;
		this.ui = ui;
	}
	
	public void menu()
	{
		ui.displayMessage(s.getText(1));
		while(true)
		{
			String input = ui.getResponse(s.getText(0));
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
			default : ui.displayMessage(s.getText(20));
			break;
			}
		}
	}
	
	public void createUser()
	{
		String name = chooseName();
		String ini = chooseInitials();
		String cpr = chooseCPR();
		List<String> roles = chooseRoles();
		String psswrd = generatePassword();
		int userId = generateUserId();
		UserDTO newUser = new UserDTO();
		newUser.setCpr(cpr);
		newUser.setIni(ini);
		newUser.setPassword(psswrd);
		newUser.setRoles(roles);
		newUser.setUserID(userId);
		newUser.setUserName(name);
		try 
		{
			f.createUser(newUser);
		} 
		catch (DALException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	public void listUsers()
	{
		try 
		{
			List<UserDTO> users = f.getUserList();
			ui.displayMessage(s.getText(2));
			for(int i = 0; i<users.size();i++)
			{
				ui.displayMessage(".."+users.get(i));
			}
		} catch (DALException e) 
		{
			System.out.println(e.getMessage());
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
		ui.quit();
	}
	
	private String chooseName()
	{
		while(true)
		{
			String input = ui.getResponse(s.getText(9));
			if(input.length()<2)
				ui.displayMessage("Name too short");
			else if(input.length()>20)
				ui.displayMessage("Name too long");
			else
				return input;
		}
		
	}
	
	private String chooseInitials()
	{
		while(true)
		{
			String input = ui.getResponse(s.getText(10));
			
			if(containsNumbers(input))
				ui.displayMessage("initials can't contain numbers");
			else if(input.length()<2)
				ui.displayMessage("Not enough initials");
			else if(input.length()>4)
				ui.displayMessage("Too many Initials");
			else
				return input;
		}
	}
	
	private boolean containsNumbers(String str)
	{
		for(char c : str.toCharArray())
		{
			if(c>='0' && c<= '9')
				return true;
		}
		return false;
	}
	
	private String chooseCPR()
	{
		return "";
	}
	
	private List<String> chooseRoles()
	{
		List<String> roles = new ArrayList<String>();
		List<String> chosenRoles = new ArrayList<String>();
		roles.add(s.getText(12));
		roles.add(s.getText(13));
		roles.add(s.getText(14));
		roles.add(s.getText(15));
		ui.displayMessage(s.getText(16));
		while(roles.size()>0)
		{
			//Prints a list of all the available roles
			String msg = s.getText(17);
			ui.displayMessage(s.getText(17));
			for(int i = 0; i<roles.size();i++)
			{
				msg += String.format(s.getText(18), i+1, roles.get(i));
			}
			msg += String.format(s.getText(18), roles.size()+1, "Done choosing");
			String input = ui.getResponse(msg);
			if(input.equals(""+(roles.size()+1)))
			{
				roles.clear();
			}
			else if(input.equals("1") || input.equals("2") || input.equals("3") || input.equals("4"))
			{
				chosenRoles.add(roles.get(Integer.parseInt(input)-1));
				//Removes the roles from the options list
				if(roles.get(Integer.parseInt(input)-1).equals(s.getText(13)) || 
						roles.get(Integer.parseInt(input)-1).equals(s.getText(14)))
				{
					//Makes sure that these two roles are mutually exclusive
					roles.remove(s.getText(13));
					roles.remove(s.getText(14));
				}
				else
					roles.remove(Integer.parseInt(input)-1);
			}
			else
				System.out.println(s.getText(20));
			
			String chosenRolesMsg = s.getText(19);
			for(int i = 0; i<chosenRoles.size();i++)
			{
				chosenRolesMsg += chosenRoles.get(i)+".. ";
			}
			chosenRolesMsg += "\n";
			ui.displayMessage(chosenRolesMsg);
		}
		return chosenRoles;
	}
	
	private String generatePassword()
	{
		return "";
	}
	
	private int generateUserId()
	{
		return 0;
	}
	public void run()
	{
		this.menu();
	}

}
