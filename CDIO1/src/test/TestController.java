package test;

import dal.IUserDAO;
import dal.UserDAOBasic;
import view.IUserInterface;
import view.TUIBasic;
import view.TUIController;

public class TestController {


	public static void main(String[] args) {
		IUserDAO f = new UserDAOBasic();
		IUserInterface ui = new TUIBasic();
		TUIController ctrl = new TUIController(ui,f);
		ctrl.run();

	}

}
