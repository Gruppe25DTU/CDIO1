package persistency;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.xml.bind.DatatypeConverter;
import javax.xml.crypto.Data;

import dto.UserDTO;

public class DatabaseSaver implements IPersistency{
	private static Connection conn;


	public DatabaseSaver() {
		init();
		try {
			createTable();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the connection
	 */
	public void init() {
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
	private static boolean addToTable(UserDTO user) {
		boolean returnb = false;
		int userID = user.getUserID();
		String userName = user.getUserName();
		String ini = user.getIni();
		String cpr = user.getCpr();
		String password = user.getPassword();
		String roles = "";

		//TODO: Method in UserDTO?
		for (String role : user.getRoles()) {
			roles += ";" + role;
		}
		if (roles != "") {
			roles = roles.substring(1);
		}

		String  consistantStatement = "INSERT INTO users VALUES('%d','%s','%s','%s','%s','%s');";
		String statement = String.format(consistantStatement, userID,userName,ini,cpr,password,roles);

		PreparedStatement addToTable;
		try {
			addToTable = conn.prepareStatement(statement);
			addToTable.executeUpdate();
			returnb = true;

		} catch (SQLException e) {
			returnb = false;
			e.printStackTrace();
		}
		return returnb;
	}


	/**
	 * Creates the main table 'users'. <br>
	 * Contains the values: <br>
	 * userID. <br>
	 * userName <br>
	 * initials "ini" <br>
	 * cpr <br>
	 * password <br>
	 * role <br>
	 * userID is the primary key <br>
	 * @throws Exception
	 */
	private static void createTable() throws Exception {
		try{
			String statement = "CREATE TABLE IF NOT EXISTS users("
					+ "userID int(2) NOT NULL, "
					+ "userName VARCHAR(20) NOT NULL,"
					+ "ini VARCHAR(4) NOT NULL, "
					+ "cpr VARCHAR(11) NOT NULL, "
					+ "password VARCHAR(30) NOT NULL,"
					+ "role VARCHAR(256) NOT NULL, "
					+ "PRIMARY KEY(userID));";


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
	private static Connection getConnection() throws Exception {
		try{
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/cdio1";
			String username = "cdio";
			String password = "Gruppe25DTU!CDIO";
			Class.forName(driver);

			Connection conn = DriverManager.getConnection(url,username,password);
			System.out.println("Connected");
			return conn;
		} catch(Exception e) {
			e.printStackTrace();
		}


		return null;
	}

	@Override
	public boolean deleteUser(int userID) {
		String consistantStatement = "delete from users where userID = %d";
		consistantStatement = String.format(consistantStatement, userID);
		try {
			PreparedStatement statement = conn.prepareStatement(consistantStatement);
			statement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
            return false;
		}
        return true;
    }

	@Override
	@SuppressWarnings("unchecked")
	public ArrayList<UserDTO> getUserList() {
		ArrayList<UserDTO> list = new ArrayList<UserDTO>();


		try {
			String statement = "select * from users;";
			PreparedStatement stmt = conn.prepareStatement(statement);

			boolean results = stmt.execute();

			//Loop through the available result sets.
			do {
				if(results) {
					ResultSet result = stmt.getResultSet();

					//Show data from the result set.
					while (result.next()) {
						ArrayList<String> information = new ArrayList<String>();
						ArrayList<String> roles = new ArrayList<String>();

						information.add(result.getString("userID"));
						information.add(result.getString("userName"));
						information.add(result.getString("ini"));
						information.add(result.getString("cpr"));
						information.add(result.getString("password"));


						//TODO ("role" and) ";" should be a constant string
						roles.addAll(Arrays.asList(result.getString("role").split(";")));
						list.add(arrayToUserDTO(information,roles));
					}

					result.close();
				}
				results = stmt.getMoreResults();
			} while(results);
			stmt.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public Set<Integer> getUserIDList() {
		String statement = "select userID from users;";
		Set<Integer> list = new HashSet<>();

		try {
			PreparedStatement stmt = conn.prepareStatement(statement);
			boolean results = stmt.execute();
			//Loop through the available result sets.
			do {
				if(results) {
					ResultSet result = stmt.getResultSet();
					//Add data to ArrayList
					while (result.next()) {
						list.add(result.getInt("userID"));
					}

					result.close();
				}
				results = stmt.getMoreResults();
			} while(results);
			stmt.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

	/**
	 * Saves a non existing user. 
	 * return true if it's able to add the user to the database,
	 * return false if not.
	 */
	@Override
	public boolean save(UserDTO user) {
		return addToTable(user);
	}

	/**
	 * Updates the users data. Relies on userID.
	 * @param user
	 */
	@Override
	public void updateUser(UserDTO user,int userID) {
		String consistantStatement = "update users set userID = %d, userName = '%s', ini = '%s', cpr = '%s', password = '%s', role = '%s' where userID = %d;";
		int newUserID = user.getUserID();
		String userName = user.getUserName();
		String ini = user.getIni();
		String cpr = user.getCpr();
		String password = user.getPassword();
		String roles = "";

		//TODO: Method in UserDTO?
		for (String role : user.getRoles()) {
			roles += ";" + role;
		}
		if (roles != "") {
			roles = roles.substring(1);
		}


		String statement = String.format(consistantStatement,newUserID,userName,ini,cpr,password,roles,userID);

		try {
			PreparedStatement update = conn.prepareStatement(statement);
			update.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Takes two arrayLists and returns a UserDTO.
	 * @param information
	 * @param roles
	 * @return UserDTO
	 */
	private static UserDTO arrayToUserDTO(ArrayList<String> information, ArrayList<String> roles) {
		int userID = Integer.parseInt(information.get(0));
		String userName = information.get(1);
		String ini = information.get(2);
		String cpr = information.get(3);
		String password = information.get(4);


		UserDTO user = new UserDTO(userID,userName,ini,cpr,password,roles);
		return user;
	}

	/**
	 * loads a user from the database.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public UserDTO getUser(int userID) {
		String statement = "Select * FROM users WHERE UserID = '%d'";
		statement = String.format(statement, userID);
		ArrayList<String> array = new ArrayList<String>();
		ArrayList<String> roles = new ArrayList<String>();
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(statement);

			ResultSet result = preparedStatement.executeQuery();



			while(result.next()) {
				array.add(result.getString("userID"));
				array.add(result.getString("userName"));
				array.add(result.getString("ini"));
				array.add(result.getString("cpr"));
				array.add(result.getString("password"));

				//TODO ("role" and) ";" should be a constant string
				roles.addAll(Arrays.asList(result.getString("role").split(";")));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return arrayToUserDTO(array,roles);
	}
	
	/**
	 * Sets the connection to null
	 */
	public void quit() {
		conn = null;
	}

}


