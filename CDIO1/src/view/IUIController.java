package view;



public interface IUIController {

    /**
     * Shows a list of all the  available options
     * And asks for input
     */
    void menu();

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
    
    void run();

}
