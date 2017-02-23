package inputDevices;

import java.util.Scanner;

public class Keyboard {

    public static final String KEYBOARD_INTERRUPT_CODE = "quit()";
    private Scanner keyb;
    private String lastInput = ""; //Saves the last input

    public class DALKeyboardInterruptException extends Exception {
    }

    public Keyboard() {
        keyb = new Scanner(System.in);
        lastInput = null;
    }

    public int nextInt() throws DALKeyboardInterruptException {
        String input = keyb.nextLine().trim();
        if (input.equals(KEYBOARD_INTERRUPT_CODE)) {
            throw new DALKeyboardInterruptException();
        }
        lastInput = input;
        int result = -1;

        try {
            result = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            
        }
        return result;
    }

    public String nextString() throws DALKeyboardInterruptException {
        String input = keyb.nextLine().trim();
        if (input.equals(KEYBOARD_INTERRUPT_CODE)) {
            throw new DALKeyboardInterruptException();
        }
        lastInput = input;
        return input;
    }

    public void close() {
        keyb.close();
    }

    public String getLastInput() {
        return lastInput;
    }

}
