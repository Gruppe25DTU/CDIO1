package persistency;

import dto.UserDTO;

import java.sql.*;
import java.util.*;

import dal.IUserDAO.DALException;

public class DatabaseSaver implements IPersistency {
	private static Connection conn;


	public DatabaseSaver() {
		try {
			init();
			createTable();
			addInitialInformation();
			createView();
		} catch (DALException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Initialize the connection
	 */
	public void init() throws DALException {
		try {
			conn = getConnection();
		} catch (DALException e) {
			// TODO Auto-generated catch block
			throw e;
		}
	}

	/**
	 * Adds a user to the database:
	 */
	private static boolean addToTable(UserDTO user)  {
		boolean returnb = false;
		int userID = user.getUserID();
		String userName = user.getUserName();
		String ini = user.getIni();
		String cpr = user.getCpr();
		String password = user.getPassword();




		String consistantStatement = "INSERT INTO users VALUES('%d','%s','%s','%s','%s');";
		String statement = String.format(consistantStatement, userID, userName, ini, cpr, password);

		PreparedStatement addToTable;
		try {
			addToTable = conn.prepareStatement(statement);
			addToTable.executeUpdate();
			for (String role : user.getRoles()) {
				addRole(userID,role);
			}
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
	 *
	 * @throws Exception
	 */
	private static void createTable() throws DALException{
		try {
			String users = 
					"CREATE TABLE IF NOT EXISTS users ("
							+ "userID int(2) NOT NULL,"
							+ "userName VARCHAR(20) NOT NULL,"
							+ "ini VARCHAR(4) NOT NULL,"
							+ "cpr VARCHAR(11) NOT NULL,"
							+ "password VARCHAR(30) NOT NULL,"
							+ "PRIMARY KEY(userID)"
							+ ");"
							;
			String roles = "create table if not exists roles ("
					+ "role_ID varchar(7),"
					+ "roleName varchar(30), primary key(role_ID));	";
			String worksAs = "create table if not exists worksAs ("
					+ "u_ID	int(2),"
					+ "r_ID varchar(7),"
					+ "role_nr int(3),"
					+ "primary key (u_ID,role_nr),"
					+ "foreign key (r_ID) references roles (role_ID) on delete set null,"
					+ "foreign key (u_ID) references users (userID)	on delete cascade"
					+ ");";


			PreparedStatement createUsers = conn.prepareStatement(users);
			createUsers.executeUpdate();
			PreparedStatement createRoles = conn.prepareStatement(roles);
			createRoles.executeUpdate();
			PreparedStatement createWorksAs = conn.prepareStatement(worksAs);
			createWorksAs.executeUpdate();

		} catch (Exception e) {
			throw new DALException("Error occured while creating database tables");
		} finally {
		}
	}

	private static void addInitialInformation() throws DALException {
		try {
			String admin = "insert into roles values('a1','Admin');";
			String pharmacist = " insert into roles values('a2','Pharmacist');";
			String foreman = "insert into roles values('a3','Foreman');";
			String operator = "insert into roles values('a4','Operator');";
			PreparedStatement adminInformation = conn.prepareStatement(admin);
			adminInformation.executeUpdate();
			PreparedStatement pharmacistInformation = conn.prepareStatement(pharmacist);
			pharmacistInformation.executeUpdate();
			PreparedStatement foremanInformation = conn.prepareStatement(foreman);
			foremanInformation.executeUpdate();
			PreparedStatement operatorInformation = conn.prepareStatement(operator);
			operatorInformation.executeUpdate();


		} catch (SQLException e) {
			throw new DALException("Error occured while adding roles to role table");
		}
	}

	private static void addRole(int userID,String r_ID) {
		String statement = "insert into worksAs values('%d','%s','%d');";
		String role_ID = "";
		int role_nr = 0;
		switch(r_ID) {
		case "Admin" : {
			role_ID = "a1";
			role_nr = 0;
			break;
		}
		case "Pharmacist" : {
			role_ID = "a2";
			role_nr = 1;
			break;
		}
		case "Foreman" : {
			role_ID = "a3";
			role_nr = 2;
			break;
		}
		case "Operator" : {
			role_ID = "a4";
			role_nr = 3;
			break;
		}
		}
		statement = String.format(statement, userID,role_ID,role_nr);

		try {
			PreparedStatement addRole = conn.prepareStatement(statement);
			addRole.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}


	private static ArrayList<String> getRoles(int userID) {
		ArrayList<String> returnList = new ArrayList<String>();

		try {
			String statement = "select roleName from userDTO where userID = '%d';";
			statement = String.format(statement, userID);
			PreparedStatement getRoles = conn.prepareStatement(statement);
			ResultSet result = getRoles.executeQuery();
			while (result.next()) {

				returnList.add(result.getString("roleName"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Nothing to fetch/user not found");
		}
		return returnList;

	}    


	private static void createView() {
		try {
			String statement = "create or replace view userDTO as select users.*,roles.roleName "
					+ "from (users natural left outer join roles) natural left outer join worksAs "
					+ "where roles.role_ID = worksAs.r_ID and users.userID = worksAs.u_ID;";
			PreparedStatement createView = conn.prepareStatement(statement);
			createView.executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}


	/**
	 * Connects to the database.
	 *
	 * @return Conncection con
	 * @throws Exception
	 */
	private static Connection getConnection() throws DALException {
		try {
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/25cdio01";
			String username = "root";
			String password = "";
			Class.forName(driver);

			Connection conn = DriverManager.getConnection(url, username, password);
			System.out.println("Connected.");
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DALException("Error occured while trying to connect to database");
		}

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
	public ArrayList<UserDTO> getUserList() throws DALException {
		ArrayList<UserDTO> list = new ArrayList<>();


		try {
			String statement = "select * from users;";
			PreparedStatement stmt = conn.prepareStatement(statement);

			boolean results = stmt.execute();

			//Loop through the available result sets.
			do {
				if (results) {
					ResultSet result = stmt.getResultSet();

					//Show data from the result set.
					while (result.next()) {
						ArrayList<String> information = new ArrayList<>();
						ArrayList<String> roles = new ArrayList<>();

						information.add(result.getString("userID"));
						information.add(result.getString("userName"));
						information.add(result.getString("ini"));
						information.add(result.getString("cpr"));
						information.add(result.getString("password"));

						roles = getRoles(Integer.parseInt(information.get(0)));
						try {
							list.add(arrayToUserDTO(information, roles));
						}
						catch (ArrayIndexOutOfBoundsException e) {}

					}

					result.close();
				}
				results = stmt.getMoreResults();
			} while (results);
			stmt.close();

		}
		catch(NullPointerException e)
		{
			throw new DALException("No connection");
		} 
		catch (SQLException e)
		{
			throw new DALException("Error with database");
		}

		return list;
	}


	@Override
	public Set<Integer> getUserIDList() throws DALException{
		String statement = "select userID from users;";
		Set<Integer> list = new HashSet<>();

		try {
			PreparedStatement stmt = conn.prepareStatement(statement);
			boolean results = stmt.execute();
			//Loop through the available result sets.
			do {
				if (results) {
					ResultSet result = stmt.getResultSet();
					//Add data to ArrayList
					while (result.next()) {
						list.add(result.getInt("userID"));
					}

					result.close();
				}
				results = stmt.getMoreResults();
			} while (results);
			stmt.close();
		} catch (NullPointerException e) {
			throw new DALException("Error occured when getting User ID's");
		} catch (SQLException e) {
			throw new DALException("Error occured when getting User ID's");
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
	 *
	 * @param user
	 */
	@Override
	public void updateUser(UserDTO user, int userID) {
		String consistantStatement = "update users set userID = %d, userName = '%s', ini = '%s', cpr = '%s', password = '%s' where userID = %d;";
		int newUserID = user.getUserID();
		String userName = user.getUserName();
		String ini = user.getIni();
		String cpr = user.getCpr();
		String password = user.getPassword();


		String statement = String.format(consistantStatement, newUserID, userName, ini, cpr, password, userID);

		try {
			PreparedStatement update = conn.prepareStatement(statement);
			deleteRoles(userID);
			for (String role : user.getRoles()) {
				addRole(newUserID,role);
			}

			update.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Bitch why u no work?!");
		}

	}

	/**
	 * Takes two arrayLists and returns a UserDTO.
	 *
	 * @param information
	 * @param roles
	 * @return UserDTO
	 */
	private static UserDTO arrayToUserDTO(List<String> information, List<String> roles) {
		if(information.size()==0) {
			return null;

		};
		int userID = Integer.parseInt(information.get(0));
		String userName = information.get(1);
		String ini = information.get(2);
		String cpr = information.get(3);
		String password = information.get(4);

		return new UserDTO(userID, userName, ini, cpr, password, roles);

	}

	private void deleteRoles(int userID) {
		String statement = "delete from worksAs where u_ID = '%d'";
		statement = String.format(statement, userID);
		try {
			PreparedStatement delete = conn.prepareStatement(statement);
			delete.executeUpdate();
		} catch (SQLException e) {
			//Nothing happens here
		}

	}


	/**
	 * loads a user from the database.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public UserDTO getUser(int userID) throws DALException {
		String statement = "Select * FROM users WHERE UserID = '%d'";
		statement = String.format(statement, userID);
		List<String> array = new ArrayList<>();
		List<String> roles = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(statement);

			ResultSet result = preparedStatement.executeQuery();


			while (result.next()) {
				array.add(result.getString("userID"));
				array.add(result.getString("userName"));
				array.add(result.getString("ini"));
				array.add(result.getString("cpr"));
				array.add(result.getString("password"));

				roles = getRoles(Integer.parseInt(array.get(0)));
			}
		} catch (Exception e) {
			throw new DALException("Can't retrieve user");
		}
		UserDTO user = null;
		try {
			user = arrayToUserDTO(array,roles);
		}
		catch (ArrayIndexOutOfBoundsException e ) {}
		return user;
	}

	/**
	 * Sets the connection to null
	 */
	public void quit() {
		conn = null;
	}

}


