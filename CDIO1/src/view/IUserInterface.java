package view;

import inputDevices.Keyboard;

import java.util.List;

public interface IUserInterface {

	void displayMessage(String msg, Object...args);
	String getResponse(String msg, Object...args) throws Keyboard.DALKeyboardInterruptException;
	int getInt(String msg, Object...args) throws Keyboard.DALKeyboardInterruptException;
	String getLastInput();
	boolean confirmInput() throws Keyboard.DALKeyboardInterruptException;
	/**
	 * Shows a list of all the  available options
	 * And asks for input
	 */
	void menu();
	
	void showMenu(List<String> menuCommands);

	/**
	 * Lists all the users on the disk / database
	 */
	void listUsers();

	/**
	 * Creates a user with the inputted details
	 */
	void createUser();

	/**
	 * Asks which user details to change, and then changes them
	 */
	void updateUser();

	/**
	 * Asks which user to delete by entering their ID, then deletes them
	 */
	void deleteUser();

	/**
	 * Quits the program 
	 */
	void quit();

}
