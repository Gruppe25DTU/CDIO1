package view;

import java.util.List;

public interface IUserInterface {

	void displayMessage(String msg, Object...args);
	String getResponse(String msg, Object...args);
	int getInt(String msg, Object...args);
	String getLastInput();
	boolean confirmInput();
	/**
	 * Quits the program 
	 */
	void quit();

}
