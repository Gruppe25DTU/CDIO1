package test;

import dal.IUserDAO;
import dal.UserDAOBasic;
import view.IUserInterface;
import view.TUIBasic;

public class TUITest {

	

	public static void main(String[] args) {
		IUserDAO dav = new UserDAOBasic();
		IUserInterface UI = new TUIBasic(dav);
		UI.menu();

	}

}
