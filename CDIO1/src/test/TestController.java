package test;

import dal.IUserDAO;
import dal.UserDAOBasic;
import persistency.DatabaseSaver;
import persistency.IPersistency;
import view.IUserInterface;
import view.TUIBasic;
import view.TUIController;

public class TestController {


    public static void main(String[] args) {
        IPersistency persistency = new DatabaseSaver();
        IUserDAO logic = new UserDAOBasic(persistency);
        IUserInterface ui = new TUIBasic();
        TUIController ctrl = new TUIController(ui, logic);
        ctrl.run();

    }

}
