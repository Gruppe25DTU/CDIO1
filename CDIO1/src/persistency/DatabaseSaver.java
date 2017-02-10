package persistency;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.UserDTO;

public class DatabaseSaver implements IPersistency{
	private static Connection conn;


	/**
	 * Initialize the connection
	 */
	public static void init() {
		try {
			conn = getConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Adds a user to the database:
	 */
	public static void addToTable(UserDTO user) {
		int userID = user.getUserID();
		String userName = user.getUserName();
		String ini = user.getIni();
		String cpr = user.getCpr();
		String password = user.getPassword();
		List<String> role = user.getRoles();

		String  consistantStatement = "INSERsT INTO users VALUES('%d','%s','%s','%s','%s','%s');";
		String statement = String.format(consistantStatement, userID,userName,ini,cpr,password,role);

		PreparedStatement addToTable;
		try {
			addToTable = conn.prepareStatement(statement);
			addToTable.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * Creates the main table users. <br>
	 * Contains the values: <br>
	 * userID. <br>
	 * userName <br>
	 * initials "ini" <br>
	 * cpr <br>
	 * password <br>
	 * role <br>
	 * userID is the primary key <br>
	 * @param statement
	 * @throws Exception
	 */
	public static void createTable() throws Exception {
		try{
			String statement = "CREATE TABLE IF NOT EXISTS users("
					+ "userID int(2) NOT NULL, "
					+ "userName VARCHAR(20) NOT NULL,"
					+ "ini VARCHAR(4) NOT NULL, "
					+ "cpr VARCHAR(11) NOT NULL, "
					+ "password VARCHAR(30) NOT NULL,"
					+ "role VARCHAR(20) NOT NULL, "
					+ "PRIMARY KEY(userID),"
					+ "PRIMARY KEY(userName));";

			PreparedStatement create = conn.prepareStatement(statement);
			create.executeUpdate();


		}
		catch (Exception e) {
			System.out.println(e);
		}
		finally {System.out.println("Function complete"); }
	}

	/**
	 * Connects to the database.
	 * @return Conncection con
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception {
		try{
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/cdio01";
			String username = "root";
			String password = "";
			Class.forName(driver);

			Connection conn = DriverManager.getConnection(url,username,password);
			System.out.println("Connected");
			return conn;
		} catch(Exception e) {
			System.out.println(e);
		}


		return null;
	}

	@Override
	public boolean save(ArrayList<UserDTO> list) {
		String statement = "Select ";
		for(int i = 0;i<list.size();i++) {
			UserDTO user = list.get(i);
			
			addToTable(user);
			
		}
		return true;
		// TODO Auto-generated method stub
		
	}

	/**
	 * Updates the users data. Relies on dd
	 * @param user
	 */
	public void update(UserDTO user) {
		String consistantStatement = "update users set userName = '%s', ini = '%s', cpr = '%s', password = '%s', role = '%s' where userID = %d;";
		int userID = user.getUserID();
		String userName = user.getUserName();
		String ini = user.getIni();
		String cpr = user.getCpr();
		String password = user.getPassword();
		List<String> role = user.getRoles();
		
		
		String statement = String.format(consistantStatement,userName,ini,cpr,password,role,userID);

		try {
			PreparedStatement update = conn.prepareStatement(statement);
			update.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

	@Override
	public ArrayList<UserDTO> load() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void opdateUser(UserDTO user, String userID) {
		// TODO Auto-generated method stub
		
	}

}


