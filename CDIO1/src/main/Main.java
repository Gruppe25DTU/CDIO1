package main;

import dal.IUserDAO;
import dal.UserDAOBasic;
import persistency.DatabaseSaver;
import persistency.FileSaver;
import persistency.IPersistency;
import persistency.MemorySaver;
import view.IUIController;
import view.TUIDisplay;
import view.TUIController;

import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        IPersistency persistency = persistencyChooser();
        IUserDAO logic = new UserDAOBasic(persistency);
        IUIController ctrl = new TUIController(logic);
        ctrl.run();

    }

    private static IPersistency persistencyChooser() {
        IPersistency persistency;
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("Please choose a way to store data");
            System.out.println("Save to Database: 1\nSave to File: 2\nSave to Memory: 3\n");
            try {
                int input = Integer.valueOf(scanner.nextLine());
                persistency = persistencyFactory(input);
                if (persistency == null) {
                    System.out.println("Please enter a number 1, 2 or 3\n");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number 1, 2 or 3\n");
            }
        }
        return persistency;
    }

    private static IPersistency persistencyFactory(int input) {
        switch (input) {
            case 1:
                return new DatabaseSaver();
            case 2:
                return new FileSaver();
            case 3:
                return new MemorySaver();
            default:
                return null;
        }
    }

}
