package Stringbank;

public class TUIBasic_Strings {

    private String[] strings = {
            "[1] : Create user \n"
                    + "[2] : List users \n"
                    + "[3] : Change user details \n"
                    + "[4] : Delete user \n"
                    + "[5] : Close program \n",

            "Welcome to the User Management System \n You have the following options:",

            "Listing users...",

            "User has been created",

            "Enter the User ID for the user you wish to update."
                    + " \n or type or an invalid userID or text to go back to the main menu.",

            "\n Updating the user: \n %s",

            "[1] - change name \n"
                    + "[2] - change initials \n"
                    + "[3] - change roles \n"
                    + "[4] - done \n",

            "Enter the ID of the person you want to delete \n"
                    + " or type \"help\" to see a list of all users",

            "Deleting... %d",

            "Enter the name of the user: ",

            "Enter their initials: ",

            "Please enter the users CPR-number",

            "Admin", "Pharmacist", "Foreman", "Operator",

            "Choose the user's roles",

            "Available roles: ",

            "--[%d] - %s",

            "Current chosen roles: ",

            "ERROR: Unknown command",

            "No user exists with that ID",

            "The User ID has to be a number",

            "Error trying to get user",

            "Enter new password",
    };


    /**
     * @param i
     * @return 0 = Main menu options <br>
     * 1 = Main menu greetings <br>
     * 2 = listing users msg <br>
     * 3 = user has been created msg <br>
     * 4 = choose user to update msg <br>
     * 5 = current user being updated (needs user name as arg)<br>
     * 6 = choose what to update msg <br>
     * 7 = choose user to delete <br>
     * 8 = deleting progress msg (needs userID as arg)<br>
     * 9 = enter name msg<br>
     * 10 = enter initials msg <br>
     * 11 = enter CPR number msg <br>
     * 12 = Admin role <br>
     * 13 = Pharmacist role <br>
     * 14 = Foreman role <br>
     * 15 = Operator role <br>
     * 16 = choose user roles msg <br>
     * 17 = show available roles msg <br>
     * 18 = default [Command] - command description msg
     * (needs a number for the command and string for the description)<br>
     * 19 = current chosen roles <br>
     * 20 = Unknown command ERROR<br>
     * 21 = no user exists with id ERROR <br>
     * 22 = UserID has to be a number ERROR <br>
     * 23 = cant get user ERROR <br>
     * 24 = choose a password <br>
     */
    public String getText(int i) {
        return strings[i];
    }

}
