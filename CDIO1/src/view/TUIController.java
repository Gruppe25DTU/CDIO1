package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Stringbank.TUIBasic_Strings;
import dal.IUserDAO;
import dal.IUserDAO.DALException;
import dto.UserDTO;
import inputDevices.Keyboard;

public class TUIController {

	private IUserInterface ui;
	private IUserDAO f;
	private TUIBasic_Strings s;
	private HashMap<String, Command> commandMap;
	private boolean running;

  private class Command implements Runnable {
    
    Runnable method;
    String desc, help;
    
    public Command(Runnable method, String desc, String help) {
      this.method = method;
      this.desc = desc;
      this.help = help;

    }
    
    @Override
    public String toString() {
      return desc;
    }
    
    public String getHelp() {
      return help;
    }
    
    @Override
    public void run() {
      method.run();
    }
    
  }
	
	public TUIController(IUserInterface ui, IUserDAO f) 
	{
		this.f = f;
		this.ui = ui;
		s = new TUIBasic_Strings();
		running = true;
	}
	
	public void initCommandList() {
	commandMap = new HashMap<>();
    Command create = new Command(this::createUser, "Create a new User", "Help for Create User goes here!");
    Command list = new Command(this::listUsers, "List all existing Users", "Help for List goes here!");
    Command update = new Command(this::updateUser, "Update an existing User", "Help for Update User goes here!");
    Command delete = new Command(this::deleteUser, "Delete an existing User", "Help for Delete User goes here!");
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
		List<String> menu = new ArrayList<>();
		for(String key : commandMap.keySet()) {
			  menu.add("["+key+"]" + " : " + commandMap.get(key));
		}

        ui.displayMessage("\n" + s.getText(1));
		while(running) {
			//TODO: Stringbank
			ui.showMenu(menu);
			String input = "";
			try {
				input = ui.getResponse("");
			} catch (Keyboard.DALKeyboardInterruptException e) {
				e.printStackTrace();
				//TODO: Fix quit order / system!
				ui.quit();
			}
			Command cmd;
			if ((cmd = commandMap.get(input)) != null) {
			  cmd.run();
			}
			
		}
	}

	public void createUser()
	{
		try
		{
			String name, ini, cpr;
			UserDTO newUser = f.createUser();

			do{ name = chooseName(); } while (!f.setName(newUser, name));
			do{ ini = chooseInitials();} while (!f.setInitials(newUser, ini));
			do{ cpr = chooseCPR();} while (!f.setCpr(newUser, cpr));
			List<String> roles = chooseRoles();
			f.setRoles(newUser, roles);
			try
			{
				ui.displayMessage("User to be saved:\n" + newUser);
				if(ui.confirmInput())
					f.saveUser(newUser);
			}
			catch (DALException|Keyboard.DALKeyboardInterruptException e) {
				e.printStackTrace();
			}
		} catch (DALException e) {
			e.printStackTrace();
		} catch (Keyboard.DALKeyboardInterruptException e) {
			e.printStackTrace();
			return;
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
			e.printStackTrace();
			ui.displayMessage("Unable to retrieve user list!");
		}
	}

	public void updateUser()
	{
		ui.displayMessage(s.getText(4));
		UserDTO user;
		while(true)
		{
			int userId = -1;
			try {
				userId = ui.getInt("");
			} catch (Keyboard.DALKeyboardInterruptException e) {
				e.printStackTrace();
				return;
			}
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
			String input = null;
			try {
				input = ui.getResponse(s.getText(6));
			} catch (Keyboard.DALKeyboardInterruptException e) {
				e.printStackTrace();
				return;
			}
/*
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
*/
		}
	}

	public void deleteUser()
	{
		ui.displayMessage(s.getText(7));
		while(true)
		{
			int userId = -1;
			try {
				userId = ui.getInt("");
			} catch (Keyboard.DALKeyboardInterruptException e) {
				e.printStackTrace();
			}
			if(userId!=-1)
			{
				try {
					UserDTO user = f.getUser(userId);
					if (user != null) {
						ui.displayMessage("Deleting user:\n" + user);
					}
					if(ui.confirmInput())
					{
						ui.displayMessage(s.getText(8), userId);
						f.deleteUser(userId);
						break;
					}
					else
						break;

				} catch (DALException e) {
					e.printStackTrace();
				} catch (Keyboard.DALKeyboardInterruptException e) {
					e.printStackTrace();
					return;
				}
			}
			else {
				ui.displayMessage(s.getText(20));
			}
		}
	}

	private void quit()
	{
		running = false;
		ui.quit();
	}

	private String chooseName() throws Keyboard.DALKeyboardInterruptException {
		return ui.getResponse(s.getText(9));
	}

	private String chooseInitials() throws Keyboard.DALKeyboardInterruptException {
		return ui.getResponse(s.getText(10));
	}

	private String chooseCPR() throws Keyboard.DALKeyboardInterruptException {
		return ui.getResponse(s.getText(11));
	}

	//TODO
	private List<String> chooseRoles() throws Keyboard.DALKeyboardInterruptException
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
				ui.displayMessage(s.getText(20));

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

	public void run()
	{
		this.menu();
	}

}
