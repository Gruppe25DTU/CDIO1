package test;

import dal.IUserDAO;
import dal.UserDAOBasic;
import view.IUserInterface;
import view.TUIBasic;
import view.TUIController;

public class ControllerTest {


	public static void main(String[] args) {
		IUserInterface ui = new TUIBasic();
		IUserDAO f = new UserDAOBasic();
		TUIController ctrl = new TUIController(ui,f);
		ctrl.run();

	}

}
