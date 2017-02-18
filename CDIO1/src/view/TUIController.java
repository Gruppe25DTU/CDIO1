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

	private boolean updating = false;
	private int selectedOriginalID;
	private UserDTO selected;
	private IUserInterface ui;
	private IUserDAO f;
	private TUIBasic_Strings s;
	private HashMap<String, Command> commandMap;
	private HashMap<String, Command> updateMap;
	private boolean running;

	private class Command {
    
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

	private interface UpdateMethod extends Runnable {
		void method() throws DALException, Keyboard.DALKeyboardInterruptException;
		default void run() {
			try {
				method();
			} catch (DALException e) {
				e.printStackTrace();
			} catch (Keyboard.DALKeyboardInterruptException e) {
				e.printStackTrace();
			}
		}
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

		updateMap = new HashMap<>();
		Command name = new Command(new UpdateMethod() {
			@Override
			public void method() throws Keyboard.DALKeyboardInterruptException, DALException {
				chooseName();
			}
		} , "Update Name", "Help for Update Name goes here");
		Command initials = new Command(new UpdateMethod() {
			@Override
			public void method() throws Keyboard.DALKeyboardInterruptException, DALException {
				chooseInitials();
			}
		}, "Update Initials", "Help for Update Initials goes here");
		Command cpr = new Command(new UpdateMethod() {
			@Override
			public void method() throws Keyboard.DALKeyboardInterruptException, DALException {
				chooseCPR();
			}
		}, "Update CPR", "Help for Update CPR goes here");
		Command roles = new Command(new UpdateMethod() {
			@Override
			public void method() throws Keyboard.DALKeyboardInterruptException {
				chooseRoles();
			}
		}, "Update Roles", "Help for Update Roles goes here");
		Command finish = new Command(new UpdateMethod() {
			@Override
			public void method() throws Keyboard.DALKeyboardInterruptException, DALException {
				finishUpdate();
			}
		}, "Save Changes", "Help for Save Changes goes here");
		Command pwd = new Command(new UpdateMethod() {
			@Override
			public void method() throws Keyboard.DALKeyboardInterruptException, DALException {
				choosePwd();
			}
		}, "Update Password", "Help for Update Password goes here");
		updateMap.put("1", name);
		updateMap.put("2", initials);
		updateMap.put("3", cpr);
		updateMap.put("4", roles);
		updateMap.put("5", pwd);
		updateMap.put("6", finish);
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
			UserDTO newUser = f.createUser();
			chooseName(newUser);
			chooseInitials(newUser);
			chooseCPR(newUser);
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
				return;
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
			if (userId != -1)
			{
				try
				{
					user = f.getUser(userId);
					if(user != null)
					{
						chooseWhatToUpdate(user);
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
		selected = new UserDTO(user);
		selectedOriginalID = user.getUserID();
		List<String> menu = new ArrayList<>();
		for(String key : updateMap.keySet()) {
			menu.add("["+key+"]" + " : " + updateMap.get(key));
		}

		updating = true;
		while(updating)
		{
			ui.displayMessage(s.getText(5), user.getUserName());
			ui.showMenu(menu);
			String input = "";
			try {
				input = ui.getResponse("");
			} catch (Keyboard.DALKeyboardInterruptException e) {
				e.printStackTrace();
				return;
			}
			Command cmd;
			if ((cmd = updateMap.get(input)) != null) {
				cmd.run();
			}
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
				return;
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

	private String chooseName(UserDTO user) throws Keyboard.DALKeyboardInterruptException, DALException {
		String name = user.getUserName();
		do {
		    //TODO: FIX hardcoded string!
		    ui.displayMessage(f.getRequirement("name"));
			name = ui.getResponse(s.getText(9));
		} while (!f.setName(user, name));
		return name;
	}

	private String chooseName() throws DALException, Keyboard.DALKeyboardInterruptException {
		if (selected != null) {
			return chooseName(selected);
		}
		throw new DALException("No user selected!");
	}

	private String chooseInitials(UserDTO user) throws Keyboard.DALKeyboardInterruptException, DALException {
		String ini = user.getIni();
		do {
            //TODO: FIX hardcoded string!
            ui.displayMessage(f.getRequirement("init"));
			ini = ui.getResponse(s.getText(10));
		} while (!f.setInitials(user, ini));
		return ini;
	}

	private String chooseInitials() throws DALException, Keyboard.DALKeyboardInterruptException {
		if (selected != null) {
			return chooseInitials(selected);
		}
		throw new DALException("No user selected!");
	}

	private String chooseCPR(UserDTO user) throws Keyboard.DALKeyboardInterruptException, DALException {
		String cpr = user.getCpr();
		do {
            //TODO: FIX hardcoded string!
            ui.displayMessage(f.getRequirement("cpr"));
			cpr = ui.getResponse(s.getText(11));
		} while (!f.setCpr(user, cpr));
		return cpr;
	}

	private String chooseCPR() throws DALException, Keyboard.DALKeyboardInterruptException {
		if (selected != null) {
			return chooseCPR(selected);
		}
		throw new DALException("No user selected!");
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

    private void choosePwd() throws Keyboard.DALKeyboardInterruptException, DALException {
        if (selected != null) {
            String pwd;
            do {
                ui.displayMessage(f.getRequirement("pwd"));
                pwd = ui.getResponse(s.getText(24));
            } while (!f.setPwd(selected, pwd));
        }
    }

	private void finishUpdate() throws DALException {
		if (selected != null) {
			f.updateUser(selected, selectedOriginalID);
		}
		updating = false;
	}

	public void run()
	{
		this.menu();
	}

}
