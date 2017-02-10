package test;
import dto.UserDTO;
import sql.DatabaseSaver;

public class Test {
	public static void main(String[] args) {
		
		boolean igang
		
		while(!igang) {
			
			igang = false;
		}
		while(!exit) 
		
		if(n == 'n') 
			exit = false;
		
		/*
		 * @param userID
		 * @param userName
		 * @param ini
		 * @param cpr
		 * @param password
		 * @param role
		 */

		UserDTO user = new UserDTO(11,"Anders And", "AA","123456-7890","asdf", "Admin");		
		DatabaseSaver.init();
		try {
			DatabaseSaver.createTable();

			DatabaseSaver.addToTable(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}






	}

}
