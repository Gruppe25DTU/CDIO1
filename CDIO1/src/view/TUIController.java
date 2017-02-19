package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Stringbank.TUIBasic_Strings;
import dal.IUserDAO;
import dal.IUserDAO.DALException;
import dto.UserDTO;

public class TUIController {

	protected class Command implements Runnable {

		Runnable method;
		String sdesc, trigger, help;

		public Command(Runnable method, String sdesc, String help, String trigger) {
			this.method = method;
			this.sdesc = sdesc;
			this.help = help;
			this.trigger = trigger;

		}

		@Override
		public String toString() {
			return sdesc;
		}

		public String getHelp() {
			return help;
		}



		@Override
		public void run() {
			method.run();
		}

	}

	private IUserInterface ui;
	private IUserDAO f;
	private TUIBasic_Strings s;
	private boolean stayInMenu;
	private UserDTO userSelected; //The current user being worked upon


	public TUIController(IUserInterface ui, IUserDAO f) 
	{
		this.f = f;
		this.ui = ui;
		s = new TUIBasic_Strings();
		stayInMenu = true;
	}


	public void menu()
	{
		Menu mainMenu = new Menu(s.getText(1));
		mainMenu.addCommand(new Command(this::createUser, "Create a new User", "Help for Create User goes here!", "1"));
		mainMenu.addCommand(new Command(this::listUsers, "List all existing Users", "Help for List goes here!","2"));
		mainMenu.addCommand(new Command(this::updateUser, "Update an existing User", "Help for Update User goes here!", "3"));
		mainMenu.addCommand(new Command(this::deleteUser, "Delete an existing User", "Help for Delete User goes here!", "4"));
		mainMenu.addCommand(new Command(this::quit, "Quit the program", "Help for Quit goes here!", "5"));
		while(stayInMenu)
		{
			String input = ui.getResponse(mainMenu.toString());
			if (mainMenu.get(input) != null) {
				mainMenu.get(input).run();;
			}
			else
			{
				ui.displayMessage("ERROR: Unknown command");
			}

		}
	}

	public void createUser()
	{
		userSelected = new UserDTO();
		Menu createM = new Menu("User selected: "+userSelected.toString());
		createM.addCommand(new Command(this::chooseName,"Enter the user's name", "Help", "1"));
		createM.addCommand(new Command(this::chooseInitials, "Enter the user's initials","Help" ,"2"));
		createM.addCommand(new Command (this::chooseCPR, "Enter the user's CPR number", "Help", "3"));
		createM.addCommand(new Command(this::chooseRoles, "Choose the roles for the user","Help","4"));
		createM.addCommand(new Command(this::generatePassword, "Generate a new password", "Help","5"));
		createM.addCommand(new Command(this::resetUser, "Start over", "Help", "6"));
		createM.addCommand(new Command(this::finishCreate, "Save user", "Help", "7"));
		createM.addCommand(new Command(this::goBack, "Exit user creation", "Help", "8"));
		int userId = generateUserId();
		userSelected.setUserId(userId);
		while(stayInMenu)
		{
			createM.setMsg("User selected: "+userSelected.toString());
			String input = ui.getResponse(createM.toString());
			if(createM.get(input) != null)
				createM.get(input).run();

		}
		stayInMenu = true;

	}
	public void finishCreate()
	{
		try 
		{
			if(ui.confirmInput())
				f.createUser(userSelected);
		} 
		catch (DALException e) {
			System.out.println(e.getMessage());
		}	
	}

	public void resetUser()
	{
		userSelected = new UserDTO();
		userSelected.setUserID(generateUserId());
	}

