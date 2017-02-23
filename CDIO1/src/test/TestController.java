package test;

import dal.IUserDAO;
import dal.UserDAOBasic;
import persistency.DatabaseSaver;
import persistency.FileSaver;
import persistency.IPersistency;
import view.IUIController;
import view.TUIDisplay;
import view.TUIController;

public class TestController {


    public static void main(String[] args) {
        IPersistency persistency = new DatabaseSaver();
        IUserDAO logic = new UserDAOBasic(persistency);
        TUIDisplay ui = new TUIDisplay();
        IUIController ctrl = new TUIController(ui, logic);
        ctrl.run();

    }

}
