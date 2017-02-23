package view;

import inputDevices.Keyboard;

import java.util.List;

public class TUIDisplay{

    private Keyboard keyb;

    public TUIDisplay() {
        keyb = new Keyboard();

    }

    public void showMenu(List<String> menuCommands) {
        for (String singlCom : menuCommands)
            System.out.println(singlCom);
    }

    public void quit() {
        keyb.close();
    }

 
    public void displayMessage(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    
    public String getResponse(String msg, Object... args) throws Keyboard.DALKeyboardInterruptException {
        System.out.println(String.format(msg, args));
        return keyb.nextString();
    }

    
    public int getInt(String msg, Object... args) throws Keyboard.DALKeyboardInterruptException {
        System.out.println(String.format(msg, args));
        return keyb.nextInt();
    }

    
    public String getLastInput() {
        return keyb.getLastInput();
    }

    
    public boolean confirmInput() throws Keyboard.DALKeyboardInterruptException {
        System.out.println("Are you sure? (y/n)");
        String input = keyb.nextString();
        return input.length() == 1 && input.equalsIgnoreCase("y");
    }



}