	public void listUsers()
	{
		try 
		{
			List<UserDTO> users = f.getUserList();
			ui.displayMessage(s.getText(2));
			if(users!=null)
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
		userSelected = null;
		Menu updateM = new Menu("");
		updateM.addCommand(new Command(this::getUser, "Select a user to update", "Help", "1"));
		updateM.addCommand(new Command(this::listUsers, "List all users", "Help", "2"));
		updateM.addCommand(new Command(this::chooseWhatToUpdate, "Choose what to update", "Help", "3"));
		updateM.addCommand(new Command(this::goBack, "Exit user update", "Help", "4"));
		while(stayInMenu)
		{
			String user = userSelected!=null?userSelected.toString(): "--NO USER--";
			updateM.setMsg("User Selected: "+ user);
			String input = ui.getResponse(updateM.toString());
			if(updateM.get(input)!=null)
				updateM.get(input).run();

		}
		stayInMenu = true;


	}


	public void getUser()
	{
		
		try
		{
			int input = ui.getInt("Enter the User ID");
			userSelected = f.getUser(input);
			if(userSelected == null)
				ui.displayMessage(s.getText(21));
		}
		catch(DALException e)
		{
			ui.displayMessage(e.getMessage());
		}
	}

	public void finishUpdate()
	{
		try
		{
			f.setCpr(userSelected, userSelected.getCpr());
			f.setID(userSelected, userSelected.getUserID());
			f.setInitials(userSelected, userSelected.getIni());
			f.setName(userSelected, userSelected.getUserName());
			f.setPwd(userSelected, userSelected.getPassword());
		}
		catch(DALException e)
		{
			ui.displayMessage(e.getMessage());
		}
	}

	private void chooseWhatToUpdate()
	{
		if(userSelected == null)
			ui.displayMessage("ERROR: No user selected");
		else
		{
			Menu chooseM = new Menu("");
			chooseM.addCommand(new Command(this::chooseName, "Update the name", "Help", "1"));
			chooseM.addCommand(new Command(this::chooseInitials, "Update the initials","Help", "2"));
			chooseM.addCommand(new Command(this::chooseCPR, "Update the CPR number", "Help", "3"));
			chooseM.addCommand(new Command(this::chooseRoles, "Update the user's roles", "Help", "4"));
			chooseM.addCommand(new Command(this::finishUpdate, "Save the updates", "Help", "5"));
			chooseM.addCommand(new Command(this::goBack, "Exit update menu","Help","6"));
			while(stayInMenu)
			{
				chooseM.setMsg("User selected: "+userSelected.toString());

				String input = ui.getResponse(chooseM.toString());
				if(chooseM.get(input)!=null)
					chooseM.get(input).run();

			}
			stayInMenu = true;
		}

	}

	public void deleteUser()
	{
		userSelected = null;
		Menu deleteM = new Menu("");
		deleteM.addCommand(new Command(this::getUser, "Which user do you wish to delete?", "Help", "1"));
		deleteM.addCommand(new Command(this::listUsers, "List all the users", "Help", "2"));
		deleteM.addCommand(new Command(this::finishDelete, "Delete the user","Help", "3"));
		deleteM.addCommand(new Command(this::goBack, "Exit delete menu", "Help", "4"));
		while(stayInMenu)
		{
			String user = userSelected!=null?userSelected.toString():"--NO USER--";
			deleteM.setMsg("User Selected: "+user);
			String input = ui.getResponse(deleteM.toString());
			if(deleteM.get(input)!=null)
				deleteM.get(input).run();

		}
		stayInMenu = true;
	}
	
	public void finishDelete()
	{
		try
		{
			if(ui.confirmInput())
			{
				f.deleteUser(userSelected.getUserID());
				userSelected = null;
			}
				
		}
		catch(DALException e)
		{
			ui.displayMessage(e.getMessage());
		}
	}

	private void quit()
	{
		stayInMenu = false;
		ui.quit();
	}

	private void chooseName()
	{
		while(true)
		{
			String input = ui.getResponse(s.getText(9));
			if(input.length()<2)
				ui.displayMessage("Name too short");
			else if(input.length()>20)
				ui.displayMessage("Name too long");
			else
			{
				userSelected.setUserName(input);
				break;
			}

		}

	}

	private void chooseInitials()
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
			{
				userSelected.setIni(input);
				break;
			}

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

	private void chooseCPR()
	{
		while(true)
		{
			String input = ui.getResponse(s.getText(11));
			if(correctCPRFormat(input))
			{
				userSelected.setCpr(input);
				break;
			}		
			else
				ui.displayMessage("Incorrect CPR format");
		}
	}

	private boolean correctCPRFormat(String cpr)
	{
		//CPR number has to be symbols long
		if(cpr.length()!=11)
			return false;
		//checking if the cprNumber has the hyphen and that each side of hyphen is as long as it should be
		String[] cprParts = cpr.split("-");
		if(cprParts.length!=2 || cprParts[0].length()!=6 || cprParts[1].length()!=4)
			return false;

		//Checking if the cpr contains anything other than numbers
		for(char c : cprParts[0].toCharArray())
			if(!(c>='0' && c<='9'))
				return false;

		for(char c : cprParts[1].toCharArray())
			if(!(c>='0' && c<='9'))
				return false;

		return true;
	}

	private void chooseRoles()
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
			for(int i = 0; i<roles.size();i++)
			{
				msg += "\n"+String.format(s.getText(18), i+1, roles.get(i));
			}
			msg += "\n"+String.format(s.getText(18), roles.size()+1, "Done choosing");
			String input = ui.getResponse(msg);
			if(input.equals(""+(roles.size()+1)))
			{
				roles.clear();
			}
			else if(input.equals("1") || input.equals("2") || input.equals("3") || input.equals("4"))
			{
				if(Integer.parseInt(input)-1<roles.size())
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
		userSelected.setRoles((ArrayList<String>)chosenRoles);
	}

	private void generatePassword()
	{
		Random gen = new Random();
		String password = "";
		//Randomly decides whether the password should have 3-5 Capital letters, 3-5 small letters, and 3-5 numbers
		int cpLet = gen.nextInt(3)+3;
		int smlLet = gen.nextInt(3)+3;
		int nmbs = gen.nextInt(3)+3;
		int[] all = {cpLet,smlLet,nmbs};
		for(int i = 0; i<cpLet+smlLet+nmbs;i++)
		{
			int index = gen.nextInt(3);
			if(all[index]>0)
			{
				char c = 0;
				all[index]--;
				if(index == 0)
					c += gen.nextInt(26)+'A';
				else if(index == 1)
					c += gen.nextInt(26)+'a';
				else
					c += gen.nextInt(10)+'0';
				password += c;
			}
			else
				i--;
		}
		userSelected.setPassword(password);
	}

	private int generateUserId()
	{
		try {
			List<UserDTO> users = f.getUserList();
			if(users!=null)
				for(int i = 11; i<100; i++)
				{
					boolean idInUse = false;
					for(int k = 0 ; k<users.size();k++)
					{
						if(i == users.get(i-11).getUserID())
							idInUse = true;
					}
					if(!idInUse)
						return i;
				}
		} catch (DALException e) {
			System.out.println(e.getMessage());
		}
		return -1;
	}
	public void run()
	{
		this.menu();
	}



	public void goBack()
	{
		stayInMenu = false;
	}

}
