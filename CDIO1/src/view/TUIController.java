package view;

import Stringbank.TUIBasic_Strings;
import dal.IUserDAO;
import dal.IUserDAO.DALException;
import dto.UserDTO;
import inputDevices.Keyboard;

import java.util.*;

public class TUIController implements IUIController {

    private boolean updating = false;
    private int selectedOriginalID;
    private UserDTO selected;
    private TUIDisplay ui;
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

    public TUIController(IUserDAO f) {
        this.f = f;
        this.ui = new TUIDisplay();
        s = new TUIBasic_Strings();
        running = true;
    }

    private interface UpdateMethod extends Runnable {
        void method() throws DALException, Keyboard.DALKeyboardInterruptException;

        default void run() {
            try {
                method();
            } catch (DALException | Keyboard.DALKeyboardInterruptException e) {
                
            }
        }
    }

    public void initCommandList() {
        Command create = new Command(this::createUser, "Create a new User", "Help for Create User goes here!");
        Command list = new Command(this::listUsers, "List all existing Users", "Help for List goes here!");
        Command update = new Command(this::updateUser, "Update an existing User", "Help for Update User goes here!");
        Command delete = new Command(this::deleteUser, "Delete an existing User", "Help for Delete User goes here!");
        Command quit = new Command(this::quit, "Quit the program", "Help for Quit goes here!");
        commandMap = new HashMap<>();
        commandMap.put("1", create);
        commandMap.put("2", list);
        commandMap.put("3", update);
        commandMap.put("4", delete);
        commandMap.put("5", quit);


        Command name = new Command((UpdateMethod) this::chooseName, "Update Name", "Help for Update Name goes here");
        Command initials = new Command((UpdateMethod) this::chooseInitials, "Update Initials", "Help for Update Initials goes here");
        Command cpr = new Command((UpdateMethod) this::chooseCPR, "Update CPR", "Help for Update CPR goes here");
        Command roles = new Command((UpdateMethod) this::chooseRoles, "Update Roles", "Help for Update Roles goes here");
        Command finish = new Command((UpdateMethod) this::finishUpdate, "Save Changes", "Help for Save Changes goes here");
        Command pwd = new Command((UpdateMethod) this::choosePwd, "Update Password", "Help for Update Password goes here");
        updateMap = new HashMap<>();
        updateMap.put("1", name);
        updateMap.put("2", initials);
        updateMap.put("3", cpr);
        updateMap.put("4", roles);
        updateMap.put("5", pwd);
        updateMap.put("6", finish);
    }

    public void menu() {
        initCommandList();
        List<String> menu = new ArrayList<>();
        for (String key : commandMap.keySet()) {
            menu.add("[" + key + "]" + " : " + commandMap.get(key));
        }
        String quit = Keyboard.KEYBOARD_INTERRUPT_CODE;
        ui.displayMessage("\n" + s.getText(1)
                + (quit.length() > 0 ? "Type '" + quit + "' to go back a level, or exit the program from the main menu"
                : ""));
        while (running) {
            //TODO: Stringbank
            ui.showMenu(menu);
            String input = "";
            try {
                input = ui.getResponse("");
            } catch (Keyboard.DALKeyboardInterruptException e) {
                ui.displayMessage("System shutting down...");
                //TODO: Fix quit order / system!
                quit();
            }
            Command cmd;
            if ((cmd = commandMap.get(input)) != null) {
                cmd.run();
            }

        }
    }

    public void createUser() {
        try {
            UserDTO newUser = f.createUser();
            chooseName(newUser);
            chooseInitials(newUser);
            chooseCPR(newUser);
            chooseRoles(newUser);
            try {
                ui.displayMessage("User to be saved:\n" + newUser);
                if (ui.confirmInput())
                    f.saveUser(newUser);
            } catch (DALException | Keyboard.DALKeyboardInterruptException e) {
            	ui.displayMessage(e.getMessage());
                return;
            }
        } catch (DALException e) {
            ui.displayMessage(e.getMessage());
        } catch (Keyboard.DALKeyboardInterruptException e) {
            return;
        }
    }

    public void listUsers() {
        try {
            List<UserDTO> users = f.getUserList();
            ui.displayMessage(s.getText(2));
            if (users != null)
                for (UserDTO user : users) {
                    ui.displayMessage(".." + user);
                }
        } catch (DALException e) {
            ui.displayMessage("Unable to retrieve user list!");
        }
    }

