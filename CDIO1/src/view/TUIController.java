package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import Stringbank.TUIBasic_Strings;
import dal.IUserDAO;
import dal.IUserDAO.DALException;
import dto.UserDTO;

public class TUIController {

  private class Command implements Runnable {
    
    Runnable method;
    String sdesc, help;
    
    public Command(Runnable method, String sdesc, String help) {
      this.method = method;
      this.sdesc = sdesc;
      this.help = help;

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
	private HashMap<String, Command> commandMap;
	private boolean running;
	
	public TUIController(IUserInterface ui, IUserDAO f) 
	{
		this.f = f;
		this.ui = ui;
		s = new TUIBasic_Strings();
		running = true;
	}
	
	public void initCommandList() {
	commandMap = new HashMap<String, Command>();
    Command create = new Command(this::createUser, "Create a new User", "Help for Create User goes here!");
    Command list = new Command(this::listUsers, "List all existing Users", "Help for List goes here!");
    Command update = new Command(this::updateUser, "Update an existing User", "Help for Update User goes here!");
    Command delete = new Command(this::createUser, "Delete an existing User", "Help for Delete User goes here!");
    Command quit = new Command(this::quit, "Quit the program", "Help for Quit goes here!");
    
    commandMap.put("1", create);
    commandMap.put("2", list);
    commandMap.put("3", update);
    commandMap.put("4", delete);
    commandMap.put("5", quit);
    
	}

	public void menu()
	{
		initCommandList();
		List<String> menu = new ArrayList<String>();
		for(String key : commandMap.keySet()) {
			  menu.add("["+key+"]" + " : " + commandMap.get(key));
			}
		
		ui.displayMessage(s.getText(1));
		ui.showMenu(menu);
		while(running)
		{
			String input = ui.getResponse("");
			Command cmd;
			if ((cmd = commandMap.get(input)) != null) {
			  cmd.run();
			}
			
		}
	}

	public void createUser()
	{
		UserDTO newUser = new UserDTO();
		int userId = generateUserId();
		String name = chooseName();
		String ini = chooseInitials();
		String cpr = chooseCPR();
		List<String> roles = chooseRoles();
		String psswrd = generatePassword();	
		newUser.setCpr(cpr);
		newUser.setIni(ini);
		newUser.setPassword(psswrd);
		newUser.setRoles((ArrayList<String>)roles);
		newUser.setUserID(userId);
		newUser.setUserName(name);
		try 
		{
			if(ui.confirmInput())
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
		ui.displayMessage(s.getText(4));
		UserDTO user;
		while(true)
		{

			int userId = ui.getInt("");
			String input = ui.getLastInput();
			if(input.equals("help"))
				listUsers();
			else if(userId == -1)
			{

			}
			else
			{
				try
				{
					user = f.getUser(userId);
					if(user != null)
					{
						chooseWhatToUpdate(user);
						//f.updateUser(user);
						break;

					}
					else
						ui.displayMessage(s.getText(21));
				}
				catch(DALException e)
				{
					ui.displayMessage(e.getMessage());
				}
			}


		}
	}

	private void chooseWhatToUpdate(UserDTO user)
	{
		boolean changing = true;
		while(changing)
		{
			ui.displayMessage(s.getText(5), user.getUserName());
			String input = ui.getResponse(s.getText(6));

			switch(input)
			{
			case "1": user.setUserName(chooseName());
			break;
			case "2": user.setIni(chooseInitials());
			break;
			case "3": user.setRoles((ArrayList<String>)chooseRoles());
			break;
			case "4": changing = false;
			break;
			default : ui.displayMessage(s.getText(20));
			break;
			}

		}
	}

	public void deleteUser()
	{
		ui.displayMessage(s.getText(7));
		while(true)
		{
			int userId = ui.getInt("");
			String input = ui.getLastInput();
			if(input.equals("help"))
			{
				listUsers();
			}
			else
			{
				if(userId!=-1)
				{
					try {
						if(ui.confirmInput())
						{
							ui.displayMessage(s.getText(8), userId);
							f.deleteUser(userId);
							break;
						}
						else
							break;

					} catch (DALException e) {
						System.out.println(e.getMessage());
					}
				}
				else
					ui.displayMessage(s.getText(20));


			}

		}
	}

	private void quit()
	{
		running = false;
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
		while(true)
		{
			String input = ui.getResponse(s.getText(11));
			if(correctCPRFormat(input))
				return input;
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
		return password;
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
						if(i == users.get(i).getUserID())
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

}