    public void updateUser() {
        ui.displayMessage(s.getText(4));
        UserDTO user;
        while (true) {
            int userId = -1;
            try {
                userId = ui.getInt("");
            } catch (Keyboard.DALKeyboardInterruptException e) {
                return;
            }
            if (userId != -1) {
                try {
                    user = f.getUser(userId);
                    if (user != null) {
                        chooseWhatToUpdate(user);
                        break;
                    } else
                        ui.displayMessage(s.getText(21));
                } catch (DALException e) {
                    ui.displayMessage(e.getMessage());
                    return;
                }
            }
            else
            	return;
        }
    }

    private void chooseWhatToUpdate(UserDTO user) {
        selected = new UserDTO(user);
        selectedOriginalID = user.getUserID();
        List<String> menu = new ArrayList<>();
        for (String key : updateMap.keySet()) {
            menu.add("[" + key + "]" + " : " + updateMap.get(key));
        }

        updating = true;
        while (updating) {
            ui.displayMessage(s.getText(5), user.getUserName());
            ui.showMenu(menu);
            String input = "";
            try {
                input = ui.getResponse("");
            } catch (Keyboard.DALKeyboardInterruptException e) {
                return;
            }
            Command cmd;
            if ((cmd = updateMap.get(input)) != null) {
                cmd.run();
            }
        }
    }

    public void deleteUser() {
        ui.displayMessage(s.getText(7));
        while (true) {
            int userId = -1;
            try {
                userId = ui.getInt("");
            } catch (Keyboard.DALKeyboardInterruptException e) {
                ui.displayMessage("Going back to the menu...");
                return;
            }
            if (userId != -1) {
                try {
                    UserDTO user = f.getUser(userId);
                    if (user != null) {
                        ui.displayMessage("Deleting user:\n" + user);
                    }
                    if (ui.confirmInput()) {
                        ui.displayMessage(s.getText(8), userId);
                        f.deleteUser(userId);
                        break;
                    } else
                        break;

                } catch (DALException e) {
                	ui.displayMessage(e.getMessage());
                	return;
                } catch (Keyboard.DALKeyboardInterruptException e) {
                    return;
                }
            } else {
                ui.displayMessage(s.getText(20));
            }
        }
    }

    public void quit() {
        running = false;
        f.quit();
        ui.quit();
    }

    private String chooseName(UserDTO user) throws Keyboard.DALKeyboardInterruptException, DALException {
        String name = user.getUserName();
        do {
            ui.displayMessage(f.getNameReq().toString());
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
            ui.displayMessage(f.getIniReq().toString());
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
            ui.displayMessage(f.getCprReq().toString());
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

    private Set<String> chooseRoles(UserDTO user) throws Keyboard.DALKeyboardInterruptException {
        Set<String> chosenRoles = user.getRoles();
        HashSet<String> roles = f.getAvailableRoles(user);
        ui.displayMessage(s.getText(16));
        while (roles.size() > 0) {
            chosenRoles = user.getRoles();
            //Print a list of all chosen roles
            String chosenRolesMsg = s.getText(19);
            for (int i = 0; i < chosenRoles.size(); i++) {
                chosenRolesMsg += "\n\t" + chosenRoles.toArray()[i];
            }
            chosenRolesMsg += "\n";
            ui.displayMessage(chosenRolesMsg);

            //Prints a list of all the available roles
            String msg = s.getText(17);
            for (int i = 0; i < roles.size(); i++) {
                msg += "\n" + String.format(s.getText(18), i + 1, roles.toArray()[i]);
            }
            msg += "\n" + String.format(s.getText(18), roles.size() + 1, "Done choosing");
            msg += "\n" + String.format(s.getText(18), roles.size() + 2, "Clear user's roles");

            ui.displayMessage(msg);

            int input = ui.getInt("") - 1;
            if (input < 0) {
                continue;
            }
            if (input < roles.size()) {
                f.addRole(user, (String) roles.toArray()[input]);
                roles = f.getAvailableRoles(user);
            } else if (input == roles.size()) {
                roles.clear();
            } else if (input == roles.size() + 1) {
                user.setRoles(new HashSet<>());
                roles = f.getAvailableRoles(user);
            } else {
                ui.displayMessage(s.getText(20));
            }
        }
        return chosenRoles;
    }

    private Set<String> chooseRoles() throws Keyboard.DALKeyboardInterruptException {
        if (selected != null) {
            return chooseRoles(selected);
        }
        return null;
    }

    private void choosePwd() throws Keyboard.DALKeyboardInterruptException, DALException {
        if (selected != null) {
            String pwd;
            do {
                ui.displayMessage(f.getPwdReq().toString());
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

    public void run() {
        this.menu();
    }

}
